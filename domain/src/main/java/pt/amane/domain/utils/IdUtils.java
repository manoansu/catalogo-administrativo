package pt.amane.domain.utils;

import java.util.UUID;

public class IdUtils {

  private IdUtils(){}

  /**
   * Generate the UUID randomly, and omit dash-
   * @return
   */
  public static String uuid() {
    return UUID.randomUUID().toString().toLowerCase().replace("-", "");
  }

}
