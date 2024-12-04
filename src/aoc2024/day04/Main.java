package aoc2024.day04;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2024";
        String day = "04";

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
            int totalCount = 0;

            totalCount += scan(lines, 0, 1, "XMAS"); // right
            totalCount += scan(lines, 0, -1, "XMAS"); // left
            totalCount += scan(lines, 1, 0, "XMAS"); // down
            totalCount += scan(lines, -1, 0, "XMAS"); // up
            totalCount += scan(lines, 1, 1, "XMAS"); // down-right
            totalCount += scan(lines, 1, -1, "XMAS"); // down-left
            totalCount += scan(lines, -1, 1, "XMAS"); // up-right
            totalCount += scan(lines, -1, -1, "XMAS"); // up-left

            return totalCount;
        }

        public int calculateP2(List<String> lines) {
            int totalCount = 0;
            for(int horizontal = 0; horizontal <= lines.get(0).length() - 3; horizontal++) {
                for(int vertical = 0; vertical <= lines.size() - 3; vertical++) {
                    // Only scan a 3x3 box looking for "MAS" going diagonally
                    List<String> box = new ArrayList<>();
                    box.add(lines.get(vertical).substring(horizontal, horizontal+3));
                    box.add(lines.get(vertical+1).substring(horizontal, horizontal+3));
                    box.add(lines.get(vertical+2).substring(horizontal, horizontal+3));

                    int count = 0;
                    count += scan(box, 1, 1, "MAS"); // down-right
                    count += scan(box, 1, -1, "MAS"); // down-left
                    count += scan(box, -1, 1, "MAS"); // up-right
                    count += scan(box, -1, -1, "MAS"); // up-left
                    // This is an X-MAS if MAS was found twice
                    if(count == 2) {
                        totalCount++;
                    }
                }
            }

            return totalCount;
        }

        public int scan(List<String> lines, int verticalDelta, int horizontalDelta, String word) {
            int lineLength = lines.get(0).length();
            int numLines = lines.size();
            int count = 0;

            for(int verticalIndex=0; verticalIndex<lineLength; verticalIndex++) {
                for (int horizontalIndex = 0; horizontalIndex < numLines; horizontalIndex++) {
                    boolean stillGood = true;
                    for(int w=0; w<word.length() && stillGood; w++) {
                        try {
                            int newVerticalIndex = verticalIndex + (verticalDelta * w);
                            int newHorizontalIndex = horizontalIndex + (horizontalDelta * w);
                            if (newVerticalIndex < 0 || newVerticalIndex > lineLength || newHorizontalIndex < 0 || newHorizontalIndex > numLines) {
                                stillGood = false;
                            } else if (lines.get(newVerticalIndex).charAt(newHorizontalIndex) != word.charAt(w)) {
                                stillGood = false;
                            }
                        } catch(Exception e) {
                            stillGood = false;
                        }
                    }
                    if(stillGood) {
                        count++;
                    }
                }
            }

            return count;
        }
    }
}
