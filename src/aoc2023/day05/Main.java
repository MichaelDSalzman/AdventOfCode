package aoc2023.day05;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2023";
        String day = "05";

        // FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/sample.txt");
        FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/input.txt");

        List<String> lines = fileReader.getLines();

        Problem problem = new Problem();

        long start = System.currentTimeMillis();
        System.out.println ("P1 : " + problem.calculateP1(lines));
        long end = System.currentTimeMillis();
        System.out.println("P1 took " + (end-start) + " millis");

        start = System.currentTimeMillis();
        System.out.println ("P2 : " + problem.calculateP2(lines));
        end = System.currentTimeMillis();

        System.out.println("P2 took " + (end-start) + " millis");
    }

    public static class Problem {

        public Long calculateP1(List<String> lines) {

            List<Seed> seeds = new ArrayList<>();

            for(String line : lines) {
                if(line.contains("seeds")) {
                    Pattern p = Pattern.compile("\\d+");
                    Matcher m = p.matcher(line);
                    while(m.find()) {
                        Seed seed = new Seed();
                        seed.currentValue = Long.parseLong(m.group());
                        seeds.add(seed);
                    }

                    continue;
                }

                if(line.contains("map") || line.isEmpty()) {
                    seeds.forEach(s -> s.hasBeenConverted = false);
                    continue;
                }

                String[] rule = line.split("\\s+");
                Long destinationRangeStart = Long.parseLong(rule[0]);
                Long sourceRangeStart = Long.parseLong(rule[1]);
                Long rangeLength = Long.parseLong(rule[2]);

                for(Seed seed : seeds) {
                    if(!seed.hasBeenConverted && seed.currentValue >= sourceRangeStart && seed.currentValue < (sourceRangeStart + rangeLength)) {
                        seed.currentValue = (seed.currentValue - sourceRangeStart) + destinationRangeStart;
                        seed.hasBeenConverted = true;
                    }
                }
            }

            Long smallestLocation = Long.MAX_VALUE;
            for(Seed seed : seeds) {
                if(seed.currentValue < smallestLocation){
                    smallestLocation = seed.currentValue;
                }
            }
            return smallestLocation;
        }

        public Long calculateP2(List<String> lines) {

            List<List<Rule>> rulesList = new ArrayList<>();
            List<SeedRange> seedRanges = new ArrayList<>();

            for (String line : lines) {
                if(line.startsWith("seeds")) {
                    String[] split = line.split(":");
                    String[] values = split[1].trim().split("\\s+");
                    SeedRange seedRange = null;
                    for(int i=0; i<values.length; i++) {
                        if(i%2 == 0) {
                            seedRange = new SeedRange();
                            seedRange.start = Long.parseLong(values[i]);
                        } else {
                            seedRange.length = Long.parseLong(values[i]);
                            seedRanges.add(seedRange);
                        }
                    }

                    continue;
                }

                if(line.isEmpty()) {
                    continue;
                }

                if(line.contains("map")) {
                    List<Rule> rules = new ArrayList<>();
                    rulesList.add(rules);
                    continue;
                }

                String[] ruleValues = line.split("\\s+");
                Rule rule = new Rule();

                rule.destinationRangeStart = Long.parseLong(ruleValues[0]);
                rule.sourceRangeStart = Long.parseLong(ruleValues[1]);
                rule.rangeLength = Long.parseLong(ruleValues[2]);

                rulesList.get(rulesList.size()-1).add(rule);
            }

            Long startingValue = 0L;
            while(true) {

                Long currentValue = startingValue;

                for (int i = rulesList.size() - 1; i >= 0; i--) {
                    List<Rule> rules = rulesList.get(i);

                    for(Rule rule : rules) {
                        if(currentValue >= rule.destinationRangeStart && currentValue < rule.destinationRangeStart + rule.rangeLength) {
                            currentValue = (currentValue - rule.destinationRangeStart) + rule.sourceRangeStart;
                            break;
                        }
                    }
                }

                for(SeedRange seedRange : seedRanges) {
                    if(seedRange.seedFallsInRange(currentValue)) {
                        return startingValue;
                    }
                }
                startingValue++;
            }
        }
    }

    public static class Seed {
        public Long currentValue;
        public boolean hasBeenConverted = false;
    }

    public static class SeedRange {
        public Long start;
        public Long length;

        public boolean seedFallsInRange(Long value) {
            return value >= start && value < start + length;
        }
    }

    public static class Rule {
        public Long destinationRangeStart;
        public Long sourceRangeStart;
        public Long rangeLength;
    }
}
