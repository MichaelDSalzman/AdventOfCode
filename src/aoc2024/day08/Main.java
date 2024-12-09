package aoc2024.day08;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2024";
        String day = "08";

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
            City city = City.valueOf(lines);

            Set<Point> antinodes = new HashSet<>();
            Map<String, List<Tower>> towersByFrequency = city.towers();
            for(String frequency : towersByFrequency.keySet()) {
                List<Tower> towers = towersByFrequency.get(frequency);
                if(towers.size() < 2) {
                    continue;
                }

                for(int i=0; i<towers.size(); i++) {
                    for(int j=i+1; j<towers.size(); j++) {
                        Tower t1 = towers.get(i);
                        Tower t2 = towers.get(j);

                        int deltaHorizontal = t1.location.horizontal - t2.location.horizontal;
                        int deltaVertical = t1.location.vertical - t2.location.vertical;
                        Point a1 = new Point(t1.location.horizontal + deltaHorizontal, t1.location.vertical+deltaVertical);
                        Point a2 = new Point(t2.location.horizontal - deltaHorizontal, t2.location.vertical-deltaVertical);

                        if(pointWithinCity(city, a1)) {
                            antinodes.add(a1);
                        }
                        if(pointWithinCity(city, a2)) {
                            antinodes.add(a2);
                        }
                    }
                }
            }

            return antinodes.size();
        }

        public int calculateP2(List<String> lines) {
            City city = City.valueOf(lines);

            Set<Point> antinodes = new HashSet<>();
            Map<String, List<Tower>> towersByFrequency = city.towers();
            for(String frequency : towersByFrequency.keySet()) {
                List<Tower> towers = towersByFrequency.get(frequency);
                if(towers.size() < 2) {
                    continue;
                }

                for(int i=0; i<towers.size(); i++) {
                    for(int j=i+1; j<towers.size(); j++) {
                        Tower t1 = towers.get(i);
                        Tower t2 = towers.get(j);

                        int deltaHorizontal = t1.location.horizontal - t2.location.horizontal;
                        int deltaVertical = t1.location.vertical - t2.location.vertical;

                        int gcd = greatestCommonDivisor(deltaHorizontal, deltaVertical);
                        deltaHorizontal /= gcd;
                        deltaVertical /= gcd;

                        int count = 0;
                        while(true) {
                            Point antinode = new Point(t1.location.horizontal + (count * deltaHorizontal),
                                t1.location.vertical + (count * deltaVertical));
                            if(!pointWithinCity(city, antinode)) {
                                break;
                            }
                            antinodes.add(antinode);
                            count++;
                        }

                        count = 0;
                        while(true) {
                            Point antinode = new Point(t2.location.horizontal - (count * deltaHorizontal),
                                t2.location.vertical - (count * deltaVertical));
                            if(!pointWithinCity(city, antinode)) {
                                break;
                            }
                            antinodes.add(antinode);
                            count++;
                        }
                    }
                }
            }

            return antinodes.size();
        }

        private boolean pointWithinCity(City city, Point point) {
            return point.horizontal >= 0 && point.horizontal <= city.maxHorizontal &&
                point.vertical >= 0 && point.vertical <= city.maxVertical;
        }

        private int greatestCommonDivisor(int n1, int n2) {
            if (n2 == 0) {
                return n1;
            }
            return greatestCommonDivisor(n2, n1 % n2);
        }
    }

    record Point(int horizontal, int vertical) {}
    record Tower(String frequency, Point location) {}
    record City(Map<String, List<Tower>> towers, int maxHorizontal, int maxVertical) {

        static City valueOf(List<String> lines) {
            Map<String, List<Tower>> towers = new HashMap<>();

            for(int vertical=0; vertical<lines.size(); vertical++){
                String line = lines.get(vertical);

                for(int horizontal=0; horizontal<line.length(); horizontal++) {
                    String frequency = line.substring(horizontal, horizontal+1);
                    if(!frequency.equals(".")) {
                        List<Tower> towersForFrequency = towers.getOrDefault(frequency, new ArrayList<>());
                        towersForFrequency.add(new Tower(frequency, new Point(horizontal, vertical)));
                        towers.put(frequency, towersForFrequency);
                    }
                }
            }

            return new City(towers, lines.get(0).length()-1, lines.size()-1);
        }
    }
}
