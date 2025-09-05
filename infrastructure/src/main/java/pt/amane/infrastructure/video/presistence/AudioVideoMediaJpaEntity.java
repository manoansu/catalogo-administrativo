package pt.amane.infrastructure.video.presistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import pt.amane.domain.video.AudioVideoMedia;
import pt.amane.domain.video.MediaStatus;

@Entity(name = "AudioVideoMedia")
@Table(name = "videos_video_media")
public class AudioVideoMediaJpaEntity {

  @Id
  private String id;

  @Column(name = "checksum", nullable = false)
  private String checksum;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "file_path", nullable = false)
  private String filePath;

  @Column(name = "encode_path", nullable = false)
  private String encodePath;

  @Column(name = "media_status", nullable = false)
  @Enumerated(EnumType.STRING)
  private MediaStatus status;

  public AudioVideoMediaJpaEntity() {
  }


  public AudioVideoMediaJpaEntity(
      final String id,
      final String checksum,
      final String name,
      final String filePath,
      final String encode,
      final MediaStatus status
  ) {
    this.id = id;
    this.checksum = checksum;
    this.name = name;
    this.filePath = filePath;
    this.encodePath = encode;
    this.status = status;
  }

  public static AudioVideoMediaJpaEntity from(final AudioVideoMedia entity) {
    return new AudioVideoMediaJpaEntity(
        entity.id(),
        entity.checksum(),
        entity.name(),
        entity.rawLocation(),
        entity.encodedLocation(),
        entity.status()
    );
  }

  public AudioVideoMedia toDomain() {
    return AudioVideoMedia.with(
        getId(),
        getChecksum(),
        getName(),
        getFilePath(),
        getEncodePath(),
        getStatus()
    );
  }

  public String getId() {
    return id;
  }

  public String getChecksum() {
    return checksum;
  }

  public String getName() {
    return name;
  }

  public String getFilePath() {
    return filePath;
  }

  public String getEncodePath() {
    return encodePath;
  }

  public MediaStatus getStatus() {
    return status;
  }

}
