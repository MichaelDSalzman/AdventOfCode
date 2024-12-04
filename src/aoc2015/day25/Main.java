package aoc2015.day25;

import java.io.IOException;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2015";
        String day = "25";

        FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/sample.txt");
        // FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/input.txt");
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

        public long calculateP1(List<String> lines) {
            long code = 20151125;
            int row = 1;
            int nextRow = 2;
            int col = 1;

            System.out.printf("ROW %d COL %d CODE %d%n", row, col, code);
            while(!(row == 2978 && col == 3083)) {
                if(row == 1) {
                    row = nextRow++;
                    col = 1;
                } else {
                    row = row - 1;
                    col = col + 1;
                }

                code = (code * 252533) % 33554393;
                System.out.printf("ROW %d COL %d CODE %d%n", row, col, code);
            }

            return code;
        }

        public int calculateP2(List<String> lines) {
            return -1;
        }
    }
}
