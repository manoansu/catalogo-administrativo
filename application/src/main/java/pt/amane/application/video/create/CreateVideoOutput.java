package pt.amane.application.video.create;

import pt.amane.domain.video.Video;

public record CreateVideoOutput (String id) {

  public static CreateVideoOutput from(final Video video) {
    return new CreateVideoOutput(video.getId().getValue());
  }
}
