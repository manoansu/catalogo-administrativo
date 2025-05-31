package pt.amane.domain.genre;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pt.amane.AggregateRoot;
import pt.amane.domain.category.CategoryID;
import pt.amane.domain.exception.NotificationException;
import pt.amane.domain.utils.InstantUtils;
import pt.amane.domain.validation.ValidationHandler;
import pt.amane.domain.validation.handler.Notification;

public class Genre extends AggregateRoot<GenreID> {

  private String name;
  private boolean active;
  private List<CategoryID> categories;
  private Instant createdAt;
  private Instant updatedAt;
  private Instant deletedAt;

  protected Genre(
      final GenreID genreID,
      final String name,
      final boolean active,
      final List<CategoryID> categories,
      final Instant createdAt,
      final Instant updatedAt,
      final Instant deletedAt) {
    super(genreID);
    this.name = name;
    this.active = active;
    this.categories = new ArrayList<>(categories);
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.deletedAt = deletedAt;
    setValidate();
  }

  public static Genre newGenre(final String aName, final boolean isActive) {
    final var genreID = GenreID.unique();
    final var now = InstantUtils.now();
    final var deletedAt = isActive ? null : now;
    return new Genre(genreID, aName, isActive, List.of(), now, now, deletedAt);
  }

  public static Genre with(
      final GenreID genreID,
      final String name,
      final boolean active,
      final List<CategoryID> categories,
      final Instant createdAt,
      final Instant updatedAt,
      final Instant deletedAt
  ) {
    return new Genre(genreID, name, active, categories, createdAt, updatedAt, deletedAt);
  }

  public static Genre with(final Genre genre) {
    return new Genre(
        genre.id,
        genre.name,
        genre.active,
        new ArrayList<>(genre.categories),
        genre.createdAt,
        genre.updatedAt,
        genre.deletedAt
    );
  }

  @Override
  public void validate(ValidationHandler handler) {
    new GenreValidator(this, handler).validate();
  }

  public Genre update(final String aName, final boolean isActive, final List<CategoryID> categories) {
    if (isActive) {
      activate();
    }else {
      deactivate();
    }

    this.name = aName;
    this.categories = new ArrayList<>(categories != null ? categories : Collections.emptyList());
    this.updatedAt = InstantUtils.now();
    setValidate();
    return this;
  }

  public void deactivate() {
    if (getDeletedAt() == null) {
      this.deletedAt = InstantUtils.now();
    }

    this.active = false;
    this.updatedAt = InstantUtils.now();
  }

  public void activate() {
    this.deletedAt = null;
    this.active = true;
    this.updatedAt = InstantUtils.now();
  }

  public String getName() {
    return name;
  }

  public boolean isActive() {
    return active;
  }

  public List<CategoryID> getCategories() {
    return Collections.unmodifiableList(categories);
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

  public void setValidate() {
    final var notification = Notification.create();
    validate(notification);

    if (notification.hasErrors()) {
      throw new NotificationException("Failed to create a Aggregate Genre ", notification);
    }
  }

  /**
   * Adding an CategoryID associated a genre.
   */
  public Genre addCategory(final CategoryID aCategoryID) {
    if (aCategoryID == null) {
      return this;
    }
    this.categories.add(aCategoryID);
    this.updatedAt = InstantUtils.now();
    return this;
  }

  /**
   * Adding list of CategoryID associated a genre.
   */
  public Genre addCategories(final List<CategoryID> categories) {
    if (categories == null || categories.isEmpty()) {
      return this;
    }
    this.categories.addAll(categories);
    this.updatedAt = InstantUtils.now();
    return this;
  }

  /**
   * Remove a CategoryId associated a genre.
   */
  public Genre removeCategory(final CategoryID aCategoryID) {
    if (aCategoryID == null) {
      return this;
    }
    this.categories.remove(aCategoryID);
    this.updatedAt = InstantUtils.now();
    return this;
  }

}
