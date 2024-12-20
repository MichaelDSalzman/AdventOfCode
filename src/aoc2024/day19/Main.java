package aoc2024.day19;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = null;
        String day = null;

        Pattern p = Pattern.compile("aoc(\\d+)\\.day(\\d+).*");
        Matcher m = p.matcher(Main.class.getName());
        if(m.find()) {
            year = m.group(1);
            day = m.group(2);
        }

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

        public Object calculateP1(List<String> lines) {
            // Count the number of towels that had solutions
            return findTowelCombos(lines).stream().filter(l -> l > 0).toList().size();
        }

        public Object calculateP2(List<String> lines) {
            // Count all solutions
            return findTowelCombos(lines).stream().mapToLong(Long::longValue).sum();
        }

        // Return list of towel solutions
        public List<Long> findTowelCombos(List<String> lines) {
            List<String> patterns = Arrays.stream(lines.get(0).split(",\\s*")).toList();

            List<Long> results = new ArrayList<>();
            for(int i=2; i< lines.size(); i++) {
                results.add(howManyWaysCanTowelCanBeMade(patterns, lines.get(i), new HashMap<>()));
            }

            return results;
        }
        /**
         * Given a list of patterns, a desired output, and a map of already
         * solved solutions, find the number of ways the desiredTowel can be solved
         * @param patterns
         * @param desiredTowel
         * @param solutions
         * @return
         */
        public long howManyWaysCanTowelCanBeMade(List<String> patterns,
                                                 String desiredTowel,
                                                 Map<String, Long> solutions) {

            // If the desiredTowel has already been solved, return the answer
            if(solutions.containsKey(desiredTowel)) {
                return solutions.get(desiredTowel);
            }

            // Find all patterns that matter (filter out the ones that don't appear in the desired output)
            List<String> filteredPatterns = patterns.stream()
                .filter(desiredTowel::contains)
                .toList();

            long count = 0;

            // Iterate over the filtered patterns
            for(String pattern : filteredPatterns) {
                // If the pattern matches the desired towel, we've found a solution
                if(desiredTowel.equals(pattern)) {
                    count++;
                }
                // else, if the towel starts with the pattern, recursively investigate the new towel scraps
                else if(desiredTowel.startsWith(pattern)) {
                    String newDesiredTowel = desiredTowel.substring(pattern.length());
                    long newCount = howManyWaysCanTowelCanBeMade(filteredPatterns, newDesiredTowel, solutions);
                    count += newCount;
                }
            }

            // we now have the solution count for the desired towel so add it to the solutions
            solutions.put(desiredTowel, count);
            return count;
        }
    }
}
