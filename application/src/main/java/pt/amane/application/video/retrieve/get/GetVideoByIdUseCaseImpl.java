package pt.amane.application.video.retrieve.get;

import pt.amane.domain.exception.NotFoundException;
import pt.amane.domain.validation.ObjectsValidator;
import pt.amane.domain.video.Video;
import pt.amane.domain.video.VideoGateway;
import pt.amane.domain.video.VideoID;

public class GetVideoByIdUseCaseImpl extends GetVideoByIdUseCase {

  private final VideoGateway videoGateway;

  public GetVideoByIdUseCaseImpl(final VideoGateway videoGateway) {
    this.videoGateway = (VideoGateway) ObjectsValidator.objectValidation(videoGateway);
  }

  @Override
  public VideoOutput execute(String anIn) {
    final var aVideoId = VideoID.from(anIn);
    return this.videoGateway.findById(aVideoId)
        .map(VideoOutput::from)
        .orElseThrow(() -> NotFoundException.with(Video.class, aVideoId));
  }
}
