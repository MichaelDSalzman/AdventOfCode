package aoc2024.day07;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2024";
        String day = "07";

        // FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/sample.txt");
        FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/input.txt");
        List<String> lines = fileReader.getLines();

        Problem problem = new Problem();
        long start = System.currentTimeMillis();
        System.out.println ("P1 : " + String.format("%.0f", problem.calculateP1(lines)));

        System.out.println("P1 took " + (System.currentTimeMillis() - start) + " millis");
        start = System.currentTimeMillis();
        System.out.println ("P2 : " + String.format("%.0f", problem.calculateP2(lines)));
        System.out.println("P2 took " + (System.currentTimeMillis() - start) + " millis");
    }

    public static class Problem {

        public double calculateP1(List<String> lines) {
            double total = 0;
            for(String line : lines) {
                Equation equation = Equation.valueOf(line);

                boolean canBeSolved = canBeSolved(equation, List.of("+", "*"));
                if(canBeSolved) {
                    total += equation.value;
                }
            }

            return total;
        }

        public double calculateP2(List<String> lines) {

            double total = 0;
            for(String line : lines) {
                Equation equation = Equation.valueOf(line);

                boolean canBeSolved = canBeSolved(equation, List.of("+", "*", "|"));
                if(canBeSolved) {
                    total += equation.value;
                }
            }

            return total;
        }

        public boolean canBeSolved(Equation equation, List<String> validOperators) {
            int numOperators = equation.terms.size() - 1;
            double numIterations = Math.pow(validOperators.size(), numOperators);
            for(int i=0; i<numIterations; i++) {
                String operatorString =
                    StringUtils.leftPad(Integer.toString(i, validOperators.size()), numOperators,
                        '0');
                for (int operatorIdx = 0; operatorIdx < validOperators.size(); operatorIdx++) {
                    operatorString = operatorString.replaceAll(String.valueOf(operatorIdx),
                        validOperators.get(operatorIdx));
                }

                double register = equation.terms.get(0);
                for(int termIdx=1; termIdx<equation.terms.size(); termIdx++) {
                    char operation = operatorString.charAt(termIdx-1);
                    double term = equation.terms.get(termIdx);

                    switch (operation) {
                        case '+' -> register += term;
                        case '*' -> register *= term;
                        case '|' -> register = Double.parseDouble(String.format("%.0f", register) + term);
                    }
                }

                if(register == equation.value) {
                    return true;
                }
            }

            return false;

        }
    }

    public record Equation(double value, List<Double> terms) {
        static Equation valueOf(String line) {

            String[] split = line.split(":? ");
            return new Equation(Double.parseDouble(split[0]),
                Arrays.asList(split).subList(1, split.length)
                    .stream()
                    .map(Double::parseDouble)
                    .toList());
        }
    }
}
