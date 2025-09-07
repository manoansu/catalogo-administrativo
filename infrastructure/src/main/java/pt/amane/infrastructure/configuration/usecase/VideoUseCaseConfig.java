package pt.amane.infrastructure.configuration.usecase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
import pt.amane.application.video.create.CreateVideoUseCase;
import pt.amane.application.video.create.CreateVideoUseCaseImpl;
import pt.amane.application.video.delete.DeleteVideoUseCase;
import pt.amane.application.video.delete.DeleteVideoUseCaseImpl;
import pt.amane.application.video.media.get.GetMediaUseCase;
import pt.amane.application.video.media.get.GetMediaUseCaseImpl;
import pt.amane.application.video.media.update.UpdateMediaStatusUseCase;
import pt.amane.application.video.media.update.UpdateMediaStatusUseCaseImpl;
import pt.amane.application.video.media.upload.UploadMediaUseCase;
import pt.amane.application.video.media.upload.UploadMediaUseCaseImpl;
import pt.amane.application.video.retrieve.get.GetVideoByIdUseCase;
import pt.amane.application.video.retrieve.get.GetVideoByIdUseCaseImpl;
import pt.amane.application.video.retrieve.list.ListVideosUseCase;
import pt.amane.application.video.retrieve.list.ListVideosUseCaseImpl;
import pt.amane.application.video.update.UpdateVideoUseCase;
import pt.amane.application.video.update.UpdateVideoUseCaseImpl;
import pt.amane.domain.castmember.CastMemberGateway;
import pt.amane.domain.category.CategoryGateway;
import pt.amane.domain.genre.GenreGateway;
import pt.amane.domain.video.MediaResourceGateway;
import pt.amane.domain.video.VideoGateway;

@Configuration
public class VideoUseCaseConfig {

    private final CategoryGateway categoryGateway;
    private final CastMemberGateway castMemberGateway;
    private final GenreGateway genreGateway;
    private final MediaResourceGateway mediaResourceGateway;
    private final VideoGateway videoGateway;

    public VideoUseCaseConfig(
        final CategoryGateway categoryGateway,
        final CastMemberGateway castMemberGateway,
        final GenreGateway genreGateway,
        final MediaResourceGateway mediaResourceGateway,
        final VideoGateway videoGateway
    ) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Bean
    public CreateVideoUseCase createVideoUseCase() {
        return new CreateVideoUseCaseImpl(categoryGateway, castMemberGateway, genreGateway, mediaResourceGateway, videoGateway);
    }

    @Bean
    public UpdateVideoUseCase updateVideoUseCase() {
        return new UpdateVideoUseCaseImpl(videoGateway, categoryGateway, castMemberGateway, genreGateway, mediaResourceGateway);
    }

    @Bean
    public GetVideoByIdUseCase getVideoByIdUseCase() {
        return new GetVideoByIdUseCaseImpl(videoGateway);
    }

    @Bean
    public DeleteVideoUseCase deleteVideoUseCase() {
        return new DeleteVideoUseCaseImpl(videoGateway, mediaResourceGateway);
    }

    @Bean
    public ListVideosUseCase listVideosUseCase() {
        return new ListVideosUseCaseImpl(videoGateway);
    }

    @Bean
    public GetMediaUseCase getMediaUseCase() {
        return new GetMediaUseCaseImpl(mediaResourceGateway);
    }

    @Bean
    public UploadMediaUseCase uploadMediaUseCase() {
        return new UploadMediaUseCaseImpl(mediaResourceGateway, videoGateway);
    }

    @Bean
    public UpdateMediaStatusUseCase updateMediaStatusUseCase() {
        return new UpdateMediaStatusUseCaseImpl(videoGateway);
//        return new UpdateMediaStatusUseCase(videoGateway);
    }
}
