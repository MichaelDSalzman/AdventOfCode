package aoc2016.day16;

import java.io.IOException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2016";
        String day = "16";

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
        public static final String INPUT = "00111101111101000";
        // public static final String INPUT = "10000";

        public String calculateP1(List<String> lines) {
            return runCalculation(INPUT, 272);
        }

        public String calculateP2(List<String> lines) {
            return runCalculation(INPUT, 35651584);
        }

        private String runCalculation(String input, int diskLength) {
            while(input.length() < diskLength) {
                input = runIteration(input);
            }

            input = input.substring(0, diskLength);
            return getChecksum(input);
        }

        private String runIteration(String a) {
            String b = a;
            b = StringUtils.reverse(b);

            b = b.replaceAll("0", "T");
            b = b.replaceAll("1", "0");
            b = b.replaceAll("T", "1");

            return a + "0" + b;
        }

        private String getChecksum(String input) {
            StringBuilder checksumBuilder = new StringBuilder();
            for(int i=0; i<input.length(); i+=2) {
                if(input.charAt(i) == input.charAt(i+1)) {
                    checksumBuilder.append("1");
                } else {
                    checksumBuilder.append("0");
                }
            }

            if(checksumBuilder.length() % 2 == 0) {
                return getChecksum(checksumBuilder.toString());
            } else {
                return checksumBuilder.toString();
            }
        }
    }
}
