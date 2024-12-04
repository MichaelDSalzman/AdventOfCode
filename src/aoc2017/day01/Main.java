package aoc2017.day01;

import java.io.IOException;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2017";
        String day = "01";

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
            return calculateSum(lines.get(0), 1);
        }

        public int calculateP2(List<String> lines) {
            return calculateSum(lines.get(0), lines.get(0).length()/2);
        }

        private int calculateSum(String line, int offset) {
            int sum = 0;
            for(int i=0; i<line.length(); i++) {
                if(line.charAt(i) == line.charAt((i+offset) % line.length())) {
                    sum += Integer.parseInt(String.valueOf(line.charAt(i)));
                }
            }

            return sum;
        }
    }
}
