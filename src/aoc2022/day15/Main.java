package aoc2022.day15;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2022";
        String day = "15";

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

            Set<Sensor> sensors = new HashSet<>();
            Set<Beacon> beacons = new HashSet<>();

            for(String line : lines ) {
                Pattern p = Pattern.compile("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)");
                Matcher m = p.matcher(line);
                m.find();
                Beacon beacon = new Beacon(Long.parseLong(m.group(3)), Long.parseLong(m.group(4)));
                beacons.add(beacon);
                Long manhattanDistance = Math.abs(Long.parseLong(m.group(1)) - Long.parseLong(m.group(3))) + Math.abs(Long.parseLong(m.group(2)) - Long.parseLong(m.group(4)));
                Sensor sensor = new Sensor(Long.parseLong(m.group(1)), Long.parseLong(m.group(2)), manhattanDistance);
                sensors.add(sensor);
            }

            long minIndex = Long.MAX_VALUE;
            long maxIndex = Long.MIN_VALUE;
            long largestManhattan = Long.MIN_VALUE;
            for(Sensor s : sensors) {
                minIndex = Math.min(minIndex, s.x);
                maxIndex = Math.max(maxIndex, s.x);
                largestManhattan = Math.max(largestManhattan, s.manhattanDistance);
            }

            for(Beacon b: beacons) {
                minIndex = Math.min(minIndex, b.x);
                maxIndex = Math.max(maxIndex, b.x);
            }

            minIndex-=largestManhattan;
            maxIndex+=largestManhattan;

            int count = 0;
            // long y = 10;
            long y = 2000000;

            System.out.println("MIN " + minIndex + " MAX " + maxIndex);

            for(long x=minIndex; x<=maxIndex; x++) {
                for(Sensor s : sensors) {
                    Long sensorDistanceToSpot = Math.abs(y-s.y) + Math.abs(x-s.x);
                    // System.out.println("X: " + x + " has distance " + sensorDistanceToSpot + " from sensor at location " + s.x + "," + s.y + " with closet beacon distance of " + s.manhattanDistance);
                    if(sensorDistanceToSpot <= s.manhattanDistance && !beacons.contains(new Beacon(x, y))) {
                        // System.out.println("X: " + x + " seen by sensor with x="+s.x + " y=" + s.y + " distance to closest sensor=" + s.manhattanDistance);
                        count++;
                        break;
                    }
                }
            }

            return count;
        }

        public int calculateP2(List<String> lines) {

            Set<Sensor> sensors = new HashSet<>();
            Set<Beacon> beacons = new HashSet<>();

            for(String line : lines ) {
                Pattern p = Pattern.compile("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)");
                Matcher m = p.matcher(line);
                m.find();
                Beacon beacon = new Beacon(Long.parseLong(m.group(3)), Long.parseLong(m.group(4)));
                beacons.add(beacon);
                Long manhattanDistance = Math.abs(Long.parseLong(m.group(1)) - Long.parseLong(m.group(3))) + Math.abs(Long.parseLong(m.group(2)) - Long.parseLong(m.group(4)));
                Sensor sensor = new Sensor(Long.parseLong(m.group(1)), Long.parseLong(m.group(2)), manhattanDistance);
                sensors.add(sensor);
            }

            for(Sensor s : sensors) {

                for(long i = -(s.manhattanDistance + 1); i<= s.manhattanDistance; i++) {
                    long width = (s.manhattanDistance - Math.abs(i));

                    long tx = (s.x - width - 1);
                    long ty = (s.y + i);
                    if(tx >= 0 && tx <= 4000000 && ty >= 0 && ty <= 4000000 && isCoordOutsideSensors(sensors, tx, ty)) {
                        System.out.println (tx + "," + ty);
                        System.out.println("Frequency: " + BigInteger.valueOf(tx).multiply(BigInteger.valueOf(4000000)).add(BigInteger.valueOf((ty))));

                    }

                    tx = (s.x + width + 1);
                    if(tx >= 0 && tx <= 4000000 && ty >= 0 && ty <= 4000000 && isCoordOutsideSensors(sensors, tx, ty)) {
                        System.out.println (tx + "," + ty);
                        System.out.println("Frequency: " + BigInteger.valueOf(tx).multiply(BigInteger.valueOf(4000000)).add(BigInteger.valueOf((ty))));
                    }
                }
            }

            return -1;
        }

        public boolean isCoordOutsideSensors(Set<Sensor> sensors, long x, long y) {
            return sensors.stream().allMatch(s -> Math.abs(s.x - x) + Math.abs(s.y - y) > s.manhattanDistance);
        }

        public static class Sensor {
            public final Long x;
            public final Long y;
            public final Long manhattanDistance;

            public Sensor(Long x, Long y, Long manhattanDistance) {
                this.x = x;
                this.y = y;

                this.manhattanDistance = manhattanDistance;//Math.abs(sensorX-beaconX) + Math.abs(sensorY-beaconY);
            }
        }

        public static class Beacon {
            public final Long x;
            public final Long y;

            public Beacon(Long x, Long y) {
                this.x = x;
                this.y = y;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
                Beacon beacon = (Beacon) o;
                return Objects.equals(x, beacon.x) && Objects.equals(y, beacon.y);
            }

            @Override
            public int hashCode() {
                return Objects.hash(x, y);
            }
        }
    }
}
