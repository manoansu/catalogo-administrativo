package pt.amane.application.category.update;

import io.vavr.API;
import io.vavr.control.Either;
import java.util.Objects;
import java.util.function.Supplier;
import pt.amane.domain.category.Category;
import pt.amane.domain.category.CategoryGateway;
import pt.amane.domain.category.CategoryID;
import pt.amane.domain.exception.DomainException;
import pt.amane.domain.exception.NotFoundException;
import pt.amane.domain.validation.handler.Notification;

public non-sealed class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {

  private final CategoryGateway categoryGateway;

  public DefaultUpdateCategoryUseCase(CategoryGateway categoryGateway) {
    this.categoryGateway = Objects.requireNonNull(categoryGateway);
  }

  @Override
  public Either<Notification, UpdateCategoryOutput> execute(UpdateCategoryCommand aCommand) {
    final var anId = CategoryID.from(aCommand.id());
    final var aName = aCommand.name();
    final var aDescription = aCommand.description();
    final var isActive = aCommand.isActive();

    final var aCategory = this.categoryGateway.findById(anId)
        .orElseThrow(notFound(anId));

    final var notification = Notification.create();
    aCategory
        .update(aName, aDescription, isActive)
        .validate(notification);


    return notification.hasError() ? API.Left(notification) : update(aCategory);
  }

  private Either<Notification, UpdateCategoryOutput> update(final Category aCategory) {
    return API.Try(() -> this.categoryGateway.update(aCategory))
        .toEither()
        .bimap(Notification::create, UpdateCategoryOutput::form);
  }

  private Supplier<DomainException> notFound(final CategoryID anId) {
    return () -> NotFoundException.with(Category.class, anId);
  }
}
