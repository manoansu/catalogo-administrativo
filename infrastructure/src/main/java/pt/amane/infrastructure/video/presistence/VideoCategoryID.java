package pt.amane.infrastructure.video.presistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import pt.amane.domain.validation.ObjectsValidator;

@Embeddable
public class VideoCategoryID implements Serializable {

  @Column(name = "video_id", nullable = false)
  private String videoId;

  @Column(name = "category_id", nullable = false)
  private String categoryId;

  /**
   * Default constructor for Hibernate JPA.
   */
  public VideoCategoryID() {
  }

  /**
   * For security this constructor is private.
   * @param videoId
   * @param categoryId
   */
  private VideoCategoryID(
      final String videoId,
      final String categoryId
  ) {
    this.videoId = ObjectsValidator.objectValidation(videoId);
    this.categoryId = ObjectsValidator.objectValidation(categoryId);
  }

  /**
   * Factory method that can be call outside of class.
   * @param videoId
   * @param categoryId
   * @return
   */
  public static VideoCategoryID from(
      final String videoId,
      final String categoryId
  ) {
    return new VideoCategoryID(videoId, categoryId);
  }

  public String getVideoId() {
    return videoId;
  }

  public String getCategoryId() {
    return categoryId;
  }

  public VideoCategoryID setVideoId(String videoId) {
    this.videoId = videoId;
    return this;
  }

  public VideoCategoryID setCategoryId(String categoryId) {
    this.categoryId = categoryId;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final VideoCategoryID that = (VideoCategoryID) o;
    return Objects.equals(getVideoId(), that.getVideoId()) && Objects.equals(
        getCategoryId(), that.getCategoryId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getVideoId(), getCategoryId());
  }
}
