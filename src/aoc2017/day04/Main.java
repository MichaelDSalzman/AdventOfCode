package aoc2017.day04;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2017";
        String day = "04";

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
            int numValid = 0;
            for(String line : lines) {
                if(isValid(line, true)) {
                    numValid++;
                }
            }
            return numValid;
        }

        public int calculateP2(List<String> lines) {
            int numValid = 0;
            for(String line : lines) {
                if(isValid(line, false)) {
                    numValid++;
                }
            }
            return numValid;
        }

        private boolean isValid(String line, boolean acceptAnagrams) {
            String[] split = line.split("\\s+");
            Set<String> words = new HashSet<>(List.of(split));
            boolean isValid = words.size() == split.length;

            if(acceptAnagrams) {
                return isValid;
            }

            Set<String> sortedSet = new HashSet<>();
            for(String string : words) {
                char[] chars = string.toCharArray();
                Arrays.sort(chars);
                sortedSet.add(String.valueOf(chars));
            }

            isValid = isValid && sortedSet.size() == words.size();
            return isValid;
        }
    }
}
