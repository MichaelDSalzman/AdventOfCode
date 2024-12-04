package aoc2015.day20;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2015";
        String day = "20";

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
            int i = 1;
            while(true) {
                i++;
                int numPresents = findFactors(i).stream().reduce(Integer::sum).get() * 10;
                if ( numPresents >= 33100000) {
                    return i;
                }
            }
        }

        public int calculateP2(List<String> lines) {
            int i = 1;
            while(true) {
                i++;
                int numPresents = findFactorsButLimit(i).stream().reduce(Integer::sum).get() * 11;
                if ( numPresents >= 33100000) {
                    return i;
                }
            }
        }

        private Set<Integer> findFactors(int num) {
            Set<Integer> factors = new HashSet<>();
            for(int i=1; i*i<=num; i++) {
                if(num%i == 0) {
                    factors.add(i);
                    factors.add(num/i);
                }
            }

            return factors;
        }

        private Set<Integer> findFactorsButLimit(int num) {
            Set<Integer> factors = new HashSet<>();
            for(int i=1; i*i<=num; i++) {
                if(num%i == 0) {
                    if(i*50 >= num) {
                        factors.add(i);
                    } if(num/i * 50 >= num) {
                        factors.add(num / i);
                    }
                }
            }

            return factors;
        }
    }
}
