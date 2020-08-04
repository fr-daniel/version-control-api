package br.com.atlantico.versioncontrolapi.config;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class MinioConfig {

    @Autowired
    private Environment environment;

    @Bean
    @Qualifier("bucket")
    public String getBucket() {
        return environment.getProperty("minio.bucket");
    }

    @Bean
    public MinioClient getMinioClient() {
        try {
            String serverName = environment.getProperty("minio.serverName");
            String accessKey = environment.getProperty("minio.accessKey");
            String secretKey = environment.getProperty("minio.secretKey");

            return new MinioClient(serverName, accessKey, secretKey);
        } catch (InvalidEndpointException | InvalidPortException e) {
            return null;
        }
    }


}
