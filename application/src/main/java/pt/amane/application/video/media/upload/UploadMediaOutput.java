package pt.amane.application.video.media.upload;

import pt.amane.domain.video.Video;
import pt.amane.domain.video.VideoMediaType;

public record UploadMediaOutput(
    String videoId,
    VideoMediaType mediaType
) {

  public static UploadMediaOutput with(final Video aVideo, final VideoMediaType aType) {
    return new UploadMediaOutput(aVideo.getId().getValue(), aType);
  }
}