package aoc2015.day24;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.math3.util.Combinations;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2015";
        String day = "24";

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

        public long calculateP1(List<String> lines) {

            List<Integer> packages = new ArrayList<>();
            int total = 0;
            for(String line: lines) {
                int packageWeight = Integer.parseInt(line);
                packages.add(packageWeight);
                total += packageWeight;
            }

            int compartmentWeight = total / 3;

            int smallestPackageSize = packages.size();
            long bestQE = Long.MAX_VALUE;
            boolean found = false;

            for(int i=1; i<=lines.size(); i++) {
                Combinations combos = new Combinations(packages.size(), i);
                Iterator<int[]> iter = combos.iterator();
                while(iter.hasNext()) {
                    int[] combo = iter.next();
                    if(Arrays.stream(combo).map(packages::get).sum() == compartmentWeight) {
                        smallestPackageSize = i;
                        int[] comboValues = Arrays.stream(combo).map(packages::get).toArray();
                        bestQE = Math.min(bestQE, Arrays.stream(comboValues).asLongStream().reduce(1, (a, b) -> a*b));

                        found = true;
                    }
                }

                if(found) {
                    return bestQE;
                }
            }

            return -1;
        }

        public int calculateP2(List<String> lines) {
            return -1;
        }
    }
}
