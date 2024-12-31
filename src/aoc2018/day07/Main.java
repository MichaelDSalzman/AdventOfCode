package aoc2018.day07;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
            return getOrder(lines);
        }

        public Object calculateP2(List<String> lines) {
            return howLong(lines);
        }

        private String getOrder(List<String> lines) {
            Map<String, Set<String>> stepsAndDependencies = new HashMap<>();

            for(String line : lines) {
                String[] parts = line.split("\\s+");
                String stepBeingCompleted = parts[7];
                String dependentStep = parts[1];

                Set<String> dependentSteps = stepsAndDependencies.getOrDefault(stepBeingCompleted, new HashSet<>());
                dependentSteps.add(dependentStep);
                stepsAndDependencies.put(stepBeingCompleted, dependentSteps);

                // Add an entry for the dependent step as well if it does not exist
                stepsAndDependencies.put(dependentStep, stepsAndDependencies.getOrDefault(dependentStep, new HashSet<>()));
            }

            StringBuilder stepOrder = new StringBuilder();
            while(!stepsAndDependencies.keySet().isEmpty()) {
                String stepWithNoDependencies = stepsAndDependencies.keySet().stream()
                    .filter(s -> stepsAndDependencies.get(s).isEmpty())
                    .sorted()
                    .toList()
                    .get(0);

                stepOrder.append(stepWithNoDependencies);

                stepsAndDependencies.remove(stepWithNoDependencies);
                stepsAndDependencies.values().forEach(c -> c.remove(stepWithNoDependencies));
            }

            return stepOrder.toString();
        }

        private long howLong(List<String> lines) {
            Map<String, Set<String>> stepsAndDependencies = new HashMap<>();

            for(String line : lines) {
                String[] parts = line.split("\\s+");
                String stepBeingCompleted = parts[7];
                String dependentStep = parts[1];

                Set<String> dependentSteps = stepsAndDependencies.getOrDefault(stepBeingCompleted, new HashSet<>());
                dependentSteps.add(dependentStep);
                stepsAndDependencies.put(stepBeingCompleted, dependentSteps);

                // Add an entry for the dependent step as well if it does not exist
                stepsAndDependencies.put(dependentStep, stepsAndDependencies.getOrDefault(dependentStep, new HashSet<>()));
            }

            int secondCount = 0;
            Map<String, Integer> stepsAndCompleteTimes = new HashMap<>();

            while(!stepsAndDependencies.isEmpty()) {
                //Find all steps that can be done
                List<String> stepsWithNoDependencies = new ArrayList<>(stepsAndDependencies.keySet().stream()
                    .filter(s -> stepsAndDependencies.get(s).isEmpty())
                    .filter(s -> !stepsAndCompleteTimes.containsKey(s))
                    .sorted()
                    .toList());

                // Hand out steps to any available elves
                while(stepsAndCompleteTimes.keySet().size() < 5 && !stepsWithNoDependencies.isEmpty()) {
                    String step = stepsWithNoDependencies.remove(0);
                    stepsAndCompleteTimes.put(step, secondCount + 60 + (step.charAt(0) - 'A' + 1));
                }

                // determine when next iteration will be ready
                secondCount = stepsAndCompleteTimes.values().stream().mapToInt(Integer::intValue).min().orElseThrow();
                int finalSecondCount = secondCount;

                // Step was completed, remove it from everywhere
                String stepCompleted = stepsAndCompleteTimes.keySet().stream().filter(k -> stepsAndCompleteTimes.get(k) == finalSecondCount).findFirst().orElseThrow();
                stepsAndCompleteTimes.remove(stepCompleted);
                stepsAndDependencies.remove(stepCompleted);
                stepsAndDependencies.values().forEach(c -> c.remove(stepCompleted));
            }

            return secondCount;
        }
    }
}
