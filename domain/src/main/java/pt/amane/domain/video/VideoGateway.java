package pt.amane.domain.video;

import java.util.Optional;
import pt.amane.domain.pagination.Pagination;

public interface VideoGateway {

  Video create(Video aVideo);

  void deleteById(VideoID anId);

  Optional<Video> findById(VideoID anId);

  Video update(Video aVideo);

  Pagination<Video> findAll(VideoSearchQuery aQuery);



}
