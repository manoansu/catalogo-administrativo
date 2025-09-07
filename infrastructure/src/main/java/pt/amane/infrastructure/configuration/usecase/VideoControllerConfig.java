package pt.amane.infrastructure.configuration.usecase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.amane.application.video.create.CreateVideoUseCase;
import pt.amane.application.video.delete.DeleteVideoUseCase;
import pt.amane.application.video.media.get.GetMediaUseCase;
import pt.amane.application.video.media.upload.UploadMediaUseCase;
import pt.amane.application.video.retrieve.get.GetVideoByIdUseCase;
import pt.amane.application.video.retrieve.list.ListVideosUseCase;
import pt.amane.application.video.update.UpdateVideoUseCase;
import pt.amane.infrastructure.api.controllers.VideoController;

@Configuration
public class VideoControllerConfig {

  private final CreateVideoUseCase createVideoUseCase;
  private final GetVideoByIdUseCase getVideoByIdUseCase;
  private final UpdateVideoUseCase updateVideoUseCase;
  private final DeleteVideoUseCase deleteVideoUseCase;
  private final ListVideosUseCase listVideosUseCase;
  private final GetMediaUseCase getMediaUseCase;
  private final UploadMediaUseCase uploadMediaUseCase;

  public VideoControllerConfig(
      final CreateVideoUseCase createVideoUseCase,
      final GetVideoByIdUseCase getVideoByIdUseCase,
      final UpdateVideoUseCase updateVideoUseCase,
      final DeleteVideoUseCase deleteVideoUseCase,
      final ListVideosUseCase listVideosUseCase,
      final GetMediaUseCase getMediaUseCase,
      final UploadMediaUseCase uploadMediaUseCase
  ) {
    this.createVideoUseCase = createVideoUseCase;
    this.getVideoByIdUseCase = getVideoByIdUseCase;
    this.updateVideoUseCase = updateVideoUseCase;
    this.deleteVideoUseCase = deleteVideoUseCase;
    this.listVideosUseCase = listVideosUseCase;
    this.getMediaUseCase = getMediaUseCase;
    this.uploadMediaUseCase = uploadMediaUseCase;
  }


  @Bean
  public VideoController videoController() {
    return new VideoController(
        createVideoUseCase,
        getVideoByIdUseCase,
        updateVideoUseCase,
        deleteVideoUseCase,
        listVideosUseCase,
        getMediaUseCase,
        uploadMediaUseCase
    );
  }
}