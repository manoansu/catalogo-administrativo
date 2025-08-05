package pt.amane.infrastructure.genre.presenters;

import pt.amane.application.genre.retrieve.get.GenreOutput;
import pt.amane.application.genre.retrieve.list.GenreListOutput;
import pt.amane.infrastructure.genre.models.GenreListResponse;
import pt.amane.infrastructure.genre.models.GenreResponse;

public interface GenreApiPresenter {

    static GenreResponse present(final GenreOutput output) {
        return new GenreResponse(
                output.id(),
                output.name(),
                output.isActive(),
                output.categories(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    static GenreListResponse present(final GenreListOutput output) {
        return new GenreListResponse(
                output.id(),
                output.name(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}
