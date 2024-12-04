package aoc2016.day15;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2016";
        String day = "15";

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
            return findTimeToDrop(lines);
        }

        public int calculateP2(List<String> lines) {
            lines.add("Disc #7 has 11 positions; at time=0, it is at position 0.");
            return findTimeToDrop(lines);
        }

        private int findTimeToDrop(List<String> lines) {
            List<Disk> disks = new ArrayList<>();

            Pattern p = Pattern.compile(
                "Disc #(\\d+) has (\\d+) positions; at time=0, it is at position (\\d+).");
            for (String line : lines) {
                Matcher m = p.matcher(line);
                if (m.find()) {
                    disks.add(new Disk(
                        Integer.parseInt(m.group(1)),
                        Integer.parseInt(m.group(2)),
                        Integer.parseInt(m.group(3))
                    ));
                }
            }

            int time = 0;
            while(true) {
                int finalTime = time;
                if(disks.stream().allMatch(d -> positionAtTime(finalTime + d.number, d.numPositions, d.startingPosition) == 0))
                {
                    return time;
                }
                time++;
            }
        }

        private int positionAtTime(int time, int numPositions, int startingPosition) {
            return (time + startingPosition) % numPositions;
        }

        record Disk(int number, int numPositions, int startingPosition) {
        }

        ;
    }
}
