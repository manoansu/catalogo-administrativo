package pt.amane.infrastructure.video.presistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import pt.amane.domain.video.ImageMedia;

@Entity(name = "ImageMedia")
@Table(name = "videos_image_media")
public class ImageMediaJpaEntity {

  @Id
  private String id;

  @Column(name = "checksum", nullable = false)
  private String checksum;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "file_path", nullable = false)
  private String filePath;

  public ImageMediaJpaEntity() {
  }


  public ImageMediaJpaEntity(
      final String id,
      final String checksum,
      final String name,
      final String filePath
  ) {
    this.id = id;
    this.checksum = checksum;
    this.name = name;
    this.filePath = filePath;
    }

  public static ImageMediaJpaEntity from(final ImageMedia media) {
    return new ImageMediaJpaEntity(
        media.id(),
        media.checksum(),
        media.name(),
        media.location()
    );
  }

  public ImageMedia toDomain() {
    return ImageMedia.with(
        getId(),
        getChecksum(),
        getName(),
        getFilePath()
    );
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getChecksum() {
    return checksum;
  }

  public ImageMediaJpaEntity setChecksum(String checksum) {
    this.checksum = checksum;
    return this;
  }

  public String getName() {
    return name;
  }

  public ImageMediaJpaEntity setName(String name) {
    this.name = name;
    return this;
  }

  public String getFilePath() {
    return filePath;
  }

  public ImageMediaJpaEntity setFilePath(String filePath) {
    this.filePath = filePath;
    return this;
  }
}
