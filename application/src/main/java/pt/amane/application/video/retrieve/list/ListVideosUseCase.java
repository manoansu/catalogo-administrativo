package pt.amane.application.video.retrieve.list;

import pt.amane.UseCase;
import pt.amane.domain.pagination.Pagination;
import pt.amane.domain.video.VideoSearchQuery;

public abstract class ListVideosUseCase extends
    UseCase<VideoSearchQuery, Pagination<VideoListOutput>> {

}
