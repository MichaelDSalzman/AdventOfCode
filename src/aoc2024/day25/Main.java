package aoc2024.day25;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
            return findCombinations(lines);
        }

        public Object calculateP2(List<String> lines) {
            return -1;
        }

        private long findCombinations(List<String> lines) {
            List<Integer[]> keys = new ArrayList<>();
            List<Integer[]> locks = new ArrayList<>();

            int lineIndex = 0;
            boolean isLock = false;
            Integer[] pins = new Integer[5];

            for(String line : lines) {
                if(line.isEmpty()) {
                    if(isLock) {
                        locks.add(pins);
                    } else {
                        keys.add(pins);
                    }
                    pins = new Integer[5];
                    lineIndex = 0;
                    continue;
                } else {
                    if((lineIndex == 0 || lineIndex == 6) && line.equals("#".repeat(5))) {
                        isLock = lineIndex == 0;
                    } else {
                        String[] row = line.split("");
                        for(int i=0; i < row.length; i++) {
                            Integer pin = pins[i];
                            if(pin == null) {
                                pin = 0;
                            }
                            if(row[i].equals("#")) {
                                pin++;
                            }
                            pins[i]= pin;
                        }
                    }
                }
                lineIndex++;
            }

            if(Arrays.stream(pins).allMatch(Objects::nonNull)) {
                if (isLock) {
                    locks.add(pins);
                } else {
                    keys.add(pins);
                }
            }

            long combinations = 0;
            for(Integer[] lock : locks) {
                for(Integer[] key : keys) {
                    boolean keyFits = true;
                    for(int i = 0; i<5; i++) {
                        if (lock[i] + key[i] > 5) {
                            keyFits = false;
                            break;
                        }
                    }
                    if(keyFits) {
                        combinations++;
                    }
                }
            }
            return combinations;
        }
    }
}
