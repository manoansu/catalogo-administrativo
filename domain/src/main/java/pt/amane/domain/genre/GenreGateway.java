package pt.amane.domain.genre;

import java.util.List;
import java.util.Optional;
import pt.amane.domain.pagination.Pagination;
import pt.amane.domain.pagination.SearchQuery;

public interface GenreGateway {

  Genre create(Genre aGenre);

  void deleteById(GenreID anId);

  Optional<Genre> findById(GenreID anId);

  Genre update(Genre aGenre);

  Pagination<Genre> findAll(SearchQuery aQuery);

  List<GenreID> existsByIds(Iterable<GenreID> ids);

}
