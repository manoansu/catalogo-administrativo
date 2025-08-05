package pt.amane.domain.video;

import java.util.Objects;
import pt.amane.ValueObject;
import pt.amane.domain.utils.IdUtils;
import pt.amane.domain.validation.ObjectsValidator;

public class AudioVideoMedia extends ValueObject {

  private final String id;
  private final String checksum;
  private final String name;
  private final String rawLocation;
  private final String encodedLocation;
  private final MediaStatus status;

  private AudioVideoMedia(
      final String id,
      final String checksum,
      final String name,
      final String rowLocation,
      final String encodedLocation,
      final MediaStatus status
  ) {
    this.id = ObjectsValidator.objectValidation(id);
    this.checksum = ObjectsValidator.objectValidation(checksum);
    this.name = ObjectsValidator.objectValidation(name);
    this.rawLocation = ObjectsValidator.objectValidation(rowLocation);
    this.encodedLocation = Objects.requireNonNull(encodedLocation);
    this.status = (MediaStatus) ObjectsValidator.objectValidation(status);
  }

  public static AudioVideoMedia with(
      final String checksum,
      final String name,
      final String rawLocation
  ) {
    return new AudioVideoMedia(IdUtils.uuid(), checksum, name, rawLocation, "", MediaStatus.PENDING);
  }

  public static AudioVideoMedia with(
      final String id,
      final String checksum,
      final String name,
      final String rowLocation,
      final String encodedLocation,
      final MediaStatus status
  ) {
    return new AudioVideoMedia(id, checksum, name, rowLocation, encodedLocation, status);

  }

  public static AudioVideoMedia with(AudioVideoMedia audioVideoMedia) {
    return new AudioVideoMedia(
        audioVideoMedia.id,
        audioVideoMedia.checksum,
        audioVideoMedia.name,
        audioVideoMedia.rawLocation,
        audioVideoMedia.encodedLocation,
        audioVideoMedia.status);
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

  public String rawLocation() {
    return rawLocation;
  }

  public String encodedLocation() {
    return encodedLocation;
  }

  public MediaStatus status() {
    return status;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final AudioVideoMedia that = (AudioVideoMedia) o;
    return Objects.equals(checksum, that.checksum) && Objects.equals(rawLocation,
        that.rawLocation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(checksum, rawLocation);
  }

  public AudioVideoMedia processing() {
    return AudioVideoMedia.with(
        id(),
        checksum(),
        name(),
        rawLocation(),
        encodedLocation(),
        MediaStatus.PROCESSING
    );
  }

  public AudioVideoMedia completed(final String encodedPath) {
    return AudioVideoMedia.with(
        id(),
        checksum(),
        name(),
        rawLocation(),
        encodedPath,
        MediaStatus.COMPLETED
    );
  }

  public boolean isPendingEncode() {
    return MediaStatus.PENDING == this.status;
  }
}
