package pt.amane.application.video.update;

import pt.amane.domain.video.Video;

public record UpdateVideoOutput(String id) {

  public static UpdateVideoOutput from(final Video aVideo) {
    return new UpdateVideoOutput(aVideo.getId().getValue());
  }
}
