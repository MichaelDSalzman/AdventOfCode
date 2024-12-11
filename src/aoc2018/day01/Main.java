package aoc2018.day01;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2018";
        String day = "01";

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
            int value = 0;
            Pattern p = Pattern.compile("([+-])(\\d+)");
            for(String line : lines) {
                Matcher m = p.matcher(line);
                if(m.find()) {
                    int num = Integer.parseInt(m.group(2));
                    if(m.group(1).equals("-")) {
                        value -= num;
                    } else {
                        value += num;
                    }
                }
            }

            return value;
        }

        public int calculateP2(List<String> lines) {
            Set<Integer> seenValues = new HashSet<>();
            int value = 0;
            seenValues.add(value);

            Pattern p = Pattern.compile("([+-])(\\d+)");
            while(true) {
                for (String line : lines) {
                    Matcher m = p.matcher(line);
                    if (m.find()) {
                        int num = Integer.parseInt(m.group(2));
                        if (m.group(1).equals("-")) {
                            value -= num;
                        } else {
                            value += num;
                        }

                        if (seenValues.contains(value)) {
                            return value;
                        }

                        seenValues.add(value);
                    }
                }
            }
        }
    }
}
