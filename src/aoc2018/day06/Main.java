package aoc2018.day06;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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
            Map<Point, ClosestDistanceFromPoint> closestDistanceFromPointMap = findClosestToPoints(lines);

            long minHorizontal = Integer.MAX_VALUE;
            long maxHorizontal = Integer.MIN_VALUE;
            long minVertical = Integer.MAX_VALUE;
            long maxVertical = Integer.MIN_VALUE;
            for(Point p : closestDistanceFromPointMap.keySet()) {
                minHorizontal = Math.min(minHorizontal, p.horizontal);
                maxHorizontal = Math.max(maxHorizontal, p.horizontal);
                minVertical = Math.min(minVertical, p.vertical);
                maxVertical = Math.max(maxVertical, p.vertical);
            }

            Set<Integer> idsToThrowAway = new HashSet<>();

            long finalMinHorizontal = minHorizontal;
            long finalMaxVertical = maxVertical;
            long finalMinVertical = minVertical;
            long finalMaxHorizontal = maxHorizontal;

            closestDistanceFromPointMap.keySet().stream().filter(p -> p.horizontal == finalMinHorizontal
                || p.horizontal == finalMaxHorizontal
                || p.vertical == finalMinVertical
                || p.vertical == finalMaxVertical)
                .filter(p -> closestDistanceFromPointMap.get(p).areaIds.size() == 1)
                .forEach(p -> idsToThrowAway.addAll(closestDistanceFromPointMap.get(p).areaIds));

            long maxArea = -1;
            for(int areaId=0; areaId<lines.size(); areaId++) {
                if(idsToThrowAway.contains(areaId)) {
                    continue;
                }
                int finalAreaId = areaId;
                maxArea = Math.max(maxArea, closestDistanceFromPointMap.values().stream()
                    .filter(c -> c.areaIds.size()==1 && c.areaIds.contains(finalAreaId))
                    .toList()
                    .size());
            }
            return maxArea;
        }

        public Object calculateP2(List<String> lines) {
            return findAllPointsWithDistance(lines, 10000);
        }

        private Map<Point, ClosestDistanceFromPoint> findClosestToPoints(List<String> lines) {
            int minHorizontal = Integer.MAX_VALUE;
            int maxHorizontal = Integer.MIN_VALUE;
            int minVertical = Integer.MAX_VALUE;
            int maxVertical = Integer.MIN_VALUE;
            for(String line : lines) {
                String[] parts = line.split(",");
                int horizontal = Integer.parseInt(parts[0].trim());
                int vertical = Integer.parseInt(parts[1].trim());

                if(horizontal<minHorizontal) {
                    minHorizontal = horizontal;
                }
                if(horizontal>maxHorizontal) {
                    maxHorizontal = horizontal;
                }
                if(vertical<minVertical) {
                    minVertical = vertical;
                }
                if(vertical>maxVertical) {
                    maxVertical = vertical;
                }
            }

            Map<Point, ClosestDistanceFromPoint> closestDistanceFromPointMap = new HashMap<>();

            for(int areaId=0; areaId<lines.size(); areaId++) {
                String[] parts = lines.get(areaId).split(",");
                int areaHorizontal = Integer.parseInt(parts[0].trim());
                int areaVertical = Integer.parseInt(parts[1].trim());

                for (long vertical = (minVertical - 5); vertical < (maxVertical + 5);
                     vertical++) {
                    for (long horizontal = (minHorizontal - 5);
                         horizontal < (maxHorizontal + 5); horizontal++) {

                        Point p = new Point(horizontal, vertical);
                        ClosestDistanceFromPoint closestDistanceFromPoint
                            = closestDistanceFromPointMap.get(p);

                        long distance = Math.abs(areaHorizontal - p.horizontal) + Math.abs(areaVertical - p.vertical);
                        if(closestDistanceFromPoint == null || closestDistanceFromPoint.distance > distance) {
                            closestDistanceFromPoint = new ClosestDistanceFromPoint(p, distance, new HashSet<>(Set.of(areaId)));
                        } else if(closestDistanceFromPoint.distance == distance) {
                            closestDistanceFromPoint.areaIds.add(areaId);
                        }

                        closestDistanceFromPointMap.put(p, closestDistanceFromPoint);
                    }
                }
            }

            return closestDistanceFromPointMap;
        }

        public long findAllPointsWithDistance(List<String> lines, long distance) {
            long averageHorizontal = (long) lines.stream()
                .map(l -> Long.parseLong(l.split(",")[0].trim()))
                .mapToLong(Long::longValue)
                .average()
                .orElseThrow();

            long averageVertical = (long) lines.stream()
                .map(l -> Long.parseLong(l.split(",")[1].trim()))
                .mapToLong(Long::longValue)
                .average()
                .orElseThrow();

            Set<Point> allCoordinates = lines.stream()
                .map(l -> new Point(
                    Long.parseLong(l.split(",")[0].trim()),
                    Long.parseLong(l.split(",")[1].trim())))
                .collect(Collectors.toSet());

            Set<Point> pointsWithinDistance = new HashSet<>();

            int index=0;
            boolean keepGoing=true;
            while(keepGoing) {
                keepGoing = false;
                // top row
                for(long i=averageHorizontal-index; i<=averageHorizontal+index; i++) {
                    Point pointUnderConsideration = new Point(i, averageVertical-index);
                    if(allCoordinates.stream()
                        .map(c -> Math.abs(c.horizontal-pointUnderConsideration.horizontal) + Math.abs(c.vertical-pointUnderConsideration.vertical))
                        .mapToLong(Long::longValue)
                        .sum() < distance) {
                        pointsWithinDistance.add(pointUnderConsideration);
                        keepGoing=true;
                    }
                }
                // bottom row
                for(long i=averageHorizontal-index; i<=averageHorizontal+index; i++) {
                    Point pointUnderConsideration = new Point(i, averageVertical+index);
                    if(allCoordinates.stream()
                        .map(c -> Math.abs(c.horizontal-pointUnderConsideration.horizontal) + Math.abs(c.vertical-pointUnderConsideration.vertical))
                        .mapToLong(Long::longValue)
                        .sum() < distance) {
                        pointsWithinDistance.add(pointUnderConsideration);
                        keepGoing=true;
                    }
                }
                // left column
                for(long i=averageVertical-index; i<=averageVertical+index; i++) {
                    Point pointUnderConsideration = new Point(averageHorizontal-index, i);
                    if(allCoordinates.stream()
                        .map(c -> Math.abs(c.horizontal-pointUnderConsideration.horizontal) + Math.abs(c.vertical-pointUnderConsideration.vertical))
                        .mapToLong(Long::longValue)
                        .sum() < distance) {
                        pointsWithinDistance.add(pointUnderConsideration);
                        keepGoing=true;
                    }
                }
                // right column
                for(long i=averageVertical-index; i<=averageVertical+index; i++) {
                    Point pointUnderConsideration = new Point(averageHorizontal+index, i);
                    if(allCoordinates.stream()
                        .map(c -> Math.abs(c.horizontal-pointUnderConsideration.horizontal) + Math.abs(c.vertical-pointUnderConsideration.vertical))
                        .mapToLong(Long::longValue)
                        .sum() < distance) {
                        pointsWithinDistance.add(pointUnderConsideration);
                        keepGoing=true;
                    }
                }
                index++;
            }

            return pointsWithinDistance.size();
        }
    }

    public record Point(long horizontal, long vertical){}
    public record ClosestDistanceFromPoint(Point point, long distance, Set<Integer> areaIds){}
}
