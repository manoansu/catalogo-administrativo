package pt.amane.domain.utils;

import java.util.Set;
import java.util.function.Function;

public final class CollectionUtils {

  private CollectionUtils(){}

  public static <IN, OUT> Set<OUT> mapTo(final Set<IN> list, final Function<IN, OUT> mapper) {

    if (list == null) {
      return Set.of();
    }

    return list.stream()
        .map(mapper)
        .collect(java.util.stream.Collectors.toSet());
  }

  public static <T> Set<T> nullIfEmpty(final Set<T> list) {
    if (list == null || list.isEmpty()) {
      return Set.of();
    }

    return list;
  }
}
