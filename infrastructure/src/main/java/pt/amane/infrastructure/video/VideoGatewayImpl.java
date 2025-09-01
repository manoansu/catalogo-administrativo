package pt.amane.infrastructure.video;

import static pt.amane.domain.utils.CollectionUtils.nullIfEmpty;
import static pt.amane.domain.utils.CollectionUtils.mapTo;

import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pt.amane.Identifier;
import pt.amane.domain.pagination.Pagination;
import pt.amane.domain.validation.ObjectsValidator;
import pt.amane.domain.video.Video;
import pt.amane.domain.video.VideoGateway;
import pt.amane.domain.video.VideoID;
import pt.amane.domain.video.VideoPreview;
import pt.amane.domain.video.VideoSearchQuery;
import pt.amane.infrastructure.utils.SqlUtils;
import pt.amane.infrastructure.video.presistence.VideoJpaEntity;
import pt.amane.infrastructure.video.presistence.VideoRepository;

@Component
public class VideoGatewayImpl implements VideoGateway {

  private final VideoRepository videoRepository;

  public VideoGatewayImpl(VideoRepository repository) {
    this.videoRepository = (VideoRepository) ObjectsValidator.objectValidation(repository);
  }

  @Override
  @Transactional
  public Video create(Video aVideo) {
    return save(aVideo);
  }

  @Override
  public void deleteById(VideoID anId) {
    final var aVideoId = anId.getValue();
    if (this.videoRepository.existsById(aVideoId)) {
      this.videoRepository.deleteById(aVideoId);
    }

  }

  @Override
  public Optional<Video> findById(VideoID anId) {
    return this.videoRepository.findById(anId.getValue())
        .map(VideoJpaEntity::toAggregate);
  }

  @Override
  @Transactional
  public Video update(Video aVideo) {
    return save(aVideo);
  }

  @Override
  public Pagination<VideoPreview> findAll(final VideoSearchQuery aQuery) {
    final var page = PageRequest.of(
        aQuery.page(),
        aQuery.perPage(),
        Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
    );

    final var actualPage = this.videoRepository.findAll(
        SqlUtils.like(SqlUtils.upper(aQuery.terms())),
        nullIfEmpty(mapTo(aQuery.castMemberId(), Identifier::getValue)),
        nullIfEmpty(mapTo(aQuery.categoryId(), Identifier::getValue)),
        nullIfEmpty(mapTo(aQuery.genreId(), Identifier::getValue)),
        page
    );

    return new Pagination<>(
        actualPage.getNumber(),
        actualPage.getSize(),
        actualPage.getTotalElements(),
        actualPage.toList()
    );
  }

  private Video save(Video aVideo) {
    return videoRepository.save(VideoJpaEntity.from(aVideo)).toAggregate();
  }
}
