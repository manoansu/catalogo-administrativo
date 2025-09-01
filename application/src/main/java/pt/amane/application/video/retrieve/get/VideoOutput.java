package pt.amane.application.video.retrieve.get;

import java.time.Instant;
import java.util.Set;
import pt.amane.Identifier;
import pt.amane.domain.utils.CollectionUtils;
import pt.amane.domain.video.AudioVideoMedia;
import pt.amane.domain.video.ImageMedia;
import pt.amane.domain.video.Rating;
import pt.amane.domain.video.Video;

public record VideoOutput(
    String id,
    Instant createdAt,
    Instant updatedAt,
    String title,
    String description,
    Integer launchedAt,
    Double duration,
    Boolean opened,
    Boolean published,
    Rating rating,
    Set<String> categories,
    Set<String> genres,
    Set<String> castMembers,
    ImageMedia banner,
    ImageMedia thumbnail,
    ImageMedia thumbnailHalf,
    AudioVideoMedia trailer,
    AudioVideoMedia video

    ) {

  public static VideoOutput from(final Video aVideo) {
    return new VideoOutput(
        aVideo.getId().getValue(),
        aVideo.getCreatedAt(),
        aVideo.getUpdatedAt(),
        aVideo.getTitle(),
        aVideo.getDescription(),
        aVideo.getLaunchedAt().getValue(),
        aVideo.getDuration(),
        aVideo.getOpened(),
        aVideo.getPublished(),
        aVideo.getRating(),
        CollectionUtils.mapTo(aVideo.getCategories(), Identifier::getValue),
        CollectionUtils.mapTo(aVideo.getGenres(), Identifier::getValue),
        CollectionUtils.mapTo(aVideo.getCastMembers(), Identifier::getValue),
        aVideo.getBanner().orElse(null),
        aVideo.getThumbnail().orElse(null),
        aVideo.getThumbnailHalf().orElse(null),
        aVideo.getTrailer().orElse(null),
        aVideo.getVideo().orElse(null)
    );
  }

}
