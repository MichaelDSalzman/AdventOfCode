package aoc2015.day09;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.collections4.iterators.PermutationIterator;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2015";
        String day = "09";

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
            Pattern p = Pattern.compile("(.*) to (.*) = (\\d+)");
            Map<String, Map<String, Integer>> cityMap = new HashMap<>();
            for(String line : lines ) {
                Matcher m = p.matcher(line);
                if(m.find()) {
                    String city1 = m.group(1);
                    String city2 = m.group(2);
                    Integer distance = Integer.parseInt(m.group(3));

                    Map<String, Integer> city1Map = cityMap.computeIfAbsent(city1,
                        k -> new HashMap<>());

                    Map<String, Integer> city2Map = cityMap.computeIfAbsent(city2,
                        k -> new HashMap<>());

                    city1Map.put(city2, distance);
                    city2Map.put(city1, distance);
                }
            }

            int shortestDistance = Integer.MAX_VALUE;
            int longestDistance = Integer.MIN_VALUE;

            PermutationIterator<String> iterator = new PermutationIterator<>(cityMap.keySet());
            while (iterator.hasNext()) {
                List<String> list = iterator.next();
                int distanceTraveled = 0;

                for(int i=0; i<list.size()-1; i++) {
                    distanceTraveled += cityMap.get(list.get(i)).get(list.get(i+1));
                }
                if(distanceTraveled < shortestDistance) {
                    shortestDistance = distanceTraveled;
                }
                if(distanceTraveled > longestDistance) {
                    longestDistance = distanceTraveled;
                }
            }

            return longestDistance;
        }

        public int calculateP2(List<String> lines) {
            return -1;
        }
    }
}
