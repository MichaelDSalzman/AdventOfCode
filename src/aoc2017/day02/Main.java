package aoc2017.day02;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2017";
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

            int sum = 0;
            for(String line: lines) {
                sum += calculateCheckSum(line);
            }

            return sum;
        }

        public int calculateP2(List<String> lines) {

            int sum = 0;
            for(String line: lines) {
                int cs = calculateCheckSumByDividing(line);
                System.out.println(cs);
                sum += cs;
            }

            return sum;
        }

        private int calculateCheckSum(String line) {
            List<Integer> ints = Arrays.stream(line.split("\\s+")).map(Integer::parseInt).toList();
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            for(Integer integer : ints) {
                min = Math.min(min, integer);
                max = Math.max(max, integer);
            }

            return max - min;
        }

        private int calculateCheckSumByDividing(String line) {
            List<Integer> ints = Arrays.stream(line.split("\\s+")).map(Integer::parseInt).toList();
            for(int i=0; i<ints.size()-1; i++) {
                for(int j=i+1;j<ints.size(); j++) {
                    int int1 = ints.get(i);
                    int int2 = ints.get(j);
                    if(int1%int2 == 0){
                        return int1/int2;
                    } else if(int2%int1 == 0) {
                        return int2/int1;
                    }
                }
            }

            return -1;
        }
    }
}
