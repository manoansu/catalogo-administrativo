package pt.amane.domain.castmember;

import java.util.Objects;
import pt.amane.Identifier;
import pt.amane.domain.utils.IdUtils;
import pt.amane.domain.validation.ObjectsValidator;

public class CastMemberID extends Identifier {

  private final String value;

  public CastMemberID(String value) {
    this.value = ObjectsValidator.objectValidation(value);
  }

  public static CastMemberID from(String value) {
    return new CastMemberID(value);
  }

  public static CastMemberID unique() {
    return new CastMemberID(IdUtils.uuid());
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
    CastMemberID that = (CastMemberID) o;
    return Objects.equals(getValue(), that.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getValue());
  }
}
