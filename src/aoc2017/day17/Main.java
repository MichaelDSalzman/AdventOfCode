package aoc2017.day17;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2017";
        String day = "17";

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
            // int size = 10;
            // int stepCount = 3;
            int size = 2018;
            int stepCount = 329;
            List<Integer> spinlock = generateSpinlock(stepCount, size);
            return spinlock.get((spinlock.indexOf(size-1) + 1) % size);
        }

        public int calculateP2(List<String> lines) {

            int size = 50000000;
            int stepCount = 329;
            // return whatIsDigitAfter0(3, 10);
            return whatIsDigitAfter0(stepCount, size);
        }

        private List<Integer> generateSpinlock(int stepCount, int size) {
            List<Integer> spinlock = new ArrayList<>();
            spinlock.add(0);
            int index = 0;

            while(spinlock.size() < size) {
                index = (index + stepCount) % spinlock.size();
                spinlock.add(index + 1, spinlock.size());
                index++;
            }

            return spinlock;
        }

        private int whatIsDigitAfter0(int stepCount, int size) {
            int index = 0;
            int valueAt1 = 0;

            for(int i=1; i<size; i++) {
                index = (index + stepCount) % i;
                if(index+1 == 1) {
                    valueAt1 = i;
                }
                index++;
            }

            return valueAt1;
        }
    }
}
