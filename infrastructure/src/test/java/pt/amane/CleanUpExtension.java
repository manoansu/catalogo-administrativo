package pt.amane;

import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pt.amane.infrastructure.castmember.persistence.CastMemberRepository;
import pt.amane.infrastructure.category.persistence.CategoryRepository;
import pt.amane.infrastructure.genre.persistence.GenreRepository;
import pt.amane.infrastructure.video.presistence.VideoRepository;

public class CleanUpExtension implements BeforeEachCallback {

  @Override
  public void beforeEach(final ExtensionContext context) {
    final var appContext = SpringExtension.getApplicationContext(context);

    cleanUp(List.of(
        appContext.getBean(VideoRepository.class),
        appContext.getBean(CastMemberRepository.class),
        appContext.getBean(GenreRepository.class),
        appContext.getBean(CategoryRepository.class)
    ));
  }

  private void cleanUp(final Collection<CrudRepository> repositories) {
    repositories.forEach(CrudRepository::deleteAll);
  }
}
