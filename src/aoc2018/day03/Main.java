package aoc2018.day03;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
            return analyzeInput(lines).values().stream()
                .filter(i -> i.size()>1)
                .toList()
                .size();
        }

        public Object calculateP2(List<String> lines) {
            Map<Point, Set<Integer>> fabric = analyzeInput(lines);
            Set<Integer> ids = new HashSet<>();
            fabric.values().forEach(ids::addAll);

            for(Point point : fabric.keySet()) {
                Set<Integer> swatchesAtPoint = fabric.get(point);
                if(swatchesAtPoint.size() > 1) {
                    ids.removeAll(swatchesAtPoint);
                }
            }
            return ids;
        }

        public Map<Point, Set<Integer>> analyzeInput(List<String> lines) {
            Map<Point, Set<Integer>> fabricMap = new HashMap<>();

            Pattern inputPattern = Pattern.compile("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)");
            for(String line : lines) {
                Matcher m = inputPattern.matcher(line);
                if(m.find()) {
                    int startingHorizontal = Integer.parseInt(m.group(2));
                    int startingVertical = Integer.parseInt(m.group(3));
                    int fabricWidth = Integer.parseInt(m.group(4));
                    int fabricHeight = Integer.parseInt(m.group(5));

                    for(int vertical=startingVertical; vertical<(startingVertical+fabricHeight); vertical++){
                        for(int horizontal=startingHorizontal; horizontal<(startingHorizontal+fabricWidth); horizontal++) {
                            Point point = new Point(horizontal, vertical);
                            Set<Integer> fabricsAtPoint = fabricMap.getOrDefault(point, new HashSet<>());
                            fabricsAtPoint.add(Integer.parseInt(m.group(1)));
                            fabricMap.put(point, fabricsAtPoint);
                        }
                    }
                }
            }

            return fabricMap;
        }
    }

    public record Point(int horizontal, int vertical){}
}
