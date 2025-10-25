package pt.amane.infrastructure.video;

import static pt.amane.domain.utils.CollectionUtils.mapTo;
import static pt.amane.domain.utils.CollectionUtils.nullIfEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pt.amane.Identifier;
import pt.amane.domain.pagination.Pagination;
import pt.amane.domain.video.Video;
import pt.amane.domain.video.VideoGateway;
import pt.amane.domain.video.VideoID;
import pt.amane.domain.video.VideoPreview;
import pt.amane.domain.video.VideoSearchQuery;
import pt.amane.infrastructure.configuration.annotations.VideoCreatedQueue;
import pt.amane.infrastructure.services.EventService;
import pt.amane.infrastructure.utils.SpecificationUtils;
import pt.amane.infrastructure.video.persistence.VideoJpaEntity;
import pt.amane.infrastructure.video.persistence.VideoRepository;

@Component
public class VideoGatewayImpl implements VideoGateway {

  private final VideoRepository videoRepository;
  private final EventService eventService;

  public VideoGatewayImpl(
      @VideoCreatedQueue  final VideoRepository repository,
      final EventService eventService
  ) {
    this.videoRepository = Objects.requireNonNull(repository);
    this.eventService = Objects.requireNonNull(eventService);
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
  @Transactional(readOnly = true)
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
  @Transactional(readOnly = true)
  public Pagination<VideoPreview> findAll(final VideoSearchQuery aQuery) {
    final var page = PageRequest.of(
        aQuery.page(),
        aQuery.perPage(),
        Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
    );

    Specification<VideoJpaEntity> where = Specification.where(null);

    if (aQuery.terms() != null && !aQuery.terms().isBlank()) {
      where = where.and(SpecificationUtils.<VideoJpaEntity>like("title", aQuery.terms())
          .or(SpecificationUtils.like("description", aQuery.terms())));
    }

    if (aQuery.castMembers() != null && !aQuery.castMembers().isEmpty()) {
      where = where.and((root, query, cb) -> {
        final var castMemberId = root.join("castMembers").get("id").get("castMemberId");
        return castMemberId.in(mapTo(aQuery.castMembers(), Identifier::getValue));
      });
    }

    if (aQuery.categories() != null && !aQuery.categories().isEmpty()) {
      where = where.and((root, query, cb) -> {
        final var categoryId = root.join("categories").get("id").get("categoryId");
        return categoryId.in(mapTo(aQuery.categories(), Identifier::getValue));
      });
    }

    if (aQuery.genres() != null && !aQuery.genres().isEmpty()) {
      where = where.and((root, query, cb) -> {
        final var genreId = root.join("genres").get("id").get("genreId");
        return genreId.in(mapTo(aQuery.genres(), Identifier::getValue));
      });
    }

    final var actualPage = this.videoRepository.findAll(where, page);

    return new Pagination<>(
        actualPage.getNumber(),
        actualPage.getSize(),
        actualPage.getTotalElements(),
        actualPage.map(VideoJpaEntity::toPreview).toList()
    );
  }

  private Video save(Video aVideo) {
    final var result = this.videoRepository
        .save(VideoJpaEntity.from(aVideo))
        .toAggregate();

    aVideo.publishDomainEvents(this.eventService::send);

    return result;
  }
}
