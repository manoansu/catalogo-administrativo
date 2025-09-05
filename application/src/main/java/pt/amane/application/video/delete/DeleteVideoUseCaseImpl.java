package pt.amane.application.video.delete;

import pt.amane.domain.validation.ObjectsValidator;
import pt.amane.domain.video.MediaResourceGateway;
import pt.amane.domain.video.VideoGateway;
import pt.amane.domain.video.VideoID;

public class DeleteVideoUseCaseImpl extends DeleteVideoUseCase {

  private final VideoGateway videoGateway;
  private final MediaResourceGateway mediaResourceGateway;

  public DeleteVideoUseCaseImpl(
      final VideoGateway videoGateway,
      final MediaResourceGateway mediaResourceGateway
  ) {
    this.videoGateway = (VideoGateway) ObjectsValidator.objectValidation(videoGateway);
    this.mediaResourceGateway = (MediaResourceGateway) ObjectsValidator.objectValidation(mediaResourceGateway);
  }

  @Override
  public void execute(final String anIn) {
    final var aVideoId = VideoID.from(anIn);
    this.videoGateway.deleteById(aVideoId);
    this.mediaResourceGateway.clearResources(aVideoId);
  }
}
