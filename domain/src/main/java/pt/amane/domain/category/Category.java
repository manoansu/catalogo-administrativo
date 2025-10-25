package pt.amane.domain.category;

import java.time.Instant;
import java.util.Objects;
import pt.amane.AggregateRoot;
import pt.amane.domain.validation.ValidationHandler;

public class Category extends AggregateRoot<CategoryID> {

  private String name;
  private String description;
  private boolean active;
  private Instant createdAt;
  private Instant updatedAt;
  private Instant deletedAt;

  private Category(
      final CategoryID categoryID,
      final String name,
      final String description,
      final boolean active,
      final Instant aCreationDate,
      final Instant aUpdateDate,
      final Instant aDeleteDate) {
    super(categoryID);
    this.name = name;
    this.description = description;
    this.active = active;
    this.createdAt = Objects.requireNonNull(aCreationDate, "'aCreationDate' should not be null");
    this.updatedAt = Objects.requireNonNull(aUpdateDate, "'aUpdateDate' should not be null");
    this.deletedAt = aDeleteDate;
  }

  public static Category newCategory(final String aName, final String aDescription, final boolean isActive) {
    final var anId = CategoryID.unique();
    final var now = Instant.now();
    final var aDeletedAt = isActive ? null : now;
    return new Category(anId, aName, aDescription, isActive, now, now, aDeletedAt);
  }

  public static Category with(
      final CategoryID anId,
      final String name,
      final String description,
      final boolean active,
      final Instant createdAt,
      final Instant updatedAt,
      final Instant deletedAt
  ) {
    return new Category(
        anId,
        name,
        description,
        active,
        createdAt,
        updatedAt,
        deletedAt
    );
  }

  /**
   * Clone the Category constructor
   * @param aCategory
   * @return
   */
  public static Category with(final Category aCategory) {
    return with(
        aCategory.getId(),
        aCategory.name,
        aCategory.description,
        aCategory.isActive(),
        aCategory.createdAt,
        aCategory.updatedAt,
        aCategory.deletedAt
    );
  }

  @Override
  public void validate(final ValidationHandler handler) {
    new CategoryValidator(this, handler).validate();
  }

  public Category activate() {
    this.deletedAt = null;
    this.active = true;
    this.updatedAt = Instant.now();
    return this;
  }

  public Category deactivate() {
    if (getDeletedAt() == null) {
      this.deletedAt = Instant.now();
    }
    this.active = false;
    this.updatedAt = Instant.now();
    return this;
  }

  public Category update(final String aName, final String aDescription, final boolean isActive) {
    if (isActive) {
      activate();
    } else {
      deactivate();
    }
    this.name = aName;
    this.description = aDescription;
    this.updatedAt = Instant.now();
    return this;
  }

  public CategoryID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public boolean isActive() {
    return active;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public Instant getDeletedAt() {
    return deletedAt;
  }

}
