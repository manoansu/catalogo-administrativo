package pt.amane.application.video.retrieve.list;

import pt.amane.domain.pagination.Pagination;
import pt.amane.domain.validation.ObjectsValidator;
import pt.amane.domain.video.VideoGateway;
import pt.amane.domain.video.VideoSearchQuery;

public class ListVideosUseCaseImpl extends ListVideosUseCase{

  private final VideoGateway videoGateway;

  public ListVideosUseCaseImpl(final VideoGateway videoGateway) {
    this.videoGateway = (VideoGateway) ObjectsValidator.objectValidation(videoGateway);
  }

  @Override
  public Pagination<VideoListOutput> execute(VideoSearchQuery aQuery) {
    return this.videoGateway.findAll(aQuery)
        .map(VideoListOutput::from);
  }
}
