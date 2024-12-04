package aoc2015.day14;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2015";
        String day = "14";

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

            int numSeconds = 2503;
            int maxDistance = Integer.MIN_VALUE;

            Pattern p = Pattern.compile(
                ".* can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds.");
            for (String line : lines) {
                Matcher m = p.matcher(line);
                if (m.find()) {
                    int speed = Integer.parseInt(m.group(1));
                    int flyTime = Integer.parseInt(m.group(2));
                    int restTime = Integer.parseInt(m.group(3));

                    int distanceTraveledPerPeriod = speed * flyTime;
                    int periodLength = flyTime + restTime;
                    int numPeriods = numSeconds / periodLength;

                    int distance = distanceTraveledPerPeriod * numPeriods;
                    if (numSeconds > (numPeriods * periodLength)) {
                        int difference = numSeconds - (numPeriods * periodLength);
                        distance += (Math.min(difference, flyTime) * speed);
                    }

                    if (distance > maxDistance) {
                        maxDistance = distance;
                    }
                }
            }

            return maxDistance;

        }

        public int calculateP2(List<String> lines) {

            // int total = 1000;
            int total = 2503;
            Map<String, Integer> points = new HashMap<>();

            Pattern p = Pattern.compile(
                "(.*) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds.");
            for (int numSeconds = 1; numSeconds <= total; numSeconds++) {
                List<String> winners = new ArrayList<>();
                Integer winnerDistance = -1;

                for (String line : lines) {
                    Matcher m = p.matcher(line);
                    if (m.find()) {
                        String name = m.group(1);
                        int speed = Integer.parseInt(m.group(2));
                        int flyTime = Integer.parseInt(m.group(3));
                        int restTime = Integer.parseInt(m.group(4));

                        int distanceTraveledPerPeriod = speed * flyTime;
                        int periodLength = flyTime + restTime;
                        int numPeriods = numSeconds / periodLength;

                        int distance = distanceTraveledPerPeriod * numPeriods;
                        if (numSeconds > (numPeriods * periodLength)) {
                            int difference = numSeconds - (numPeriods * periodLength);
                            distance += (Math.min(difference, flyTime) * speed);
                        }

                        if(distance > winnerDistance) {
                            winnerDistance = distance;
                            winners.clear();
                            winners.add(name);
                        } else if (distance == winnerDistance) {
                            winners.add(name);
                        }
                    }
                }

                for(String winner : winners) {
                    Integer pointsForWinner = points.get(winner);
                    if (pointsForWinner == null) {
                        pointsForWinner = 0;
                    }
                    pointsForWinner++;
                    points.put(winner, pointsForWinner);
                }
            }

            System.out.println(points.values().stream().sorted().toList());
            return 0;
        }
    }
}
