package aoc2018.day02;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2018";
        String day = "02";

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

        public Object calculateP1(List<String> lines) {
            int num2 = 0;
            int num3 = 0;
            for(String line : lines) {
                Map<Character, Integer> charMap = new HashMap<>();
                for(Character c : line.toCharArray()) {
                    Integer value = charMap.getOrDefault(c, 0);
                    charMap.put(c, ++value);
                }

                if(charMap.values().stream().anyMatch(v -> v == 2)) {
                    num2++;
                }
                if(charMap.values().stream().anyMatch(v -> v == 3)) {
                    num3++;
                }
            }
            return num2 * num3;
        }

        public Object calculateP2(List<String> lines) {

            for(int i=0; i<lines.size(); i++) {
                String line1 = lines.get(i);

                for(int j=i+1; j<lines.size(); j++) {
                    String line2 = lines.get(j);

                    int numDifferences = 0;
                    for(int c=0; c<line1.length(); c++) {
                        if(line1.charAt(c) != line2.charAt(c)) {
                            numDifferences++;
                            if(numDifferences > 1) {
                                break;
                            }
                        }
                    }

                    if(numDifferences == 1) {
                        StringBuilder commonLetters = new StringBuilder();
                        for(int c=0; c<line1.length(); c++) {
                            if(line1.charAt(c) == line2.charAt(c)) {
                                commonLetters.append(line1.charAt(c));
                            }
                        }

                        return commonLetters;
                    }
                }
            }
            return null;
        }
    }
}
