package aoc2015.day16;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2015";
        String day = "16";

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
            Map<String, Integer> knownQuantities = new HashMap<>();
            knownQuantities.put("children", 3);
            knownQuantities.put("cats", 7);
            knownQuantities.put("samoyeds", 2);
            knownQuantities.put("pomeranians", 3);
            knownQuantities.put("akitas", 0);
            knownQuantities.put("vizslas", 0);
            knownQuantities.put("goldfish", 5);
            knownQuantities.put("trees", 3);
            knownQuantities.put("cars", 2);
            knownQuantities.put("perfumes", 1);

            Pattern p = Pattern.compile("Sue (\\d+): (.*): (\\d+), (.*): (\\d+), (.*): (\\d+)");
            for (String line : lines) {
                Matcher m = p.matcher(line);
                if(!m.find()) {
                    System.out.println(line);
                }

                if(knownQuantities.get(m.group(2)) != Integer.parseInt(m.group(3)) ||
                    knownQuantities.get(m.group(4)) != Integer.parseInt(m.group(5)) ||
                    knownQuantities.get(m.group(6)) != Integer.parseInt(m.group(7))) {
                    continue;
                }

                System.out.println(m.group(1));
            }
            return -1;
        }

        public int calculateP2(List<String> lines) {
            Map<String, Integer> knownQuantities = new HashMap<>();
            knownQuantities.put("children", 3);
            knownQuantities.put("cats", 7);
            knownQuantities.put("samoyeds", 2);
            knownQuantities.put("pomeranians", 3);
            knownQuantities.put("akitas", 0);
            knownQuantities.put("vizslas", 0);
            knownQuantities.put("goldfish", 5);
            knownQuantities.put("trees", 3);
            knownQuantities.put("cars", 2);
            knownQuantities.put("perfumes", 1);

            Pattern p = Pattern.compile("Sue (\\d+): (.*): (\\d+), (.*): (\\d+), (.*): (\\d+)");
            for (String line : lines) {
                Matcher m = p.matcher(line);
                if(!m.find()) {
                    System.out.println(line);
                }

                if(calculateItem(knownQuantities, m.group(2), Integer.parseInt(m.group(3))) &&
                    calculateItem(knownQuantities, m.group(4), Integer.parseInt(m.group(5))) &&
                    calculateItem(knownQuantities, m.group(6), Integer.parseInt(m.group(7)))) {
                    System.out.println(m.group(1));
                }

            }
            return -1;
        }

        private boolean calculateItem(Map<String, Integer> knownQuantities, String item, Integer quantity) {
            if(item.equals("cats") || item.equals("trees")) {
                return knownQuantities.get(item) < quantity;
            }

            if(item.equals("pomeranians") || item.equals("goldfish")) {
                return knownQuantities.get(item) > quantity;
            }

            return knownQuantities.get(item) == quantity;
        }
    }
}
