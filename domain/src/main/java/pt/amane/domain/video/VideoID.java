package pt.amane.domain.video;

import java.util.Objects;
import pt.amane.Identifier;
import pt.amane.domain.utils.IdUtils;
import pt.amane.domain.validation.ObjectsValidator;

public class VideoID extends Identifier {

  private final String value;

  public VideoID(final String value) {
    this.value = ObjectsValidator.objectValidation(value);
  }

  public static VideoID from(final String anId) {
    return new VideoID(anId.toLowerCase());
  }

  public static VideoID unique() {
    return VideoID.from(IdUtils.uuid());
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final VideoID videoID = (VideoID) o;
    return Objects.equals(getValue(), videoID.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getValue());
  }
}
