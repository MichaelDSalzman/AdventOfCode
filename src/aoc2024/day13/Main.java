package aoc2024.day13;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.tuple.Pair;
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

        FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/sample.txt");
        // FileReader fileReader = new FileReader("src/aoc" + year + "/day" + day + "/input.txt");
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
            List<Machine> machines = Machine.valueOf(lines, 0);
            return machines.stream().map(Machine::findSolution).filter(Objects::nonNull).map(p -> 3*p.getLeft() + p.getRight()).mapToLong(Long::longValue).sum();
        }

        public Object calculateP2(List<String> lines) {

            List<Machine> machines = Machine.valueOf(lines, 10000000000000L);
            return machines.stream().map(Machine::findSolution).filter(Objects::nonNull).map(p -> 3*p.getLeft() + p.getRight()).mapToLong(Long::longValue).sum();
        }
    }

    record Machine(long prizeX, long prizeY, long buttonAX, long buttonAY, long buttonBX, long buttonBY) {

        public static List<Machine> valueOf(List<String> lines, long prizeOffset) {
            List<Machine> machines = new ArrayList<>();
            Pattern buttonAPattern = Pattern.compile("Button A: X\\+(\\d+), Y\\+(\\d+)");
            Pattern buttonBPattern = Pattern.compile("Button B: X\\+(\\d+), Y\\+(\\d+)");
            Pattern prizePattern = Pattern.compile("Prize: X=(\\d+), Y=(\\d+)");

            long fa1 = 0, fb1 = 0, fa2 = 0, fb2 = 0, s1 = 0, s2 = 0;
            for(String line : lines) {
                Matcher buttonAMatcher = buttonAPattern.matcher(line);
                Matcher buttonBMatcher = buttonBPattern.matcher(line);
                Matcher prizeMatcher = prizePattern.matcher(line);
                if(buttonAMatcher.find()) {
                    fa1 = Long.parseLong(buttonAMatcher.group(1));
                    fa2 = Long.parseLong(buttonAMatcher.group(2));
                } else if (buttonBMatcher.find()) {
                    fb1 = Long.parseLong(buttonBMatcher.group(1));
                    fb2 = Long.parseLong(buttonBMatcher.group(2));
                } else if(prizeMatcher.find()) {
                    s1 = Long.parseLong(prizeMatcher.group(1)) + prizeOffset;
                    s2 = Long.parseLong(prizeMatcher.group(2)) + prizeOffset;

                    machines.add(new Machine(s1, s2, fa1, fa2, fb1, fb2));
                }
            }

            return machines;
        }

        Pair<Long, Long> findSolution() {

            Pair<Long, Long> pairFromSolvingSequence = null;
            long a = (prizeX *buttonBY - prizeY *buttonBX) / (buttonAX *buttonBY -
                buttonAY *buttonBX);
            long b = (prizeX - (buttonAX*a)) / buttonBX;

            if(a>0 && b>0 && (buttonAX*a + buttonBX*b == prizeX) && (buttonAY*a + buttonBY*b == prizeY)) {
                pairFromSolvingSequence = Pair.of(a, b);
            }

            return pairFromSolvingSequence;
        }
    }
}
