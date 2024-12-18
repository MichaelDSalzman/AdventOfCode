package aoc2024.day17;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
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
            Program program = new Program(lines);
            return program.executeProgram();
        }

        public Object calculateP2(List<String> lines) {
            Program program = new Program(lines);
            return program.findBackwards();

            // int numDigits = program.getOriginalProgramCode().size();
            // // int numDigits = 9;
            // BigInteger count = BigDecimal.valueOf(Math.pow(8, numDigits-1)).toBigInteger();
            // BigInteger upper = BigDecimal.valueOf(Math.pow(8, numDigits)).toBigInteger();
            //
            // count = BigInteger.valueOf(266926175730700L);
            // String originalProgramCode = "2,4,1,3,7,5,0,3,4,1,1,5,5,5,3,0";
            // while(count.compareTo(upper) < 0) {
            //     if(count.mod(BigInteger.valueOf(100000)).equals(BigInteger.ZERO)) {
            //         System.out.println(count);
            //     }
            //     String output = program.executeProgram(count);
            //     if(originalProgramCode.startsWith(output)) {
            //         System.out.println(count + " : "
            //             // + output.length()
            //             // + " : "
            //             + output);
            //         return null;
            //     }
            //     if(program.stringProgramMatches(output)) {
            //         return count;
            //     }
            //     count = count.add(BigInteger.ONE);
            // }
            //
            // return -1;
        }
    }

    public static class Program {
        private final BigInteger registerA;
        private final BigInteger registerB;
        private final BigInteger registerC;
        private List<Integer> program = new ArrayList<>();

        public Program(List<String> lines) {
            long tempRegA = -1;
            long tempRegB = -1;
            long tempRegC = -1;

            for(String line : lines) {
                if(line.startsWith("Register")) {
                    Long registerValue = Long.parseLong(line.split(": ")[1].trim());
                    if(line.contains("A:")) {
                        tempRegA = registerValue;
                    } else if(line.contains("B:")) {
                        tempRegB = registerValue;
                    } else if(line.contains("C:")) {
                        tempRegC = registerValue;
                    }
                } else if(line.startsWith("Program")) {
                    program = Arrays.stream(
                        line.split(": ")[1].trim().split(","))
                        .map(Integer::parseInt)
                        .toList();
                }
            }

            registerA = BigInteger.valueOf(tempRegA);
            registerB = BigInteger.valueOf(tempRegB);
            registerC = BigInteger.valueOf(tempRegC);
        }

        public String executeProgram() {
            return executeProgram(registerA);
        }

        public String executeProgram(BigInteger registerAOverride) {

            BigInteger tempRegA = registerAOverride;
            BigInteger tempRegB = registerB;
            BigInteger tempRegC = registerC;

            int index = 0;
            List<String> output = new ArrayList<>();

            while (index < program.size()) {
                int operator = program.get(index);
                int operand = program.get(index + 1);
                BigInteger comboOperand = BigInteger.valueOf(-1);
                switch (operand) {
                    case 0 -> comboOperand = BigInteger.ZERO;
                    case 1 -> comboOperand = BigInteger.ONE;
                    case 2 -> comboOperand = BigInteger.TWO;
                    case 3 -> comboOperand = BigInteger.valueOf(3);
                    case 4 -> comboOperand = tempRegA;
                    case 5 -> comboOperand = tempRegB;
                    case 6 -> comboOperand = tempRegC;
                }

                switch (operator) {
                    case 0 -> {
                        BigInteger numerator = tempRegA;
                        BigInteger denominator = BigInteger.TWO.pow(comboOperand.intValue());
                        tempRegA = numerator.divide(denominator);
                    }
                    case 1 -> tempRegB = BigInteger.valueOf(operand).xor(tempRegB);
                    case 2 -> tempRegB = comboOperand.mod(BigInteger.valueOf(8));
                    case 3 -> {
                        if (!tempRegA.equals(BigInteger.ZERO)) {
                            index = operand;
                            continue;
                        }
                    }
                    case 4 -> tempRegB = tempRegC.xor(tempRegB);
                    case 5 -> {
                        String value = String.valueOf(comboOperand.mod(BigInteger.valueOf(8)));
                        output.add(value);
                    }
                    case 6 -> {
                        BigInteger denominator = BigInteger.TWO.pow(comboOperand.intValue());
                        tempRegB = tempRegA.divide(denominator);
                    }
                    case 7 -> {
                        BigInteger denominator = BigInteger.TWO.pow(comboOperand.intValue());
                        tempRegC = (tempRegA.divide(denominator));
                    }
                }

                index += 2;
            }

            return String.join(",", output);
        }

        public Long findBackwards() {
            String originalProgramCode = StringUtils.join(this.program.stream().map(String::valueOf).toList(), ",");

            Stack<Long> possibilities = new Stack<>();
            possibilities.add(0L);
            List<Long> answers = new ArrayList<>();

            while(!possibilities.isEmpty()) {
                Long possibility = possibilities.pop();
                possibility = possibility << 3;
                for (int i = 0; i < 8; i++) {
                    String output = executeProgram(BigInteger.valueOf(possibility + i));

                    if(originalProgramCode.equals(output)) {
                        answers.add(possibility + i);
                    } else if(originalProgramCode.endsWith(output)) {
                        possibilities.push(possibility+i);
                    }
                }
            }

            System.out.println(answers);

            return answers.stream().mapToLong(Long::longValue).min().orElseThrow();
        }

        public boolean stringProgramMatches(String program) {
            return this.program.equals(Arrays.stream(
                program.split(","))
                .map(s -> Integer.parseInt(s.trim()))
                .toList());
        }

        public List<Integer> getOriginalProgramCode() {
            return program.stream().toList();
        }
    }
}
