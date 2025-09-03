package pt.amane.application.video.media.upload;

import pt.amane.domain.exception.NotFoundException;
import pt.amane.domain.validation.ObjectsValidator;
import pt.amane.domain.video.MediaResourceGateway;
import pt.amane.domain.video.Video;
import pt.amane.domain.video.VideoGateway;
import pt.amane.domain.video.VideoID;

public class UploadMediaUseCaseImpl extends UploadMediaUseCase{

  private final MediaResourceGateway mediaResourceGateway;
  private final VideoGateway videoGateway;

  public UploadMediaUseCaseImpl(
      final MediaResourceGateway mediaResourceGateway,
      final VideoGateway videoGateway
  ) {
    this.mediaResourceGateway = (MediaResourceGateway) ObjectsValidator.objectValidation(mediaResourceGateway);
    this.videoGateway = (VideoGateway) ObjectsValidator.objectValidation(videoGateway);
  }

  @Override
  public UploadMediaOutput execute(final UploadMediaCommand aCmd) {
    final var anId = VideoID.from(aCmd.videoId());
    final var aResource = aCmd.videoResource();

    final var aVideo = this.videoGateway.findById(anId)
        .orElseThrow(() -> notFound(anId));

    switch (aResource.type()) {
      case VIDEO -> aVideo.updateVideoMedia(mediaResourceGateway.storeAudioVideo(anId, aResource));
      case TRAILER -> aVideo.updateTrailerMedia(mediaResourceGateway.storeAudioVideo(anId, aResource));
      case BANNER -> aVideo.updateBannerMedia(mediaResourceGateway.storeImage(anId, aResource));
      case THUMBNAIL -> aVideo.updateThumbnailMedia(mediaResourceGateway.storeImage(anId, aResource));
      case THUMBNAIL_HALF -> aVideo.updateThumbnailHalfMedia(mediaResourceGateway.storeImage(anId, aResource));
    }

    return UploadMediaOutput.with(videoGateway.update(aVideo), aResource.type());
  }

  private NotFoundException notFound(final VideoID anId) {
    return NotFoundException.with(Video.class, anId);
  }
}