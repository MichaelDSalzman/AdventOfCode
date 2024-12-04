package aoc2017.day06;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2017";
        String day = "06";

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

            return findLoop(Arrays.stream(lines.get(0).split("\\s+")).mapToInt(Integer::parseInt).toArray());
        }

        public int calculateP2(List<String> lines) {
            return -1;
        }

        private int findLoop(int[] banks) {
            int steps = 0;
            Map<String, Integer> seenStates = new HashMap<>();
            String newState = StringUtils.joinWith(",", Arrays.stream(banks).mapToObj(String::valueOf).toArray());

            while(true) {
                seenStates.put(newState, steps);
                steps++;

                int index = 0;
                for(int i=1; i<banks.length; i++) {
                    if(banks[i] > banks[index]) {
                        index = i;
                    }
                }

                int toDistribute = banks[index];
                int i = 0;
                banks[index] = 0;
                while(toDistribute > 0) {
                    i++;
                    banks[(index+i)%banks.length]++;
                    toDistribute--;
                }

                newState = StringUtils.joinWith(",", Arrays.stream(banks).mapToObj(String::valueOf).toArray());
                if(seenStates.containsKey(newState)) {
                    System.out.println("Current step: " + steps + " Last saw this state on step: " + seenStates.get(newState) + " Difference: " + (steps - seenStates.get(newState)));
                    return steps;
                }
            }
        }
    }
}
