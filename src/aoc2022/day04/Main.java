package aoc2022.day04;

import java.io.IOException;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String day = "04";

        // util.FileReader fileReader = new util.FileReader("src/aoc2022/day" + day + "/sample.txt");
        FileReader fileReader = new FileReader("src/aoc2022/day" + day + "/input.txt");

        List<String> lines = fileReader.getLines();

        Problem problem = new Problem();
        System.out.println ("P1 : " + problem.calculateP1(lines));
        System.out.println ("P2 : " + problem.calculateP2(lines));
    }

    public static class Problem {

        public int calculateP1(List<String> lines) {
            int total = 0;

            for (String line : lines) {
                String[] pairs = line.split(",");

                if(pairsOverlapCompletely(pairs[0], pairs[1])) {
                    total++;
                }
            }

            return total;
        }

        private boolean pairsOverlapCompletely(String pair1, String pair2) {
            String[] pair1Values = pair1.split("-");
            String[] pair2Values = pair2.split("-");

            int pair1Start = Integer.parseInt(pair1Values[0]);
            int pair1End = Integer.parseInt(pair1Values[1]);
            int pair2Start = Integer.parseInt(pair2Values[0]);
            int pair2End = Integer.parseInt(pair2Values[1]);

            if(pair1Start >= pair2Start && pair1End <= pair2End) {
                return true;
            }

            return pair2Start >= pair1Start && pair2End <= pair1End;
        }

        private boolean pairsOverlapPartially(String pair1, String pair2) {
            String[] pair1Values = pair1.split("-");
            String[] pair2Values = pair2.split("-");

            int pair1Start = Integer.parseInt(pair1Values[0]);
            int pair1End = Integer.parseInt(pair1Values[1]);
            int pair2Start = Integer.parseInt(pair2Values[0]);
            int pair2End = Integer.parseInt(pair2Values[1]);

            if(pair1Start >= pair2Start && pair1Start <= pair2End) {
                return true;
            }

            return pair2Start >= pair1Start && pair2Start <= pair1End;
        }

        public int calculateP2(List<String> lines) {
            int total = 0;

            for (String line : lines) {
                String[] pairs = line.split(",");

                if(pairsOverlapPartially(pairs[0], pairs[1])) {
                    total++;
                }
            }

            return total;
        }
    }
}
