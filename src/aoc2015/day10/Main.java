package aoc2015.day10;

import java.io.IOException;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2015";
        String day = "10";

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

        public int calculateP1(List<String> lines) {
            String line = lines.get(0);

            for(int i=0; i<40; i++) {
                line = generateLine(line);
            }
            return line.length();
        }

        public int calculateP2(List<String> lines) {

            String line = lines.get(0);

            for(int i=0; i<50; i++) {
                line = generateLine(line);
            }
            return line.length();
        }

        private String generateLine(String line) {
            int currentCount = 0;
            String currentChar = null;
            StringBuilder output = new StringBuilder();

            for(int i=0; i<line.length(); i++) {
                String character = line.substring(i, i+1);
                if(currentChar == null) {
                    currentChar = character;
                    currentCount++;
                    continue;
                }

                if(currentChar.equals(character)) {
                    currentCount++;
                } else {
                    output.append(currentCount).append(currentChar);
                    currentCount=1;
                    currentChar=character;
                }
            }

            output.append(currentCount).append(currentChar);

            return output.toString();
        }
    }
}
