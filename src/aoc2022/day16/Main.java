package aoc2022.day16;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2022";
        String day = "16";

        FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/sample.txt");
        // FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/input.txt");
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
            mapTunnel(lines);

            return -1;
        }

        public int calculateP2(List<String> lines) {
            return -1;
        }

        public void mapTunnel(List<String> lines) {
            Pattern p = Pattern.compile("Valve (.*) has flow rate=(\\d+); tunnels? leads? to valves? (.*)");

            for(String line : lines) {
                Matcher m = p.matcher(line);
                m.find();
                Valve v = new Valve(m.group(1), Integer.parseInt(m.group(2)), Arrays.stream(m.group(3).split(",\\s*")).toList());

                System.out.println("S");
            }
        }
    }

    record Valve(String name, int flowRate, List<String> neighbors) {
    }
}
