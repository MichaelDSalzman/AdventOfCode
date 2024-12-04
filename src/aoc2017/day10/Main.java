package aoc2017.day10;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2017";
        String day = "10";

        // FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/sample.txt");
        FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/input.txt");
        List<String> lines = fileReader.getLines();

        Problem problem = new Problem();
        long start = System.currentTimeMillis();
        System.out.println("P1 : " + problem.calculateP1(lines));
        System.out.println("P1 took " + (System.currentTimeMillis() - start) + " millis");
        start = System.currentTimeMillis();
        System.out.println("P2 : " + problem.calculateP2(lines));
        System.out.println("P2 took " + (System.currentTimeMillis() - start) + " millis");
    }

    public static class Problem {

        public int calculateP1(List<String> lines) {

            // int[] rope = algorithm(5, Arrays.stream(lines.get(0).split(",")).toList().stream().map(Integer::parseInt).toList(), 1);
            int[] rope = algorithm(256,
                Arrays.stream(lines.get(0).split(",")).toList().stream().map(Integer::parseInt)
                    .toList(), 1);
            return rope[0] * rope[1];
        }

        public String calculateP2(List<String> lines) {
            return generateKnotHash(lines.get(0));
        }

        private static int[] algorithm(int ropeSize, List<Integer> instructions, int numExecutions) {
            int index = 0;
            int skipSize = 0;

            int[] rope = new int[ropeSize];
            for (int i = 0; i < ropeSize; i++) {
                rope[i] = i;
            }

            for (int executionNum = 0; executionNum < numExecutions; executionNum++) {
                for (int instruction : instructions) {
                    int[] reversedArray = new int[instruction];
                    for (int i = 0; i < instruction; i++) {
                        reversedArray[instruction - i - 1] = rope[(i + index) % ropeSize];
                    }

                    for (int i = 0; i < instruction; i++) {
                        rope[(i + index) % ropeSize] = reversedArray[i];
                    }

                    index += instruction;
                    index += skipSize;
                    index = index % ropeSize;
                    skipSize++;
                }
            }

            return rope;
        }

        public static String generateKnotHash(String stringInput) {
            List<Integer> input = new java.util.ArrayList<>(
                Arrays.stream(stringInput.chars().toArray()).boxed().toList());
            input.add(17);
            input.add(31);
            input.add(73);
            input.add(47);
            input.add(23);

            int[] sparseHash = algorithm(256, input, 64);
            int[] denseHash = new int[16];
            for (int i = 0; i < 16; i++) {
                int digit =
                    sparseHash[(16 * i)] ^
                        sparseHash[(16 * i) + 1] ^
                        sparseHash[(16 * i) + 2] ^
                        sparseHash[(16 * i) + 3] ^
                        sparseHash[(16 * i) + 4] ^
                        sparseHash[(16 * i) + 5] ^
                        sparseHash[(16 * i) + 6] ^
                        sparseHash[(16 * i) + 7] ^
                        sparseHash[(16 * i) + 8] ^
                        sparseHash[(16 * i) + 9] ^
                        sparseHash[(16 * i) + 10] ^
                        sparseHash[(16 * i) + 11] ^
                        sparseHash[(16 * i) + 12] ^
                        sparseHash[(16 * i) + 13] ^
                        sparseHash[(16 * i) + 14] ^
                        sparseHash[(16 * i) + 15];
                denseHash[i] = digit;
            }

            StringBuilder sb = new StringBuilder();
            for (int hash : denseHash) {
                sb.append(String.format("%02x", hash));
            }

            return sb.toString();
        }
    }
}
