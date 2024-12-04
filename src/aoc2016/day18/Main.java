package aoc2016.day18;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2016";
        String day = "18";

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
            return calculateSafeTiles(40, lines.get(0));
        }

        public int calculateP2(List<String> lines) {
            return calculateSafeTiles(400000, lines.get(0));
        }

        private int calculateSafeTiles(int numRows, String firstRow) {
            List<String> rows = new ArrayList<>();
            rows.add(firstRow);
            while(rows.size() < numRows) {
                StringBuilder sb = new StringBuilder();
                String lastRow = rows.get(rows.size() - 1);
                for(int i=0; i<lastRow.length(); i++) {
                    String threeAbove;
                    if(i==0){
                        threeAbove = "." + lastRow.substring(i, i+2);
                    }  else if(i==lastRow.length()-1) {
                        threeAbove = lastRow.substring(i-1, i+1) + ".";
                    } else {
                        threeAbove = lastRow.substring(i-1, i+2);
                    }
                    if(threeAbove.equals("^^.") || threeAbove.equals(".^^") || threeAbove.equals("^..") || threeAbove.equals("..^")) {
                        sb.append("^");
                    } else {
                        sb.append(".");
                    }
                }

                rows.add(sb.toString());
            }
            return rows.stream().map(s -> StringUtils.countMatches(s, ".")).reduce(0, Integer::sum);
        }

    }
}
