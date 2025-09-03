package pt.amane.infrastructure.services;

import java.util.List;
import java.util.Optional;
import pt.amane.domain.video.Resource;

/**
 * this interface use façade pattern
 */
public interface StorageService {

  void store(String id, Resource resource);

  Optional<Resource> get(String id);

  List<String> list(String prefix);

  void deleteAll(final List<String> ids);

}
