package Jitflix.Jitflix.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.transfer.s3.S3TransferManager;

import java.time.Duration;

@Configuration
public class S3Config {
    @Value("${aws.region}")
    private  String awsRegion;
    @Bean
    public S3AsyncClient s3Client() {
    return S3AsyncClient.builder()
            .overrideConfiguration(b -> b.apiCallTimeout(Duration.ofSeconds(500)).apiCallAttemptTimeout(Duration.ofSeconds(500)))
        .region(Region.of(awsRegion))
        .build();

    }
    @Bean
    public S3TransferManager s3TransferManager() {

        return S3TransferManager.builder()
                .s3Client(s3Client())
                .build();

    }



}
