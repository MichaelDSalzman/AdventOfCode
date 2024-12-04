package aoc2016.day03;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2016";
        String day = "03";

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
            int validCount = 0;
            for(String line: lines) {
                List<Integer> ints = Arrays.stream(line.trim().split("\\s+")).map(Integer::parseInt).sorted().toList();
                if(ints.get(0) + ints.get(1) > ints.get(2)) {
                    validCount++;
                }
            }
            return validCount;
        }

        public int calculateP2(List<String> lines) {
            int validCount = 0;

            for(int i=0; i<lines.size(); i+=3) {
                for(int j=0; j<3; j++) {
                    List<Integer> triangle = new ArrayList<>();
                    triangle.add(Integer.parseInt(lines.get(i).trim().split("\\s+")[j]));
                    triangle.add(Integer.parseInt(lines.get(i+1).trim().split("\\s+")[j]));
                    triangle.add(Integer.parseInt(lines.get(i+2).trim().split("\\s+")[j]));
                    if(triangle.contains(368)) {
                        boolean a = true;
                    }
                    Collections.sort(triangle);
                    if(triangle.get(0) + triangle.get(1) > triangle.get(2)) {
                        validCount++;
                    }
                }
            }
            return validCount;
        }
    }
}
