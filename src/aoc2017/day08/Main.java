package aoc2017.day08;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2017";
        String day = "08";

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
            Map<String, Integer> registers = getRegisterValues(lines);
            return registers.values().stream().sorted().toList().get(registers.size()-1);
        }

        public int calculateP2(List<String> lines) {
            return -1;
        }

        private Map<String, Integer> getRegisterValues(List<String> lines) {
            int highestValueHeld = -1;

            Map<String, Integer> registers = new HashMap<>();
            for(String line : lines) {
                String[] split = line.split(" ");
                String register = split[0];
                String op = split[1];
                int value = Integer.parseInt(split[2]);
                String compareRegister = split[4];
                String binaryOp = split[5];
                int compareValue = Integer.parseInt(split[6]);

                int compareRegisterValue = registers.getOrDefault(compareRegister, 0);
                boolean performOp = false;
                switch(binaryOp) {
                    case "<" -> performOp = compareRegisterValue < compareValue;
                    case "<=" -> performOp = compareRegisterValue <= compareValue;
                    case ">" -> performOp = compareRegisterValue > compareValue;
                    case ">=" -> performOp = compareRegisterValue >= compareValue;
                    case "==" -> performOp = compareRegisterValue == compareValue;
                    case "!=" -> performOp = compareRegisterValue != compareValue;
                }
                if(performOp) {
                    switch(op) {
                        case "inc" -> registers.put(register, registers.getOrDefault(register, 0) + value);
                        case "dec" -> registers.put(register, registers.getOrDefault(register, 0) - value);
                    }
                }
                if(registers.size() > 0) {
                    highestValueHeld = Math.max(highestValueHeld,
                        registers.values().stream().sorted().toList().get(registers.size() - 1));
                }
            }
            highestValueHeld = Math.max(highestValueHeld, registers.values().stream().sorted().toList().get(registers.size()-1));
            System.out.println("HIGHEST VALUE HELD: " + highestValueHeld);
            return registers;
        }
    }
}
