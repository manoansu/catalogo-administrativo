package pt.amane.infrastructure.video.presenters;


import pt.amane.application.video.media.upload.UploadMediaOutput;
import pt.amane.application.video.retrieve.get.VideoOutput;
import pt.amane.application.video.retrieve.list.VideoListOutput;
import pt.amane.application.video.update.UpdateVideoOutput;
import pt.amane.domain.pagination.Pagination;
import pt.amane.domain.video.AudioVideoMedia;
import pt.amane.domain.video.ImageMedia;
import pt.amane.infrastructure.video.model.AudioVideoMediaResponse;
import pt.amane.infrastructure.video.model.ImageMediaResponse;
import pt.amane.infrastructure.video.model.UpdateVideoResponse;
import pt.amane.infrastructure.video.model.UploadMediaResponse;
import pt.amane.infrastructure.video.model.VideoListResponse;
import pt.amane.infrastructure.video.model.VideoResponse;

public interface VideoApiPresenter {

    static VideoResponse present(final VideoOutput output) {
        return new VideoResponse(
                output.id(),
                output.title(),
                output.description(),
                output.launchedAt(),
                output.duration(),
                output.opened(),
                output.published(),
                output.rating().getName(),
                output.createdAt(),
                output.updatedAt(),
                present(output.banner()),
                present(output.thumbnail()),
                present(output.thumbnailHalf()),
                present(output.video()),
                present(output.trailer()),
                output.categories(),
                output.genres(),
                output.castMembers()
        );
    }

    static AudioVideoMediaResponse present(final AudioVideoMedia media) {
        if (media == null) {
            return null;
        }
        return new AudioVideoMediaResponse(
                media.id(),
                media.checksum(),
                media.name(),
                media.rawLocation(),
                media.encodedLocation(),
                media.status().name()
        );
    }

    static ImageMediaResponse present(final ImageMedia image) {
        if (image == null) {
            return null;
        }
        return new ImageMediaResponse(
                image.id(),
                image.checksum(),
                image.name(),
                image.location()
        );
    }

    static UpdateVideoResponse present(final UpdateVideoOutput output) {
        return new UpdateVideoResponse(output.id());
    }

    static VideoListResponse present(final VideoListOutput output) {
        return new VideoListResponse(
                output.id(),
                output.title(),
                output.description(),
                output.createdAt(),
                output.updatedAt()
        );
    }

    static Pagination<VideoListResponse> present(final Pagination<VideoListOutput> page) {
        return page.map(VideoApiPresenter::present);
    }

    static UploadMediaResponse present(final UploadMediaOutput output) {
        return new UploadMediaResponse(output.videoId(), output.mediaType());
    }
}
