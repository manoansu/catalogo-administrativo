package pt.amane.application.video.media.update;

import pt.amane.domain.exception.NotFoundException;
import pt.amane.domain.validation.ObjectsValidator;
import pt.amane.domain.video.AudioVideoMedia;
import pt.amane.domain.video.MediaStatus;
import pt.amane.domain.video.Video;
import pt.amane.domain.video.VideoGateway;
import pt.amane.domain.video.VideoID;
import pt.amane.domain.video.VideoMediaType;

public class UpdateMediaStatusUseCaseImpl extends UpdateMediaStatusUseCase{

  private final VideoGateway videoGateway;

  public UpdateMediaStatusUseCaseImpl(final VideoGateway videoGateway) {
    this.videoGateway = (VideoGateway) ObjectsValidator.objectValidation(videoGateway);
  }

  @Override
  public void execute(UpdateMediaStatusCommand aCmd) {
    final var anId = VideoID.from(aCmd.videoId());
    final var aResourceId = aCmd.resourceId();
    final var folder = aCmd.folder();
    final var filename = aCmd.filename();

    final var aVideo = this.videoGateway.findById(anId)
        .orElseThrow(() -> notFound(anId));

    final var encodePath = "%s/%s".formatted(folder, filename); // concat folder with filename

    if (matches(aResourceId, aVideo.getVideo().orElse(null))) {
      updateVideo(VideoMediaType.VIDEO, aCmd.status(), aVideo, encodePath);
    } else if (matches(aResourceId, aVideo.getTrailer().orElse(null))) {
      updateVideo(VideoMediaType.TRAILER, aCmd.status(), aVideo, encodePath);
    }
  }

  private void updateVideo(final VideoMediaType aType, final MediaStatus astatus, final Video aVideo, final String encodePath) {
    switch (astatus) {
      case PENDING -> {}
      case PROCESSING -> aVideo.processing(aType);
      case COMPLETED -> aVideo.completed(aType, encodePath);
    }
    this.videoGateway.update(aVideo);
  }

  private boolean matches(String anId, AudioVideoMedia audioVideoMedia) {
    if (audioVideoMedia == null) return false;
    return anId.equals(audioVideoMedia.id());
  }

  private NotFoundException notFound(VideoID anId) {
    return NotFoundException.with(Video.class, anId);
  }
}
