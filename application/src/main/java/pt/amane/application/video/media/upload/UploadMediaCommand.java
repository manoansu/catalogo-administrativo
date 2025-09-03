package pt.amane.application.video.media.upload;

import pt.amane.domain.video.VideoResource;

public record UploadMediaCommand(
    String videoId,
    VideoResource videoResource
) {

  public static UploadMediaCommand with(final String anId, final VideoResource aResource) {
    return new UploadMediaCommand(anId, aResource);
  }
}
