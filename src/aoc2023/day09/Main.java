package aoc2023.day09;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2023";
        String day = "09";

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

            int total = 0;

            for(String line : lines) {
                String[] numberStrings = line.split(" ");

                List<Integer> sequence = new ArrayList<>();
                for(String string : numberStrings) {
                    sequence.add(Integer.parseInt(string));
                }
                List<List<Integer>> numberSequences = new ArrayList<>();
                numberSequences.add(sequence);

                boolean foundZeroSequence = false;
                while(!foundZeroSequence) {
                    List<Integer> currentSequence = numberSequences.get(numberSequences.size()-1);
                    List<Integer> nextSequence = new ArrayList<>();

                    for(int i=1; i<currentSequence.size(); i++) {
                        int difference = currentSequence.get(i) - currentSequence.get(i-1);
                        nextSequence.add(difference);
                    }
                    numberSequences.add(nextSequence);

                    if(nextSequence.stream().allMatch(n -> n == 0)) {
                        foundZeroSequence = true;
                    }
                }

                for(int i=numberSequences.size() - 2; i>=0; i--) {
                    List<Integer> currentSequence = numberSequences.get(i);
                    List<Integer> previousSequence = numberSequences.get(i+1);
                    int lastDigitInCurrentSequence = currentSequence.get(currentSequence.size()-1);
                    int lastDigitInPreviousSequence = previousSequence.get(previousSequence.size()-1);
                    int sum = lastDigitInPreviousSequence + lastDigitInCurrentSequence;
                    currentSequence.add(sum);
                }

                List<Integer> firstSequence = numberSequences.get(0);
                int value = firstSequence.get(firstSequence.size()-1);
                total += value;
            }
            return total;
        }

        public int calculateP2(List<String> lines) {

            int total = 0;

            for(String line : lines) {
                String[] numberStrings = line.split(" ");

                List<Integer> sequence = new ArrayList<>();
                for(String string : numberStrings) {
                    sequence.add(Integer.parseInt(string));
                }
                List<List<Integer>> numberSequences = new ArrayList<>();
                numberSequences.add(sequence);

                boolean foundZeroSequence = false;
                while(!foundZeroSequence) {
                    List<Integer> currentSequence = numberSequences.get(numberSequences.size()-1);
                    List<Integer> nextSequence = new ArrayList<>();

                    for(int i=1; i<currentSequence.size(); i++) {
                        int difference = currentSequence.get(i) - currentSequence.get(i-1);
                        nextSequence.add(difference);
                    }
                    numberSequences.add(nextSequence);

                    if(nextSequence.stream().allMatch(n -> n == 0)) {
                        foundZeroSequence = true;
                    }
                }

                for(int i=numberSequences.size() - 2; i>=0; i--) {
                    List<Integer> currentSequence = numberSequences.get(i);
                    List<Integer> previousSequence = numberSequences.get(i+1);
                    int firstDigitInCurrentSequence = currentSequence.get(0);
                    int firstDigitInPreviousSequence = previousSequence.get(0);
                    int nextDigit = firstDigitInCurrentSequence - firstDigitInPreviousSequence;
                    currentSequence.add(0, nextDigit);
                }

                List<Integer> firstSequence = numberSequences.get(0);
                int value = firstSequence.get(0);
                total += value;
            }
            return total;
        }
    }
}
