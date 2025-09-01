package pt.amane.domain.video;

import pt.amane.ValueObject;
import pt.amane.domain.validation.ObjectsValidator;

public class Resource extends ValueObject {
  private final byte[] content;
  private final String checksum;
  private final String contentType;
  private final String name;

  private Resource(
      final byte[] content,
      final String checksum,
      final String contentType,
      final String name
  ) {
    this.content = (byte[]) ObjectsValidator.objectValidation(content);
    this.checksum = ObjectsValidator.objectValidation(checksum);
    this.contentType = ObjectsValidator.objectValidation(contentType);
    this.name = ObjectsValidator.objectValidation(name);
  }

  public static Resource with(
      final byte[] content,
      final String checksum,
      final String contentType,
      final String name) {
    return new Resource(content, checksum, contentType, name);
  }

  public byte[] content() {
    return content;
  }

  public String checksum() {
    return checksum;
  }

  public String contentType() {
    return contentType;
  }

  public String name() {
    return name;
  }


}
