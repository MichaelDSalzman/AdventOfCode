package aoc2023.day01;

import java.util.List;

public class Problem1 {

    public String calculateP1(List<String> lines) {

        int total = 0;

        for (String line : lines) {
            String firstDigit = null;
            String lastDigit = null;

            for (int i = 0; i < line.length(); i++) {

                char character = line.charAt(i);
                if (character >= '0' && character <= '9') {

                    if (firstDigit == null) {
                        firstDigit = String.valueOf(character);
                    }

                    lastDigit = String.valueOf(character);
                }
            }

            String combinedDigits = firstDigit + lastDigit;
            int num = Integer.parseInt(combinedDigits);
            total += num;
        }

        return "TOTAL " + total;
    }

    public String calculateP2(List<String> lines) {

        int total = 0;

        for (String line : lines) {
            String firstDigit = null;
            String lastDigit = null;

            String translatedline = translateNumberStrings(line);

            for (int i = 0; i < translatedline.length(); i++) {

                char character = translatedline.charAt(i);
                if (character >= '0' && character <= '9') {

                    if (firstDigit == null) {
                        firstDigit = String.valueOf(character);
                    }

                    lastDigit = String.valueOf(character);
                }
            }


            System.out.println(line + " : " + translatedline + " : " + firstDigit + " : " + lastDigit);
            String combinedDigits = firstDigit + lastDigit;
            int num = Integer.parseInt(combinedDigits);
            total += num;
        }

        return "TOTAL " + total;
    }

    private String translateNumberStrings(String line) {
        String translatedString = "";

        for (int i = 0; i < line.length(); i++) {
            String sub = line.substring(i);

            if (sub.startsWith("one")) {
                translatedString += "1";
            } else if (sub.startsWith("two")) {

                translatedString += "2";
            } else if (sub.startsWith("three")) {

                translatedString += "3";

            } else if (sub.startsWith("four")) {

                translatedString += "4";
            } else if (sub.startsWith("five")) {

                translatedString += "5";
            } else if (sub.startsWith("six")) {

                translatedString += "6";
            } else if (sub.startsWith("seven")) {

                translatedString += "7";
            } else if (sub.startsWith("eight")) {

                translatedString += "8";
            } else if (sub.startsWith("nine")) {

                translatedString += "9";
            }

            translatedString += sub.charAt(0);
        }

        return translatedString;
    }
}
