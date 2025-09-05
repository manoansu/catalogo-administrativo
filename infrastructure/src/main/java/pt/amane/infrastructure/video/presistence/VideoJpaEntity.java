package pt.amane.infrastructure.video.presistence;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.Year;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import pt.amane.domain.castmember.CastMemberID;
import pt.amane.domain.category.CategoryID;
import pt.amane.domain.genre.GenreID;
import pt.amane.domain.utils.CollectionUtils;
import pt.amane.domain.video.Rating;
import pt.amane.domain.video.Video;
import pt.amane.domain.video.VideoID;

@Table(name = "videos")
@Entity(name = "Video")
public class VideoJpaEntity {

  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private String id;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "description", length = 4000)
  private String description;

  @Column(name = "year_launched", nullable = false)
  private int yearLaunched;

  @Column(name = "opened", nullable = false)
  private Boolean opened;

  @Column(name = "published", nullable = false)
  private Boolean published;

  @Column(name = "rating", nullable = false)
  @Enumerated(EnumType.STRING)
  private Rating rating;

  @Column(name = "duration", precision = 2)
  private Double duration;

  @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
  private Instant updatedAt;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "video_id")
  private AudioVideoMediaJpaEntity video;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "trailer_id")
  private AudioVideoMediaJpaEntity trailer;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "banner_id")
  private ImageMediaJpaEntity banner;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "thumbnail_id")
  private ImageMediaJpaEntity thumbnail;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "thumbnail_half_id")
  private ImageMediaJpaEntity thumbnailHalf;

  @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<VideoCategoryJpaEntity> categories;

  @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<VideoGenreJpaEntity> genres;

  @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<VideoCastMemberJpaEntity> castMembers;

  /**
   * Default constructor for Hibernate JPA.
   */
  public VideoJpaEntity() {
  }

  /**
   * For security this constructor is private.
   *
   * @param id
   * @param title
   * @param description
   * @param yearLaunched
   * @param opened
   * @param published
   * @param rating
   * @param duration
   * @param createdAt
   * @param updatedAt
   * @param video
   * @param trailer
   * @param banner
   * @param thumbnail
   * @param thumbnailHalf
   */
  private VideoJpaEntity(
      final String id,
      final String title,
      final String description,
      final int yearLaunched,
      final boolean opened,
      final boolean published,
      final Rating rating,
      final double duration,
      final Instant createdAt,
      final Instant updatedAt,
      final AudioVideoMediaJpaEntity video,
      final AudioVideoMediaJpaEntity trailer,
      final ImageMediaJpaEntity banner,
      final ImageMediaJpaEntity thumbnail,
      final ImageMediaJpaEntity thumbnailHalf
  ) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.yearLaunched = yearLaunched;
    this.opened = opened;
    this.published = published;
    this.rating = rating;
    this.duration = duration;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.video = video;
    this.trailer = trailer;
    this.banner = banner;
    this.thumbnail = thumbnail;
    this.thumbnailHalf = thumbnailHalf;
    this.categories = new HashSet<>(3);
    this.genres = new HashSet<>(3);
    this.castMembers = new HashSet<>(3);
  }

  /**
   * Fatctory method that can be call outside of class.
   * @param aVideo
   * @return
   */
  public static VideoJpaEntity from(final Video aVideo) {
    final var entity = new VideoJpaEntity(
        aVideo.getId().getValue(),
        aVideo.getTitle(),
        aVideo.getDescription(),
        aVideo.getLaunchedAt().getValue(),
        aVideo.getOpened(),
        aVideo.getPublished(),
        aVideo.getRating(),
        aVideo.getDuration(),
        aVideo.getCreatedAt(),
        aVideo.getUpdatedAt(),
        aVideo.getVideo()
            .map(AudioVideoMediaJpaEntity::from)
            .orElse(null),
        aVideo.getTrailer()
            .map(AudioVideoMediaJpaEntity::from)
            .orElse(null),
        aVideo.getBanner()
            .map(ImageMediaJpaEntity::from)
            .orElse(null),
        aVideo.getThumbnail()
            .map(ImageMediaJpaEntity::from)
            .orElse(null),
        aVideo.getThumbnailHalf()
            .map(ImageMediaJpaEntity::from)
            .orElse(null)
    );

    aVideo.getCategories()
        .forEach(entity::addCategory);

    aVideo.getGenres()
        .forEach(entity::addGenre);

    aVideo.getCastMembers()
        .forEach(entity::addCastMember);

    return entity;
  }

  private void addCastMember(final CastMemberID castMemberID) {
    this.castMembers.add(VideoCastMemberJpaEntity.from(this, castMemberID));
  }

  private void addGenre(final GenreID genreID) {
    this.genres.add(VideoGenreJpaEntity.from(this, genreID));
  }

  private void addCategory(final CategoryID categoryID) {
    this.categories.add(VideoCategoryJpaEntity.from(this, categoryID));
  }

  /**
   * Factory method convert video to agegate.
   * @return
   */
  public Video toAggregate() {
    return Video.with(
        VideoID.from(getId()),
        getTitle(),
        getDescription(),
        Year.of(getYearLaunched()),
        getDuration(),
        isOpened(),
        isPublished(),
        getRating(),
        getCreatedAt(),
        getUpdatedAt(),
        Optional.of(getBanner())
            .map(ImageMediaJpaEntity::toDomain)
            .orElse(null),
        Optional.of(getThumbnail())
            .map(ImageMediaJpaEntity::toDomain)
            .orElse(null),
        Optional.of(getThumbnailHalf())
            .map(ImageMediaJpaEntity::toDomain)
            .orElse(null),
        Optional.of(getTrailer())
            .map(AudioVideoMediaJpaEntity::toDomain)
            .orElse(null),
        Optional.of(getVideo())
            .map(AudioVideoMediaJpaEntity::toDomain)
            .orElse(null),
        null,
        null,
        null
    );
  }

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public int getYearLaunched() {
    return yearLaunched;
  }

  public Boolean isOpened() {
    return opened;
  }
  public Boolean isPublished() {
    return published;
  }

  public Rating getRating() {
    return rating;
  }

  public double getDuration() {
    return duration;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public AudioVideoMediaJpaEntity getVideo() {
    return video;
  }

  public AudioVideoMediaJpaEntity getTrailer() {
    return trailer;
  }

  public ImageMediaJpaEntity getBanner() {
    return banner;
  }

  public ImageMediaJpaEntity getThumbnail() {
    return thumbnail;
  }

  public ImageMediaJpaEntity getThumbnailHalf() {
    return thumbnailHalf;
  }


  public Set<VideoCastMemberJpaEntity> getCastMembers() {
    return castMembers;
  }

  public Set<VideoCategoryJpaEntity> getCategories() {
    return categories;
  }

  public Set<VideoGenreJpaEntity> getGenres() {
    return genres;
  }

  public VideoJpaEntity setCastMembers(Set<VideoCastMemberJpaEntity> castMembers) {
    this.castMembers = castMembers;
    return this;
  }

  public Set<CategoryID> getCategoriesID() {
    return CollectionUtils.mapTo(getCategories(), it -> CategoryID.from(it.getId().getCategoryId()));
  }

  public Set<GenreID> getGenresID() {
    return CollectionUtils.mapTo(getGenres(), it -> GenreID.from(it.getId().getGenreId()));
  }

  public Set<CastMemberID> getCastMembersID() {
    return CollectionUtils.mapTo(getCastMembers(), it -> CastMemberID.from(it.getId().getCastMemberId()));
  }
}
