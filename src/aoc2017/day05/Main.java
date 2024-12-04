package aoc2017.day05;

import java.io.IOException;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2017";
        String day = "05";

        // FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/sample.txt");
        FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/input.txt");

        Problem problem = new Problem();
        long start = System.currentTimeMillis();
        System.out.println ("P1 : " + problem.calculateP1(fileReader.getLines()));
        System.out.println("P1 took " + (System.currentTimeMillis() - start) + " millis");
        start = System.currentTimeMillis();
        System.out.println ("P2 : " + problem.calculateP2(fileReader.getLines()));
        System.out.println("P2 took " + (System.currentTimeMillis() - start) + " millis");
    }

    public static class Problem {

        public int calculateP1(List<String> lines) {
            return processInstructions(lines, false);
        }

        public int calculateP2(List<String> lines) {

            return processInstructions(lines, true);
        }

        private int processInstructions(List<String> lines, boolean advanced) {
            int lineNum = 0;
            int steps = 0;

            while(lineNum >= 0 && lineNum < lines.size()) {
                steps++;
                String line = lines.get(lineNum);
                Integer value = Integer.parseInt(line);
                if(!advanced) {
                    lines.set(lineNum, String.valueOf(value + 1));
                } else {
                    if(value >= 3) {
                        lines.set(lineNum, String.valueOf(value - 1));
                    } else {
                        lines.set(lineNum, String.valueOf(value + 1));
                    }
                }

                lineNum += value;
            }

            return steps;
        }
    }
}
