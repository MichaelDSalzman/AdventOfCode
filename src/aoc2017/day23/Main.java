package aoc2017.day23;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            return runProgram(lines);
        }

        public Object calculateP2(List<String> lines) {
            long a = 1;
            long b = 0, c = 0, d = 0, e = 0, f = 0, g = 0, h = 0;

            b = 108400;
            c = 125400;

            while(true) {
                System.out.println("WHILE, b = "
                    + StringUtils.leftPad(String.valueOf(b), 10)
                    + " c = "
                    + StringUtils.leftPad(String.valueOf(c), 10)
                    + " f = "
                    + StringUtils.leftPad(String.valueOf(f), 10)
                    + " g = "
                    + StringUtils.leftPad(String.valueOf(g), 10)
                    + " h = "
                    + StringUtils.leftPad(String.valueOf(h), 10)
                );
                f = 1;
                d = 2;

                do {
                    // System.out.println("OUTER WHILE, g = "
                    //     + StringUtils.leftPad(String.valueOf(g), 10)
                    //     + " e = "
                    //     + StringUtils.leftPad(String.valueOf(e), 10));
                    e = 2;
                    do {
                        g = (d * e) - b;
                        // System.out.println("INNER WHILE, g = "
                        //     + StringUtils.leftPad(String.valueOf(g), 10)
                        //     + " e = "
                        //     + StringUtils.leftPad(String.valueOf(e), 10));

                        if (g == 0) {
                            f = 0;
                        }
                        e++;
                        g = e - b;
                    } while (g != 0);
                    d++;
                    g = d - b;
                } while (g != 0);

                if (f == 0) {
                    h++;
                }

                g = b - c;
                if (g == 0) {
                    return h;
                }

                b += 17;
            }
        }

        private long runProgram(List<String> lines) {
            int index = 0;
            Map<String, Long> registers = new HashMap<>();

            long numMulCalled = 0;

            while(index < lines.size()) {
                String[] parts = lines.get(index).split("\\s+");

                switch(parts[0]) {
                    case "set" -> {
                        String register = parts[1];
                        Long value = isNumeric(parts[2]) ? Long.parseLong(parts[2]) : registers.getOrDefault(parts[2], 0L);
                        registers.put(register, value);
                    }
                    case "sub" -> {
                        String register = parts[1];
                        Long value = isNumeric(parts[2]) ? Long.parseLong(parts[2]) : registers.getOrDefault(parts[2], 0L);
                        registers.put(register, registers.getOrDefault(register, 0L) - value);
                    }
                    case "mul" -> {
                        String register = parts[1];
                        Long value = isNumeric(parts[2]) ? Long.parseLong(parts[2]) : registers.getOrDefault(parts[2], 0L);
                        registers.put(register, registers.getOrDefault(register, 0L) * value);
                        numMulCalled++;
                    }
                    case "jnz" -> {
                        long value = isNumeric(parts[1]) ? Long.parseLong(parts[1]) : registers.getOrDefault(parts[1], 0L);
                        long jumpDistance = isNumeric(parts[2]) ? Long.parseLong(parts[2]) : registers.getOrDefault(parts[2], 0L);
                        if(value != 0) {
                            index += (int) jumpDistance;
                            continue;
                        }
                    }
                }

                index++;
            }

            return numMulCalled;
        }

        private boolean isNumeric(String value) {
            try {
                Long.parseLong(value);
                return true;
            } catch (NumberFormatException ex) {
                return false;
            }
        }
    }
}
