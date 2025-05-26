package pt.amane.domain.validation;

public class ObjectsValidator {

  private ObjectsValidator() {}

    public static String objectValidation(final String anId) {
    if (anId == null) {
      throw new NullPointerException("ID cannot be null");
    }
    if (anId.trim().isEmpty()) {
      throw new NullPointerException("ID cannot be empty or blank");
    }
    return anId;
  }

  public static Object objectValidation(final Object anId) {
    if (anId == null) {
      throw new NullPointerException("ID cannot be null");
    }
    return anId;
  }

}
