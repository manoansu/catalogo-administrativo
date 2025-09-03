package pt.amane.application.video.media.get;

import pt.amane.domain.exception.NotFoundException;
import pt.amane.domain.validation.ObjectsValidator;
import pt.amane.domain.video.MediaResourceGateway;
import pt.amane.domain.video.VideoID;
import pt.amane.domain.video.VideoMediaType;
import pt.amane.domain.validation.Error;

public class GetMediaUseCaseImpl extends GetMediaUseCase{

  private final MediaResourceGateway mediaResourceGateway;

  public GetMediaUseCaseImpl(MediaResourceGateway mediaResourceGateway) {
    this.mediaResourceGateway = (MediaResourceGateway) ObjectsValidator.objectValidation(mediaResourceGateway);
  }

  @Override
  public MediaOutput execute(final GetMediaCommand aCmd) {
    final var anId = VideoID.from(aCmd.videoId());
    final var aType = VideoMediaType.of(aCmd.mediaType())
        .orElseThrow(() -> typeNotFound(aCmd.mediaType()));

    final var aResource =
        this.mediaResourceGateway.getResource(anId, aType)
            .orElseThrow(() -> notFound(aCmd.videoId(), aCmd.mediaType()));

    return MediaOutput.with(aResource);
  }

  private NotFoundException notFound(final String anId, final String aType) {
    return NotFoundException.with(new Error("Resource %s not found for video %s".formatted(aType, anId)));
  }

  private NotFoundException typeNotFound(final String aType) {
    return NotFoundException.with(new Error("Media type %s doesn't exists".formatted(aType)));
  }
}

