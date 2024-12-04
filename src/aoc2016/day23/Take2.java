package aoc2016.day23;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import util.FileReader;

public class Take2 {

    public static void main(String[] args) throws IOException {
        String year = "2016";
        String day = "23";

        FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/input.txt");
        List<String> lines = fileReader.getLines();

        Map<String, Long> registers = new HashMap<>();
        registers.put("a", 12L);
        registers.put("b", 0L);
        registers.put("c", 0L);
        registers.put("d", 0L);

        System.out.println(runProgram(lines, registers));
    }

    private static long runProgram(List<String> lines, Map<String, Long> registers) {
        for(int i=0; i< lines.size(); i++) {
            String line = lines.get(i);
            System.out.println(line + " - " + registers);
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
            }
        }

        return registers.get("a");
    }
}
