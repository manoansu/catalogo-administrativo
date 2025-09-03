package pt.amane.infrastructure.configuration.proprieties.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class StorageProperties implements InitializingBean {

  private static final Logger logger = LoggerFactory.getLogger(StorageProperties.class);

  private String  locationPattern;

  private String  fileNamePattern;

  @Override
  public void afterPropertiesSet() {
    logger.debug(toString());
  }

  @Override
  public String toString() {
    return "StorageProperties{" +
        "locationPattern='" + locationPattern + '\'' +
        ", fileNamePattern='" + fileNamePattern + '\'' +
        '}';
  }

  public StorageProperties() {
  }

  public String getLocationPattern() {
    return locationPattern;
  }

  public void setLocationPattern(String locationPattern) {
    this.locationPattern = locationPattern;
  }

  public String getFileNamePattern() {
    return fileNamePattern;
  }

  public void setFileNamePattern(String fileNamePattern) {
    this.fileNamePattern = fileNamePattern;
  }
}
