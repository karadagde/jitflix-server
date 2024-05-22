package Jitflix.Jitflix.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.transfer.s3.S3TransferManager;

@Configuration
public class S3Config {
    @Value("${aws.region}")
    private String awsRegion;
    @Value("${cloud.aws.credentials.access-key}")
    private String awsAccessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String awsSecretKey;

    @Bean
    public S3AsyncClient s3Client() {

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(awsAccessKey,
                awsSecretKey);
        return S3AsyncClient.crtBuilder()
                .region(Region.of(awsRegion))
                .credentialsProvider(
                        StaticCredentialsProvider.create(awsCreds))
                .build();


//            .builder()
//            .overrideConfiguration(b -> b.apiCallTimeout(Duration.ofSeconds(500)).apiCallAttemptTimeout(Duration.ofSeconds(500)))
//        .region(Region.of(awsRegion))
//        .build();

    }

    @Bean
    public S3TransferManager s3TransferManager() {

        return S3TransferManager.builder()
                .s3Client(s3Client())
                .build();

    }


}
