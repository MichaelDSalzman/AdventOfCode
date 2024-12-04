package aoc2015.day17;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2015";
        String day = "17";

        // FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/sample.txt");
        FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/input.txt");
        List<String> lines = fileReader.getLines();

        Problem problem = new Problem();
        long start = System.currentTimeMillis();
        System.out.println("P1 : " + problem.calculateP1(lines));
        System.out.println("P1 took " + (System.currentTimeMillis() - start) + " millis");
        start = System.currentTimeMillis();
        System.out.println("P2 : " + problem.calculateP2(lines));
        System.out.println("P2 took " + (System.currentTimeMillis() - start) + " millis");
    }

    public static class Problem {

        public int calculateP1(List<String> lines) {

            List<Integer> buckets = new ArrayList<>();
            for (String line : lines) {
                buckets.add(Integer.parseInt(line));
            }

            // int desiredSize = 25;
            int desiredSize = 150;

            int count =0;
            int N = (int) Math.pow(2D, buckets.size());
            for(int i = 1; i< N; i++) {
                String code = Integer.toBinaryString(i | N).substring(1);

                int combinationTotal = 0;
                for(int j=0; j<buckets.size(); j++) {
                    if (code.charAt(j) == '1') {
                        combinationTotal += buckets.get(j);
                    }
                }
                if(combinationTotal == desiredSize) {
                    count++;
                }
            }

            return count;
        }

        public int calculateP2(List<String> lines) {
            List<Integer> buckets = new ArrayList<>();
            for (String line : lines) {
                buckets.add(Integer.parseInt(line));
            }

            // int desiredSize = 25;
            int desiredSize = 150;

            int count =0;
            int containerCount = buckets.size();

            int N = (int) Math.pow(2D, buckets.size());
            for(int i = 1; i< N; i++) {
                String code = Integer.toBinaryString(i | N).substring(1);

                int combinationTotal = 0;
                int numContainersForThisCombo = 0;
                for(int j=0; j<buckets.size(); j++) {
                    if (code.charAt(j) == '1') {
                        numContainersForThisCombo++;
                        combinationTotal += buckets.get(j);
                    }
                }
                if(combinationTotal == desiredSize && numContainersForThisCombo <= containerCount) {
                    if(containerCount == numContainersForThisCombo) {
                        count++;
                    } else if (numContainersForThisCombo < containerCount) {
                        containerCount = numContainersForThisCombo;
                        count = 1;
                    }
                }
            }

            return count;
        }

    }
}
