package aoc2024.day07;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.BiFunction;
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
        System.out.println ("P1 : " + problem.calculateP1(lines));

        System.out.println("P1 took " + (System.currentTimeMillis() - start) + " millis");
        start = System.currentTimeMillis();
        System.out.println ("P2 : " + problem.calculateP2(lines));
        System.out.println("P2 took " + (System.currentTimeMillis() - start) + " millis");
    }

    public static class Problem {

        // Determine which lines can be converted to a correct equation using "+" and "*"
        public long calculateP1(List<String> lines) {
            return lines.stream()
                .map(Equation::valueOf)
                .filter(e -> canBeSolved(e, List.of(Operator.ADD, Operator.MULTIPLY)))
                .map(Equation::value)
                .mapToLong(Long::longValue)
                .sum();
        }

        // Determine which lines can be converted to a correct equation using "+", "*", and "|"
        public long calculateP2(List<String> lines) {
            return lines.stream()
                .map(Equation::valueOf)
                .filter(e -> canBeSolved(e, List.of(Operator.ADD, Operator.MULTIPLY, Operator.CONCAT)))
                .map(Equation::value)
                .mapToLong(Long::longValue)
                .sum();
        }

        /**
         * Return true if an equation can be solved using the list of operators
         */
        public boolean canBeSolved(Equation equation, List<Operator> validOperators) {

            // How many operators are missing
            int numOperators = equation.terms.size() - 1;

            // Total number of permutations of the operators
            long numIterations = (long) Math.pow(validOperators.size(), numOperators);

            // Try out each iteration
            for(int i=0; i<numIterations; i++) {
                // Convert the index into a base X number using the number of valid operators
                // use the number to create the list of operators to test out
                Queue<Operator> operators =
                    new LinkedList<>(Arrays.stream(
                        StringUtils.leftPad(
                            Integer.toString(i, validOperators.size()), numOperators, '0')
                            .split(""))
                        .map(Integer::parseInt)
                        .map(validOperators::get)
                        .toList());

                // Convert list to a queue for easier polling
                Queue<Long> terms = new LinkedList<>(equation.terms);

                // Grab first number in terms
                long register = terms.remove();

                while(!terms.isEmpty()) {
                    // Grab the next operator perform operation
                    register = operators.remove().execute(register, terms.remove());

                    // If the result is larger than the desired value, this sequence cannot be correct
                    //  move on to the next sequence
                    if(register > equation.value) {
                        break;
                    }
                }

                // If this sequence satisfies the equation, return true
                if(register == equation.value) {
                    return true;
                }
            }

            return false;

        }
    }

    public record Equation(long value, List<Long> terms) {

        // Split the input line into the value and the list of terms
        static Equation valueOf(String line) {
            String[] split = line.split(":? ");
            return new Equation(Long.parseLong(split[0]),
                Arrays.asList(split).subList(1, split.length)
                    .stream()
                    .map(Long::parseLong)
                    .toList());
        }
    }

    public enum Operator {
        ADD(Long::sum),
        MULTIPLY((n1, n2) -> n1 * n2),
        CONCAT((n1, n2) -> Long.parseLong(String.valueOf(n1) + n2));

        // Function that will perform the operation
        private final BiFunction<Long, Long, Long> opFunc;

        Operator(BiFunction<Long, Long, Long> opFunc) {
            this.opFunc = opFunc;
        }

        public long execute(long n1, long n2) {
            return opFunc.apply(n1, n2);
        }

    }
}
