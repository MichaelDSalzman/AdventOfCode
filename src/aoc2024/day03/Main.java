package aoc2024.day03;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2024";
        String day = "03";

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
            Pattern p = Pattern.compile("mul\\((\\d+),(\\d+)\\)");
            long total = 0;

            for(String line : lines) {
                Matcher m = p.matcher(line);
                while (m.find()) {
                    long num1 = Integer.parseInt(m.group(1));
                    long num2 = Integer.parseInt(m.group(2));

                    total += (num1 * num2);
                }
            }
            return total;
        }

        public Object calculateP2(List<String> lines) {
            Pattern p = Pattern.compile("do\\(\\)|don't\\(\\)|mul\\((\\d+),(\\d+)\\)");
            long total = 0;

            boolean enabled = true;
            for(String line : lines) {
                Matcher m = p.matcher(line);
                while (m.find()) {
                    if(m.group().equals("do()")) {
                        enabled = true;
                        System.out.print("do");
                    } else if(m.group().equals("don't()")) {
                        System.out.print("dont");
                        enabled = false;
                    } else if(m.group().startsWith("mul") && enabled) {
                        long num1 = Integer.parseInt(m.group(1));
                        long num2 = Integer.parseInt(m.group(2));
                        System.out.print(num1);
                        System.out.print(num2);

                        total += (num1 * num2);
                    }
                }
            }
            return total;
        }
    }
}
