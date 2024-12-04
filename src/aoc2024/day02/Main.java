package aoc2024.day02;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2024";
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

        public int calculateP1(List<String> lines) {
            int totalSafe = 0;

            for(String line : lines) {
                List<Integer> items = Arrays.stream(line.split(" ")).map(Integer::parseInt).toList();

                if(isListSafe(items)) {
                    totalSafe++;
                }
            }
            return totalSafe;
        }

        public int calculateP2(List<String> lines) {
            int totalSafe = 0;

            for(String line : lines) {
                List<Integer> items = Arrays.stream(line.split(" ")).map(Integer::parseInt).toList();

                if(isListSafe(items)) {
                    totalSafe++;
                } else {
                    int listSize = items.size();
                    for(int i=0; i<listSize; i++) {
                        items = new ArrayList<>(Arrays.stream(line.split(" ")).map(Integer::parseInt).toList());
                        items.remove(i);
                        if(isListSafe(items)) {
                            totalSafe++;
                            break;
                        }
                    }
                }
            }
            return totalSafe;
        }

        private boolean isListSafe(List<Integer> items) {
            boolean isDescending = items.get(0) > items.get(1);
            boolean isSafe = true;
            for(int i=0; i<items.size()-1 && isSafe; i++) {
                int difference = items.get(i) - items.get(i+1);
                if(!isDescending) {
                    difference *= -1;
                }
                if(difference < 1 || difference > 3) {
                    isSafe = false;
                }
            }
            return isSafe;
        }
    }
}
