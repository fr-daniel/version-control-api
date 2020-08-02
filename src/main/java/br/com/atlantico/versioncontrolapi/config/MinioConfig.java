package br.com.atlantico.versioncontrolapi.config;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@Configuration
public class MinioConfig {

    @Autowired
    private Environment environment;

    @Bean
    @Qualifier("bucket")
    public String getBucket() {
        return environment.getProperty("minio.bucket");
    }

//    @Bean
//    @Qualifier("bucket")
//    @Profile("prod")
//    public String getProdBucket() {
//        try {
//            return new InitialContext().lookup("java:comp/env/minio/bucket").toString();
//        } catch (NamingException e) {
//            return null;
//        }
//    }

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

//    @Bean
//    @Profile("prod")
//    public MinioClient getProdMinioClient() {
//        try {
//            Context context = (Context) new InitialContext().lookup("java:comp/env");
//
//            String serverName = context.lookup("minio/serverName").toString();
//            String accessKey = context.lookup("minio/accessKey").toString();
//            String secretKey = context.lookup("minio/secretKey").toString();
//
//            return new MinioClient(serverName, accessKey, secretKey);
//        } catch (InvalidEndpointException | InvalidPortException | NamingException e) {
//            return null;
//        }
//    }


}
