package com.app.inventory.helpers;

import java.util.Base64;

public class Base64Converter {

  public static String Encode(String value) {
    return Base64.getEncoder().encodeToString(value.getBytes());
  }

  public static String Decode(String value) {
    byte[] bytes = Base64.getDecoder().decode(value);
    String decode = new String(bytes);
    return decode;
  }

}
