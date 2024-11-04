package com.app.inventory.helpers;

import java.util.Base64;
import java.util.Random;

public class RandomGenerator {

    public static String getAlphaNumericString(int n, String inputString) {

        String inputStringUcase = inputString.trim().replaceAll(" ", "").replaceAll("-", "").concat("123456789");
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index = (int) (inputStringUcase.length()
                    * Math.random());
            sb.append(inputStringUcase.charAt(index));
        }

        return sb.toString();

    }

    public static Integer getRandomNumber() {

        Random rnd = new Random();
        int generateNumber = rnd.nextInt(999999);
        return generateNumber;

    }

    public static String generateBase64String(String initial) {

        byte[] valBase64 = Base64.getEncoder().encode(initial.getBytes());
        String genString = new String(valBase64);
        return genString;
    }

}
