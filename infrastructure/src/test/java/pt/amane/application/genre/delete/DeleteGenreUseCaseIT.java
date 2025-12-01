package pt.amane.application.genre.delete;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import pt.amane.IntegrationTest;
import pt.amane.domain.genre.Genre;
import pt.amane.domain.genre.GenreGateway;
import pt.amane.domain.genre.GenreID;
import pt.amane.infrastructure.genre.persistence.GenreJpaEntity;
import pt.amane.infrastructure.genre.persistence.GenreRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class DeleteGenreUseCaseIT {

    @Autowired
    private DeleteGenreUseCase useCase;

    @SpyBean
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void givenAValidGenreId_whenCallsDeleteGenre_shouldDeleteGenre() {
        // given
        final var aGenre = Genre.newGenre("Ação", true);

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        final var expectedId = aGenre.getId();

        Assertions.assertEquals(1, genreRepository.count());

        doNothing()
                .when(genreGateway).deleteById(any());

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        // when
        verify(genreGateway).deleteById(expectedId);
    }

    @Test
    void givenAnInvalidGenreId_whenCallsDeleteGenre_shouldBeOk() {
        // given
        final var aGenre = Genre.newGenre("Ação", true);

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        final var expectedId = GenreID.from("123");

        Assertions.assertEquals(1, genreRepository.count());

        doNothing()
                .when(genreGateway).deleteById(any());

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        // when
        verify(genreGateway).deleteById(expectedId);
    }
}
