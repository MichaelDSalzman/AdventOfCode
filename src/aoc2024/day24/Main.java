package aoc2024.day24;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
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
            Map<String, Boolean> values = solve(lines);
            Pattern zPattern = Pattern.compile("z\\d+");
            List<String> zKeys= values.keySet().stream().filter(k -> zPattern.matcher(k).find()).sorted().toList();
            String bitString = zKeys.stream()
                .map(k -> values.get(k) ? "1" : "0")
                .reduce("", (a, b) -> a+b);
            bitString = StringUtils.reverse(bitString);
            return Long.parseLong(bitString, 2);
        }

        public Object calculateP2(List<String> lines) {
            findErrors(lines);
            return "NO ERRORS";
        }

        public Map<String, Boolean> solve(List<String> lines) {
            Map<String, Boolean> values = new HashMap<>();
            List<String> rules = new ArrayList<>();
            Pattern setupPattern = Pattern.compile("(.+):\\s+([01])");
            Pattern rulePattern = Pattern.compile("(\\S+)\\s+(AND|OR|XOR)\\s+(\\S+)\\s+->\\s+(\\S+)");
            for(String line : lines) {
                Matcher m = setupPattern.matcher(line);
                if (m.find()) {
                    values.put(m.group(1), m.group(2).equals("1"));
                }

                m = rulePattern.matcher(line);
                if (m.find()) {
                    rules.add(line);
                }
            }

            boolean stopSolving = false;
            while(!stopSolving) {
                for(String rule : rules) {
                    Matcher m = rulePattern.matcher(rule);
                    if(m.find()) {
                        String input1 = m.group(1);
                        String instruction = m.group(2);
                        String input2 = m.group(3);
                        String output = m.group(4);
                        values.putIfAbsent(input1, null);
                        values.putIfAbsent(input2, null);
                        values.putIfAbsent(output, null);

                        if(values.get(input1) == null || values.get(input2) == null) {
                            continue;
                        }
                        switch (instruction) {
                            case "AND" -> values.put(output, values.get(input1) && values.get(input2));
                            case "OR" ->  values.put(output, values.get(input1) || values.get(input2));
                            case "XOR" ->  values.put(output, values.get(input1) ^ values.get(input2));
                        }
                    }
                }

                // stop solving if all the values that start with z are populated
                stopSolving = values.keySet().stream()
                    .filter(k -> k.startsWith("z"))
                    .allMatch(k -> values.get(k) != null);
            }

            return values;
        }

        public void findErrors(List<String> lines) {

            List<Rule> rules = new ArrayList<>();
            Pattern rulePattern = Pattern.compile("(\\S+)\\s+(AND|OR|XOR)\\s+(\\S+)\\s+->\\s+(\\S+)");
            for(String line : lines) {
                Matcher m = rulePattern.matcher(line);
                if (m.find()) {
                    rules.add(new Rule(m.group(1), m.group(3), Operation.valueOf(m.group(2)), m.group(4)));
                }
            }

            int lastZIndex = rules.stream()
                .filter(r -> r.output.startsWith("z"))
                .map(r-> Integer.parseInt(r.output.substring(1)))
                .mapToInt(Integer::intValue)
                .max()
                .orElseThrow();

            verifyHalfAdder(rules, 0);

            for(int i=1; i<lastZIndex; i++) {
                verifyFullAdder(rules, i);
            }
        }

        public void verifyHalfAdder(List<Rule> rules, int index) {
            String xIn = "x" + (index<10 ? "0" : "") + index;
            String yIn = "y" + (index<10 ? "0" : "") + index;
            Rule inToXor = findRule(rules, Set.of(xIn, yIn), Operation.XOR);

            if(!inToXor.output.equals("z" + (index<10 ? "0" : "") + index)) {
                throw new RuntimeException("ERROR IN HALF ADDER");
            }
        }

        // FIXME FOUND SOLUTION BY HAND BY USING THESE EXCEPTIONS TO FIND WHERE THE ERRORS WERE
        //  THEN LOOKING AT THE INPUT AND MANUALLY FIGURING OUT WHAT NEEDED TO SWAP
        //  THIS MAKES A MASSIVE ASSUMPTION THAT THE SWAPS ONLY HAPPEN WITHIN THE FULL ADDER
        //  IF A SWAP WERE TO HAPPEN BETWEEN ADDERS, THIS WOULD NOT CATCH IT
        public void verifyFullAdder(Collection<Rule> rules, int index) {

            String xIn = "x" + (index<10 ? "0" : "") + index;
            String yIn = "y" + (index<10 ? "0" : "") + index;
            String zOut = "z" + (index<10 ? "0" : "") + index;

            // THE XOR THAT IS FED BY THE INPUTS MUST NOT OUTPUT TO Z
            Rule inToXor = findRule(rules, Set.of(xIn, yIn), Operation.XOR);
            if(inToXor.output.startsWith("z")) {
                throw new RuntimeException("XOR WITH INS OUTPUTS TO Z: " + inToXor);
            }

            // THE RULE THAT OUTPUTS TO Z MUST BE AN XOR
            Rule zOutRule = rules.stream().filter(r->r.output.equals(zOut)).findFirst().orElseThrow();
            if(!zOutRule.operation.equals(Operation.XOR)) {
                throw new RuntimeException("Z OUT RULE IS NOT AN XOR: " + zOutRule);
            }

            // THE XOR THAT OUTPUTS Z MUST BE FED BY THE OUTPUT FROM THE XOR THAT WAS FED BY INPUTS
            if(!zOutRule.input1.equals(inToXor.output) && !zOutRule.input2.equals(inToXor.output)) {
                throw new RuntimeException("XOR THAT OUTPUTS TO Z WAS NOT FED FROM XOR FED BY INS: " + zOutRule);
            }
        }

        public Rule findRule(Collection<Rule> rules, Set<String> inputs, Operation operation) {
            return rules.stream()
                .filter(r -> inputs.contains(r.input1)
                    && inputs.contains(r.input2)
                    && r.operation.equals(operation))
                .findFirst()
                .orElseThrow(()-> new RuntimeException("No rule found with inputs: " +inputs  + " and operation " + operation));
        }

        public enum Operation {AND,OR,XOR}
        public record Rule(String input1, String input2, Operation operation, String output) {}
    }
}
