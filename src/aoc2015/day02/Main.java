package aoc2015.day02;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2015";
        String day = "02";

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
            int sum=0;
            Pattern p = Pattern.compile("(\\d+)x(\\d+)x(\\d+)");
            for(String line : lines) {
                Matcher m = p.matcher(line);
                List<Integer> numbers = new ArrayList<>();
                m.find();
                numbers.add(Integer.parseInt(m.group(1)) * Integer.parseInt(m.group(2)));
                numbers.add(Integer.parseInt(m.group(2)) * Integer.parseInt(m.group(3)));
                numbers.add(Integer.parseInt(m.group(3)) * Integer.parseInt(m.group(1)));

                Collections.sort(numbers);

                sum += numbers.get(0) + numbers.stream().reduce(0, (sub, elem) -> sub + elem*2);
            }

            return sum;
        }

        public int calculateP2(List<String> lines) {
            int sum=0;
            Pattern p = Pattern.compile("(\\d+)x(\\d+)x(\\d+)");
            for(String line : lines) {
                Matcher m = p.matcher(line);
                List<Integer> numbers = new ArrayList<>();
                m.find();
                numbers.add(Integer.parseInt(m.group(1)));
                numbers.add(Integer.parseInt(m.group(2)));
                numbers.add(Integer.parseInt(m.group(3)));

                Collections.sort(numbers);

                sum += 2*numbers.get(0) + 2*numbers.get(1) + (numbers.get(0) * numbers.get(1) * numbers.get(2));
            }
            return sum;
        }
    }
}
