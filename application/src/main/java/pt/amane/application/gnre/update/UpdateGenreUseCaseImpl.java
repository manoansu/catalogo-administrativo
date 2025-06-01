package pt.amane.application.gnre.update;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import pt.amane.domain.category.CategoryGateway;
import pt.amane.domain.category.CategoryID;
import pt.amane.domain.exception.DomainException;
import pt.amane.domain.exception.NotFoundException;
import pt.amane.domain.exception.NotificationException;
import pt.amane.domain.genre.Genre;
import pt.amane.domain.genre.GenreGateway;
import pt.amane.domain.genre.GenreID;
import pt.amane.domain.validation.Error;
import pt.amane.domain.validation.ObjectsValidator;
import pt.amane.domain.validation.ValidationHandler;
import pt.amane.domain.validation.handler.Notification;

public non-sealed class UpdateGenreUseCaseImpl extends UpdateGenreUseCase{

  private final CategoryGateway categoryGateway;
  private final GenreGateway genreGateway;

  public UpdateGenreUseCaseImpl(
      final CategoryGateway categoryGateway,
      final GenreGateway genreGateway
  ) {
    this.categoryGateway = (CategoryGateway) ObjectsValidator.objectValidation(categoryGateway);
    this.genreGateway = (GenreGateway) ObjectsValidator.objectValidation(genreGateway);
  }

  @Override
  public UpdateGenreOutput execute(final UpdateGenreCommand aCommand) {
    final var anId = GenreID.from(aCommand.id());
    final var aName = aCommand.name();
    final var isActive = aCommand.isActive();
    final var categories = toCategoryId(aCommand.categories());

    final var aGenre = this.genreGateway.findById(anId)
        .orElseThrow(notFound(anId));

    final var notification = Notification.create();
    notification.append(validateCategories(categories));
    notification.validate(() -> aGenre.update(aName, isActive, categories));

    if (notification.hasErrors()) {
      throw new NotificationException(
          "Could not update Aggregate Genre %s".formatted(aCommand.id()), notification
      );
    }

    return UpdateGenreOutput.from(this.genreGateway.update(aGenre));
  }

  private ValidationHandler validateCategories(final List<CategoryID> ids) {
    final var notification = Notification.create();
    if (ids == null || ids.isEmpty()) {
      return notification;
    }

    final var retrievedIds = categoryGateway.existsByIds(ids);

    if (ids.size() != retrievedIds.size()) {
      final var missingIds = new ArrayList<>(ids);
      missingIds.removeAll(retrievedIds);

      final var missingIdsMessage = missingIds.stream()
          .map(CategoryID::getValue)
          .collect(Collectors.joining(", "));

      notification.append(
          new Error("Some categories could not be found: %s".formatted(missingIdsMessage)));
    }

    return notification;

  }

  private Supplier<DomainException> notFound(GenreID anIn) {
    return () -> NotFoundException.with(Genre.class, anIn);
  }

  private List<CategoryID> toCategoryId(List<String> categories) {
    if (categories != null || !categories.isEmpty()){
      return categories.stream()
          .map(CategoryID::from)
          .toList();
    }
    return List.of();
  }
}
