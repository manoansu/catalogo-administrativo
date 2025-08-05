package pt.amane.domain.video;

import java.util.Objects;
import pt.amane.ValueObject;
import pt.amane.domain.utils.IdUtils;
import pt.amane.domain.validation.ObjectsValidator;

public class ImageMedia extends ValueObject {

  private final String id;
  private final String checksum;
  private final String name;
  private final String location;

  public ImageMedia(
      final String id,
      final String checksum,
      final String name,
      final String location
  ) {
    this.id = ObjectsValidator.objectValidation(id);
    this.checksum = ObjectsValidator.objectValidation(checksum);
    this.name = ObjectsValidator.objectValidation(name);
    this.location = ObjectsValidator.objectValidation(location);
  }

  public static ImageMedia with(final String checksum, final String name, final String location) {
    return new ImageMedia(IdUtils.uuid(), checksum, name, location);
  }

  public static ImageMedia with(final String id, final String checksum, final String name, final String location) {
    return new ImageMedia(id, checksum, name, location);
  }

  public static ImageMedia with(ImageMedia imageMedia) {
    return new ImageMedia(
        imageMedia.id,
        imageMedia.checksum,
        imageMedia.name,
        imageMedia.location);
  }

  public String id() {
    return id;
  }

  public String checksum() {
    return checksum;
  }

  public String name() {
    return name;
  }

  public String location() {
    return location;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final ImageMedia that = (ImageMedia) o;
    return Objects.equals(checksum, that.checksum) && Objects.equals(location,
        that.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(checksum, location);
  }
}
