package aoc2016.day06;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2016";
        String day = "06";

        // FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/sample.txt");
        FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/input.txt");
        List<String> lines = fileReader.getLines();

        Problem problem = new Problem();
        long start = System.currentTimeMillis();
        System.out.println ("P1 : " + problem.calculateP1(lines));
        System.out.println("P1 took " + (System.currentTimeMillis() - start) + " millis");
        start = System.currentTimeMillis();
        System.out.println ("P2 : " + problem.calculateP2(lines));
        System.out.println("P2 took " + (System.currentTimeMillis() - start) + " millis");
    }

    public static class Problem {

        public String calculateP1(List<String> lines) {
            StringBuilder message = new StringBuilder();

            for(int i=0; i< lines.get(0).length(); i++) {
                int mostCharQuantity = 0;
                char mostChar = ' ';
                Map<Character, Integer> charQuantityMap = new HashMap<>();

                for(String line: lines) {
                    char c = line.charAt(i);
                    Integer quantity = charQuantityMap.get(c);

                    if(quantity == null) {
                        quantity = 0;
                    }
                    quantity++;
                    charQuantityMap.put(c, quantity);
                    if(quantity > mostCharQuantity) {
                        mostChar = c;
                        mostCharQuantity = quantity;
                    }
                }

                message.append(mostChar);
            }
            return message.toString();
        }

        public String calculateP2(List<String> lines) {
            StringBuilder message = new StringBuilder();

            for(int i=0; i< lines.get(0).length(); i++) {
                Map<Character, Integer> charQuantityMap = new HashMap<>();

                for(String line: lines) {
                    char c = line.charAt(i);
                    Integer quantity = charQuantityMap.get(c);

                    if (quantity == null) {
                        quantity = 0;
                    }
                    quantity++;
                    charQuantityMap.put(c, quantity);
                }

                int leastCharQuantity = Integer.MAX_VALUE;
                char leastChar = ' ';
                for(Character key : charQuantityMap.keySet()) {
                    Integer quantity = charQuantityMap.get(key);
                    if(quantity < leastCharQuantity) {
                        leastChar = key;
                        leastCharQuantity = quantity;
                    }
                }
                message.append(leastChar);

            }
            return message.toString();
        }
    }
}
