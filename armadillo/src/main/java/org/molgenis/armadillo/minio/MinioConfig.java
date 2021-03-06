package org.molgenis.armadillo.minio;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import org.molgenis.armadillo.service.StorageService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties("minio")
@Validated
public class MinioConfig {
  @NotBlank private String accessKey;
  @NotBlank private String secretKey;
  private boolean secure = false;
  @NotBlank private String sharedBucket = "shared";
  @NotBlank private String userBucket = "user";
  @NotBlank private String url = "http://localhost";
  @Positive private int port = 9000;
  private String region = null;

  @Bean
  public MinioClient minioClient() throws InvalidPortException, InvalidEndpointException {
    return new MinioClient(url, port, accessKey, secretKey, region, secure);
  }

  @Bean
  public StorageService userStorageService(MinioClient minioClient) {
    return new MinioStorageService(minioClient, userBucket);
  }

  @Bean
  public StorageService sharedStorageService(MinioClient minioClient) {
    return new MinioStorageService(minioClient, sharedBucket);
  }

  public String getAccessKey() {
    return accessKey;
  }

  public void setAccessKey(String accessKey) {
    this.accessKey = accessKey;
  }

  public String getSecretKey() {
    return secretKey;
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  public boolean isSecure() {
    return secure;
  }

  public void setSecure(boolean secure) {
    this.secure = secure;
  }

  public String getSharedBucket() {
    return sharedBucket;
  }

  public void setSharedBucket(String bucket) {
    this.sharedBucket = bucket;
  }

  public String getUserBucket() {
    return userBucket;
  }

  public void setUserBucket(String bucket) {
    this.userBucket = bucket;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }
}
