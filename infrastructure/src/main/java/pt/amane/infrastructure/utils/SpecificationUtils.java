package pt.amane.infrastructure.utils;

import org.springframework.data.jpa.domain.Specification;

/**
 *
 * Final class where nobody can not extend this class.
 */
public final class SpecificationUtils {

  // Private constructor for nobody instantiates this class
  private SpecificationUtils(){}

  public static <T> Specification<T> like(final String prop, final String term) {
    return ((root, query, criteriaBuilder) ->
        criteriaBuilder.like(criteriaBuilder.upper(root.get(prop)), like(term.toUpperCase())));
  }

  private static String like(String term) {
    return "%" + term + "%";
  }
}
