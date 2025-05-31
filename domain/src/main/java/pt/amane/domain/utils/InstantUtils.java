package pt.amane.domain.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public final class InstantUtils {

  private InstantUtils() {}

  /**
   * prevents Instant from generating up to 9th house = NANOSECONDS, but rather, 6th house = MICROSECONDS
   * On some computer it returns 6th place = MICROSECONDS
   */
  public static Instant now() {
    return Instant.now().truncatedTo(ChronoUnit.SECONDS);
  }


}
