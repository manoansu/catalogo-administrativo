package pt.amane.infrastructure.video.presistence;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.util.Objects;
import pt.amane.domain.castmember.CastMemberID;

@Entity(name = "VideoCastMember")
@Table(name = "videos_cast_members")
public class VideoCastMemberJpaEntity {

  @EmbeddedId
  private VideoCastMemberID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("videoId")
  private VideoJpaEntity video;

  public VideoCastMemberJpaEntity() {}

  private VideoCastMemberJpaEntity(VideoCastMemberID id, VideoJpaEntity video) {
    this.id = id;
    this.video = video;
  }

  public static VideoCastMemberJpaEntity from(final VideoJpaEntity entity, final CastMemberID castMemberID) {
    return new VideoCastMemberJpaEntity(VideoCastMemberID.from(entity.getId(), castMemberID.getValue()), entity);
  }

  public VideoCastMemberID getId() {
    return id;
  }

  public void setId(VideoCastMemberID id) {
    this.id = id;
  }

  public VideoJpaEntity getVideo() {
    return video;
  }

  public void setVideo(VideoJpaEntity video) {
    this.video = video;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    VideoCastMemberJpaEntity that = (VideoCastMemberJpaEntity) o;
    return Objects.equals(getId(), that.getId()) && Objects.equals(getVideo(), that.getVideo());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getVideo());
  }
}
