package Jitflix.Jitflix.s3;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.*;
import software.amazon.awssdk.transfer.s3.progress.LoggingTransferListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
public class S3Service {
    private final S3AsyncClient s3Client;
    private final S3TransferManager s3TransferManager;
    private final S3Buckets s3Buckets;
    public S3Service(S3AsyncClient s3Client, S3Buckets s3Buckets, S3TransferManager s3TransferManager) {
        this.s3Client = s3Client;
        this.s3Buckets = s3Buckets;
        this.s3TransferManager = s3TransferManager;

    }


    public void putObject(String bucketName, String key,byte[] file){
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

//        s3Client.putObject(objectRequest, RequestBody.fromBytes(file)).join();

        CompletableFuture<PutObjectResponse> future = s3Client.putObject(objectRequest,
                AsyncRequestBody.fromBytes(file));
    }

    public static void
    getObjectBytes (S3Client s3, String bucketName, String keyName, String path) {

        try {
            GetObjectRequest objectRequest = GetObjectRequest
                    .builder()
                    .key(keyName)
                    .bucket(bucketName)
                    .build();

            ResponseBytes<GetObjectResponse> objectBytes = s3.getObjectAsBytes(objectRequest);
            byte[] data = objectBytes.asByteArray();

            // Write the data to a local file.
            File myFile = new File(path );
            OutputStream os = new FileOutputStream(myFile);
            os.write(data);
            System.out.println("Successfully obtained bytes from an S3 object");
            os.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public byte[] getObject(String bucketName, String key){
        GetObjectRequest objectRequest = GetObjectRequest.builder()
                .key(key)
                .bucket(bucketName)
                .build();

        CompletableFuture<ResponseBytes<GetObjectResponse>> futureBytes = s3Client.getObject(objectRequest,AsyncResponseTransformer.toBytes());

        try {
            return  futureBytes.join().asByteArray();
        } catch (CompletionException e) {
            throw new RuntimeException(e.getCause());
        }

    }

    public Integer uploadDirectory(S3TransferManager transferManager,
                                   String sourceDirectory, String bucketName){
        DirectoryUpload directoryUpload =
                transferManager.uploadDirectory(UploadDirectoryRequest.builder()
                        .source(Paths.get(sourceDirectory))
                        .bucket(s3Buckets.getBucket())
                        .build());

        CompletedDirectoryUpload completedDirectoryUpload = directoryUpload.completionFuture().join();
        completedDirectoryUpload.failedTransfers().forEach(fail ->
                System.out.println("Object [{}] failed to transfer"+ fail.toString()));
        return completedDirectoryUpload.failedTransfers().size();
    }



    public String uploadFile(S3TransferManager transferManager, String bucketName,
                             String key) {
        UploadFileRequest uploadFileRequest =
                UploadFileRequest.builder()
                        .putObjectRequest(b -> b.bucket(bucketName).key(key))
                        .addTransferListener(LoggingTransferListener.create())
                        .source(Paths.get(System.getProperty("user.home") + "/Desktop/movie-test/matrix/The_Matrix.avi"))
                        .build();

        FileUpload fileUpload = transferManager.uploadFile(uploadFileRequest);

        CompletedFileUpload uploadResult = fileUpload.completionFuture().join();
        return uploadResult.response().eTag();
    }





}
