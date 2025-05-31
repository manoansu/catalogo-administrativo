package pt.amane.domain.genre;

import java.util.Objects;
import pt.amane.Identifier;
import pt.amane.domain.validation.ObjectsValidator;
import pt.amane.domain.utils.IdUtils;

public class GenreID extends Identifier {

  private final String value;

  public GenreID(String value) {
    this.value = ObjectsValidator.objectValidation(value);
  }

  public static GenreID from(final String anId) {
    return new GenreID(anId);
  }

  public static GenreID unique() {
    return GenreID.from(IdUtils.uuid());
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }final GenreID genreID = (GenreID) o;
    return Objects.equals(getValue(), genreID.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getValue());
  }
}
