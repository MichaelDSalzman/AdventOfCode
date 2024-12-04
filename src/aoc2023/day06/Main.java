package aoc2023.day06;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2023";
        String day = "06";

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
            Pattern numberP = Pattern.compile("(\\d+)");
            Matcher m = numberP.matcher(lines.get(0));

            List<Integer> times = new ArrayList<>();
            while (m.find()) {
                times.add(Integer.parseInt(m.group()));
            }

            List<Integer> distances = new ArrayList<>();
            m = numberP.matcher(lines.get(1));
            while (m.find()) {
                distances.add(Integer.parseInt(m.group()));
            }

            int winnerProduct = 1;
            for (int raceNumber = 0; raceNumber < times.size(); raceNumber++) {
                Integer distanceToBeat = distances.get(raceNumber);
                Integer raceTime = times.get(raceNumber);
                int losers = 0;
                for (int timeButtonHeld = 0; timeButtonHeld <= raceTime; timeButtonHeld++) {
                    Integer distanceBoatWent = timeButtonHeld * (raceTime - timeButtonHeld);
                    if (distanceBoatWent <= distanceToBeat) {
                        losers++;
                    } else {
                        break;
                    }
                }

                int numberOfWinners = (raceTime + 1) - (2 * losers);
                winnerProduct *= numberOfWinners;
            }

            return winnerProduct;
        }

        public long calculateP2(List<String> lines) {
            Pattern numberP = Pattern.compile("(\\d+)");
            Matcher m = numberP.matcher(lines.get(0));

            StringBuilder sb = new StringBuilder();
            while (m.find()) {
                sb.append(m.group());
            }

            Long raceTime = Long.parseLong(sb.toString());

            sb = new StringBuilder();
            m = numberP.matcher(lines.get(1));
            while (m.find()) {
                sb.append(m.group());
            }
            Long distanceToBeat = Long.parseLong(sb.toString());

            int losers = 0;
            for (int timeButtonHeld = 0; timeButtonHeld <= raceTime; timeButtonHeld++) {
                Long distanceBoatWent = timeButtonHeld * (raceTime - timeButtonHeld);
                if (distanceBoatWent <= distanceToBeat) {
                    losers++;
                } else {
                    break;
                }
            }

            long numberOfWinners = (raceTime + 1) - (2 * losers);


            return numberOfWinners;
        }
    }
}
