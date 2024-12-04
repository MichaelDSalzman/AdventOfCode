package aoc2016.day12;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2016";
        String day = "12";

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
            return runProgram(lines, new int[4]);
        }

        private int runProgram(List<String> lines, int[] registers) {
            int lineNum = 0;

            Pattern isNumber = Pattern.compile("-?\\d+");

            while(lineNum < lines.size()) {
                String line = lines.get(lineNum);
                String[] parts = line.split(" ");

                switch (parts[0]) {
                    case "cpy" -> {
                        String source = parts[1];
                        String destination = parts[2];
                        int value;
                        if (isNumber.matcher(source).find()) {
                            value = Integer.parseInt(source);
                        } else {
                            value = registers[source.charAt(0) - 97];
                        }

                        registers[destination.charAt(0) - 97] = value;
                        lineNum++;
                    }
                    case "inc" -> {
                        String destination = parts[1];
                        registers[destination.charAt(0) - 97]++;
                        lineNum++;
                    }
                    case "dec" -> {
                        String destination = parts[1];
                        registers[destination.charAt(0) - 97]--;
                        lineNum++;
                    }
                    case "jnz" -> {
                        String source = parts[1];
                        int distance = Integer.parseInt(parts[2]);
                        int value;

                        if (isNumber.matcher(source).find()) {
                            value = Integer.parseInt(source);
                        } else {
                            value = registers[source.charAt(0) - 97];
                        }

                        if (value != 0) {
                            lineNum += distance;
                        } else {
                            lineNum++;
                        }
                    }
                }
            }

            return registers[0];
        }

        public int calculateP2(List<String> lines) {
            int[] registers = new int[4];
            registers[2] = 1;
            return runProgram(lines, registers);
        }
    }
}
