package aoc2016.day25;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import util.FileReader;

public class Main {

    public static void main(String[] args) throws IOException {
        String year = "2016";
        String day = "25";

        FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/input.txt");
        List<String> lines = fileReader.getLines();


        long i =0;
        while(true) {
            if(i%1000 == 0) {
                System.out.println(i);
            }
            Map<String, Long> registers = new HashMap<>();
            registers.put("a", i);
            registers.put("b", 0L);
            registers.put("c", 0L);
            registers.put("d", 0L);

            try {
                System.out.println(runProgram(lines, registers));
                System.out.println(i);
                return;
            } catch (RuntimeException ex) {
                // System.out.println(i + " - " + ex.getMessage());
            }

            i++;
        }
    }

    private static String runProgram(List<String> lines, Map<String, Long> registers) {
        StringBuilder sb = new StringBuilder("");
        for(int i=0; i< lines.size(); i++) {
            String line = lines.get(i);
            String[] split = line.split(" ");

            String cmd = split[0];

            switch (cmd) {
                case "cpy" -> {
                    if(registers.containsKey(split[1])) {
                        registers.put(split[2], registers.get(split[1]));
                    } else {
                        registers.put(split[2], Long.parseLong((split[1])));
                    }
                }
                case "inc" -> {
                    registers.put(split[1], registers.get(split[1]) + 1);
                }
                case "dec" -> {
                    registers.put(split[1], registers.get(split[1]) - 1);
                }
                case "jnz" -> {
                    long val = registers.containsKey(split[1]) ? registers.get(split[1]) : Long.parseLong(split[1]);

                    if(val != 0) {
                        long add = registers.containsKey(split[2]) ? registers.get(split[2]) : Long.parseLong(split[2]);
                        i += (int) (add - 1);
                    }
                }
                case "tgl" -> {
                    long x = registers.get(split[1]);
                    if(i + x >= lines.size()) {
                        continue;
                    }

                    String newLine = lines.get((int) (i+x));
                    String[] newLineSplit = newLine.split(" ");
                    switch (newLineSplit[0]) {
                        case "cpy" -> {
                            newLineSplit[0] = "jnz";
                        }
                        case "dec" -> {
                            newLineSplit[0] = "inc";
                        }
                        case "tgl" -> {
                            newLineSplit[0] = "inc";
                        }
                        case "inc" -> {
                            newLineSplit[0] = "dec";
                        }
                        case "jnz" -> {
                            newLineSplit[0] = "cpy";
                        }
                    }

                    newLine = StringUtils.joinWith(" ", newLineSplit);
                    lines.set((int) (i+x), newLine);
                }
                case "out" -> {
                    long value = registers.containsKey(split[1]) ? registers.get(split[1]) : Integer.parseInt(split[1]);
                    if(value == 0L || value == 1L) {
                        if(!sb.isEmpty() && sb.toString().endsWith(String.valueOf(value))) {
                            throw new RuntimeException("NOPE - DUPLICATE");
                        }
                        sb.append(value);
                    } else {
                        throw new RuntimeException("NOPE - NOT THE RIGHT OUT");
                    }

                    if(sb.length() == 10000) {
                        return sb.toString();
                    }
                }
            }
        }

        return sb.toString();
    }
}
