package aoc2018.day04;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

            Map<String, int[]> guardShifts = getGuardSleepSchedules(lines);

            int mostTimesAsleep = guardShifts.values().stream().map(i -> Arrays.stream(i).sum()).max(Integer::compare).orElseThrow();
            String guardWhoSleptMost = guardShifts.keySet().stream().filter(guard -> Arrays.stream(guardShifts.get(guard)).sum() == mostTimesAsleep).findFirst().orElseThrow();

            int[] guardSleepSchedule = guardShifts.get(guardWhoSleptMost);
            int minuteSleptMost = 0;
            for(int i=0; i<guardSleepSchedule.length; i++) {
                if(guardSleepSchedule[i] > guardSleepSchedule[minuteSleptMost]) {
                    minuteSleptMost = i;
                }
            }
            return minuteSleptMost * Integer.parseInt(guardWhoSleptMost);
        }

        public Object calculateP2(List<String> lines) {
            Map<String, int[]> guardShifts = getGuardSleepSchedules(lines);

            String guardName = null;
            int minuteSleptMost = 0;

            for(String name : guardShifts.keySet()) {
                if(guardName == null) {
                    guardName = name;
                }
                int[] sleepSchedule = guardShifts.get(name);

                for(int i=0; i<sleepSchedule.length; i++) {
                    if(sleepSchedule[i] > guardShifts.get(guardName)[minuteSleptMost]) {
                        minuteSleptMost = i;
                        guardName = name;
                    }
                }
            }

            return Integer.parseInt(guardName) * minuteSleptMost;
        }

        private Map<String, int[]> getGuardSleepSchedules(List<String> lines) {
            List<String> sorted = lines.stream().sorted().toList();

            Map<String, int[]> guardShifts = new HashMap<>();
            String activeGuard = null;
            int[] activeGuardSleepSchedule = null;
            int minuteFallingAsleep = -1;

            for(String line : sorted) {
                if(line.contains("begins shift")) {
                    Pattern p = Pattern.compile(".*#(\\d+) begins shift");
                    Matcher m = p.matcher(line);
                    if(m.find()) {
                        activeGuard = m.group(1);
                        activeGuardSleepSchedule = guardShifts.getOrDefault(activeGuard, new int[60]);
                        guardShifts.put(activeGuard, activeGuardSleepSchedule);
                    }
                } else if (line.contains("falls asleep")) {
                    Pattern p = Pattern.compile(".* \\d\\d:(\\d\\d).*");
                    Matcher m = p.matcher(line);
                    if(m.find()) {
                        minuteFallingAsleep = Integer.parseInt(m.group(1));
                    }
                } else if (line.contains("wakes up")) {
                    Pattern p = Pattern.compile(".* \\d\\d:(\\d\\d).*");
                    Matcher m = p.matcher(line);
                    if(m.find()) {
                        int minuteWaking = Integer.parseInt(m.group(1));
                        for(int i=minuteFallingAsleep; i<minuteWaking; i++) {
                            guardShifts.get(activeGuard)[i]++;
                        }
                    }
                }
            }

            return guardShifts;
        }
    }
}
