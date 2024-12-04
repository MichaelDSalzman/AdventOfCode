package aoc2017.day14;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2017";
        String day = "14";

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

        public int calculateP1(List<String> lines) {

            // return getSquareCount("flqrgnkx");
            return getSquareCount("uugsqrei");
        }

        public int calculateP2(List<String> lines) {
            // return getGroupCount("flqrgnkx");
            return getGroupCount("uugsqrei");
        }

        private int getSquareCount(String input) {
            int count = 0;
            for(int i=0; i<128; i++) {
                String hash = aoc2017.day10.Main.Problem.generateKnotHash(input + "-" + i);

                for(char c : hash.toCharArray()) {
                    count += StringUtils.countMatches(Integer.toBinaryString(Integer.parseInt(String.valueOf(c), 16)), '1');
                }
            }

            return count;
        }

        private int getGroupCount(String input) {
            List<List<Character>> grid = new ArrayList<>();

            for(int i=0; i<128; i++) {
                String hash = aoc2017.day10.Main.Problem.generateKnotHash(input + "-" + i);

                StringBuilder sb = new StringBuilder();
                for(char c : hash.toCharArray()) {
                    sb.append(StringUtils.leftPad(Integer.toBinaryString(Integer.parseInt(String.valueOf(c), 16)), 4, '0'));
                }
                grid.add(sb.toString().chars().mapToObj(c->(char)c).toList());
            }

            Set<String> seenSpots = new HashSet<>();
            int groupCount = 0;

            for(int i=0; i<grid.size(); i++) {
                List<Character> characterList = grid.get(i);

                for(int j=0; j<characterList.size(); j++) {
                    if(characterList.get(j).equals('1') && !seenSpots.contains(i + "," + j)) {
                        groupCount++;
                        Queue<String> queue = new ArrayDeque<>();
                        queue.add(i+","+j);

                        while(!queue.isEmpty()) {
                            String spot = queue.remove();
                            if(seenSpots.contains(spot)) {
                                continue;
                            }
                            seenSpots.add(spot);
                            String[] split = spot.split(",");
                            int x = Integer.parseInt(split[0]);
                            int y = Integer.parseInt(split[1]);
                            if(x >= 0 && y >=0 && x<grid.size() && y<grid.size() && grid.get(x).get(y) == '1') {
                                queue.add((x - 1) + "," + y);
                                queue.add((x + 1) + "," + y);
                                queue.add(x + "," + (y + 1));
                                queue.add(x + "," + (y - 1));
                            }
                        }
                    }
                }
            }

            return groupCount;
        }
    }
}
