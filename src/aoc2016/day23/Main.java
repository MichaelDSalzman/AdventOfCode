package aoc2016.day23;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2016";
        String day = "23";

        // FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/sample.txt");
        FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/input.txt");
        List<String> lines = fileReader.getLines();

        Problem problem = new Problem();
        long start = System.currentTimeMillis();
        // System.out.println ("P1 : " + problem.calculateP1(lines));
        System.out.println("P1 took " + (System.currentTimeMillis() - start) + " millis");
        start = System.currentTimeMillis();
        System.out.println ("P2 : " + problem.calculateP2(lines));
        System.out.println("P2 took " + (System.currentTimeMillis() - start) + " millis");
    }

    public static class Problem {

        public long calculateP1(List<String> lines) {

            Map<String, Long> registers = new HashMap<>();
            registers.put("a", 7L);
            registers.put("b", 0L);
            registers.put("c", 0L);
            registers.put("d", 0L);
            return runProgram(lines, registers);
        }

        public long calculateP2(List<String> lines) {

            Map<String, Long> registers = new HashMap<>();
            registers.put("a", 12L);
            registers.put("b", 0L);
            registers.put("c", 0L);
            registers.put("d", 0L);
            return runProgram(lines, registers);
        }

        private long runProgram(List<String> lines, Map<String, Long> registers) {

            for(int lineNum = 0; lineNum < lines.size(); lineNum++) {
                String line = lines.get(lineNum);

                String[] parts = line.split(" ");
                // System.out.println(line + " - " + registers);

                switch (parts[0]) {
                    case "cpy" -> {
                        String source = parts[1];
                        String destination = parts[2];
                        long value;
                        if (NumberUtils.isCreatable(source)) {
                            value = Long.parseLong(source);
                        } else {
                            value = registers.get(source);
                        }

                        registers.put(destination, value);
                    }
                    case "inc" -> {
                        String destination = parts[1];

                        registers.put(destination, registers.get(destination)+1);
                    }
                    case "dec" -> {
                        String destination = parts[1];
                        registers.put(destination, registers.get(destination)-1);
                    }
                    case "jnz" -> {
                        String source = parts[1];
                        long distance;
                        if(NumberUtils.isCreatable(parts[2])) {
                            distance = Long.parseLong(parts[2]);
                        } else {
                            distance = registers.get(parts[2]);
                        }
                        long value;

                        if (NumberUtils.isCreatable(source)) {
                            value = Long.parseLong(source);
                        } else {
                            value = registers.get(source);
                        }

                        if (value != 0) {
                            lineNum += (int) (distance - 1);
                        }
                    }
                    case "tgl" -> {
                        long distance = registers.get(parts[1]);
                        if(lineNum + distance >= lines.size()) {
                            continue;
                        }

                        String lineToChange = lines.get((int) (lineNum + distance));
                        String[] lineToChangeParts = lineToChange.split(" ");
                        switch (lineToChangeParts[0]) {
                            case "cpy" -> {
                                lineToChangeParts[0] = "jnz";
                            }
                            case "dec" -> {
                                lineToChangeParts[0] = "inc";
                            }
                            case "tgl" -> {
                                lineToChangeParts[0] = "inc";
                            }
                            case "inc" -> {
                                lineToChangeParts[0] = "dec";
                            }
                            case "jnz" -> {
                                lineToChangeParts[0] = "cpy";
                            }
                        }
                        //
                        // if (lineToChangeParts.length == 2) {
                        //     if (lineToChangeParts[0].equals("inc")) {
                        //         lineToChangeParts[0] = "dec";
                        //     } else {
                        //         lineToChangeParts[0] = "inc";
                        //     }
                        // } else if (lineToChangeParts.length == 3) {
                        //     if (lineToChangeParts[0].equals("jnz")) {
                        //         lineToChangeParts[0] = "cpy";
                        //     } else {
                        //         lineToChangeParts[0] = "jnz";
                        //     }
                        // }
                        lines.set((int) (lineNum + distance),
                            StringUtils.joinWith(" ", lineToChangeParts));

                    }
                    default -> throw new RuntimeException(line);
                }
            }

            return registers.get("a");
        }
    }
}
