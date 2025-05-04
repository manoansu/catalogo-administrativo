package pt.amane.domain.utils;

import java.util.UUID;

public class IdUtils {

  private IdUtils(){}

  public static String uuid() {
    return UUID.randomUUID().toString().toLowerCase().replace("-", "");
  }

}
