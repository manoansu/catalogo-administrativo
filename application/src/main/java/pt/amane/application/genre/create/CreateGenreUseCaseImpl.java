package pt.amane.application.genre.create;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import pt.amane.domain.category.CategoryGateway;
import pt.amane.domain.category.CategoryID;
import pt.amane.domain.exception.NotificationException;
import pt.amane.domain.genre.Genre;
import pt.amane.domain.genre.GenreGateway;
import pt.amane.domain.validation.Error;
import pt.amane.domain.validation.ObjectsValidator;
import pt.amane.domain.validation.ValidationHandler;
import pt.amane.domain.validation.handler.Notification;

public non-sealed class CreateGenreUseCaseImpl extends CreateGenreUseCase {

  private final CategoryGateway categoryGateway;
  private final GenreGateway genreGateway;

  public CreateGenreUseCaseImpl(final CategoryGateway categoryGateway, final GenreGateway genreGateway) {
    this.categoryGateway = (CategoryGateway) ObjectsValidator.objectValidation(categoryGateway);
    this.genreGateway = (GenreGateway) ObjectsValidator.objectValidation(genreGateway);
  }

  @Override
  public CreateGenreOutput execute(final CreateGenreCommand aCommand) {
    final var aName = aCommand.name();
    final var isActive = aCommand.isActive();
    final var categories = toCategoryID(aCommand.categories());

    final var notification = Notification.create();
    notification.append(validateCategories(categories));

    final var aGenre = notification.validate(() -> Genre.newGenre(aName, isActive));

    if (notification.hasErrors()) {
      throw new NotificationException("Could not create Aggregate Genre", notification);
    }

    aGenre.addCategories(categories);

    return CreateGenreOutput.from(this.genreGateway.create(aGenre));
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

    notification.append(new Error("Some categories could not be found: %s".formatted(missingIdsMessage)));
  }

  return notification;
}

  private List<CategoryID> toCategoryID(List<String> categories) {
    return categories.stream().map(CategoryID::from).toList();
  }
}
