package pt.amane.application.video.media.get;

import pt.amane.domain.video.Resource;

public record MediaOutput(
    byte[] content,
    String contentType,
    String name
) {
  public static MediaOutput with(final Resource aResource) {
    return new MediaOutput(
        aResource.content(),
        aResource.contentType(),
        aResource.name()
    );
  }
}
