package aoc2017.day15;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2017";
        String day = "15";

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

        public int calculateP1(List<String> lines) {
            // return compareGenerators(65, 1, 8921, 1, 16, 40000000);
            return compareGenerators(883, 1, 879, 1, 16, 40000000);
        }

        public int calculateP2(List<String> lines) {

            // return compareGenerators(65, 4, 8921, 8, 16, 5000000);
            return compareGenerators(883, 4, 879, 8, 16, 5000000);
        }

        private int compareGenerators(long genANum, long genAMultiple, long genBNum, long genBMultiple, int bytesToCompare, int numPairs) {
            long genAFactor = 16807;
            long genBFactor = 48271;
            long divisor = 2147483647;

            List<String> genABitsList = new ArrayList<>();
            List<String> genBBitsList = new ArrayList<>();

            while(genABitsList.size() < numPairs || genBBitsList.size() < numPairs) {
                genANum = (genANum * genAFactor) % divisor;
                genBNum = (genBNum * genBFactor) % divisor;

                if(genANum % genAMultiple == 0) {
                    genABitsList.add(StringUtils.leftPad(Long.toBinaryString(genANum), 16, '0'));
                }
                if(genBNum % genBMultiple == 0) {
                    genBBitsList.add(StringUtils.leftPad(Long.toBinaryString(genBNum), 16, '0'));
                }
            }

            int numMatching=0;
            for(int i=0; i<numPairs; i++) {

                String genABits = genABitsList.get(i);
                String genBBits = genBBitsList.get(i);

                if(genABits.substring(genABits.length()-bytesToCompare).equals(genBBits.substring(genBBits.length()-bytesToCompare))) {
                    numMatching++;
                }
            }

            return numMatching;
        }
    }
}
