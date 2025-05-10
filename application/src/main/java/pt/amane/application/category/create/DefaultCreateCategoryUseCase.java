package pt.amane.application.category.create;

import io.vavr.API;
import io.vavr.control.Either;
import java.util.Objects;
import pt.amane.domain.category.Category;
import pt.amane.domain.category.CategoryGateway;
import pt.amane.domain.validation.handler.Notification;

public non-sealed class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {

  private final CategoryGateway categoryGateway;

  public DefaultCreateCategoryUseCase(CategoryGateway categoryGateway) {
    this.categoryGateway = Objects.requireNonNull(categoryGateway);
  }

  @Override
  public Either<Notification, CreateCategoryOutput> execute(final CreateCategoryCommand aCommand) {
    final var aName = aCommand.name();
    final var aDescription = aCommand.description();
    final var isActive = aCommand.isActive();

    final var notification = Notification.create();

    final var aCategory = Category.newCategory(aName, aDescription, isActive);
    aCategory.validate(notification);

    return notification.hasError() ? API.Left(notification) : create(aCategory);
  }

  private Either<Notification, CreateCategoryOutput> create(final Category aCategory) {
    return API.Try(() -> this.categoryGateway.create(aCategory))
        .toEither()// convert category to Either,
        .bimap(Notification::create, CreateCategoryOutput::from);
  }

  /**
   * This method is the same with the create() above, they are different because one use functional and other non functional
   * @param aCategory
   * @return
   */
  private Either<Notification, CreateCategoryOutput> createNotification(final Category aCategory) {
    try {
      return API.Right(CreateCategoryOutput.from(this.categoryGateway.create(aCategory)));
    }catch (final Throwable t) {
      return API.Left(Notification.create());
    }
  }
}
