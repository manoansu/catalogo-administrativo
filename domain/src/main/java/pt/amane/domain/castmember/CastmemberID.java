package pt.amane.domain.castmember;

import java.util.Objects;
import pt.amane.Identifier;
import pt.amane.domain.utils.IdUtils;
import pt.amane.domain.validation.ObjectsValidator;

public class CastmemberID extends Identifier {

  private final String value;

  public CastmemberID(String value) {
    this.value = ObjectsValidator.objectValidation(value);
  }

  public static CastmemberID from(String value) {
    return new CastmemberID(value);
  }

  public static CastmemberID unique() {
    return new CastmemberID(IdUtils.uuid());
  }

  @Override
  public String getValue() {
    return this.value;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CastmemberID that = (CastmemberID) o;
    return Objects.equals(getValue(), that.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getValue());
  }
}
