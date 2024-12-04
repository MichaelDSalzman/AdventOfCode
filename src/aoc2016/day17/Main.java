package aoc2016.day17;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import org.apache.commons.codec.digest.DigestUtils;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2016";
        String day = "17";

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

        public String calculateP1(List<String> lines) {

            return findShortestPath("vwbaicqe", true);
        }

        public int calculateP2(List<String> lines) {
            return findShortestPath("vwbaicqe", false).length();
        }

        private String findShortestPath(String hash, boolean stopWhenReachingBottom) {
            Queue<String> paths = new ArrayDeque<>();
            String inititalLocation = "00";
            paths.add(inititalLocation);

            List<String> foundPaths = new ArrayList<>();

            while(!paths.isEmpty()) {
                String path = paths.remove();
                if(path.startsWith("33")) {
                    foundPaths.add(path.substring(2));
                    // System.out.println(path);
                    if(stopWhenReachingBottom) {
                        return path.substring(2);
                    }
                    continue;
                }

                int x = Integer.parseInt(path.substring(0,1));
                int y = Integer.parseInt(path.substring(1,2));
                String pathHistory = path.length() > 2 ? path.substring(2) : "";

                String hashWithPath = hash + pathHistory;
                if(canGoUp(hashWithPath) && y!=0) {
                    paths.add(String.valueOf(x) + (y-1) + pathHistory + "U");
                }

                if(canGoDown(hashWithPath) && y!=3) {
                    paths.add(String.valueOf(x) + (y+1) + pathHistory + "D");
                }

                if(canGoLeft(hashWithPath) && x!=0) {
                    paths.add((x-1) + String.valueOf(y) + pathHistory + "L");
                }

                if(canGoRight(hashWithPath) && x!=3) {
                    paths.add((x+1) + String.valueOf(y) + pathHistory + "R");
                }
            }

            foundPaths = foundPaths.stream().sorted(Comparator.comparingInt(String::length)).toList();
            return foundPaths.get(foundPaths.size() - 1);
        }

        private boolean canGoUp(String hash) {
            char c = DigestUtils.md5Hex(hash).charAt(0);
            return c == 'b' || c == 'c' || c == 'd' || c == 'e' || c == 'f';
        }

        private boolean canGoDown(String hash) {
            char c = DigestUtils.md5Hex(hash).charAt(1);
            return c == 'b' || c == 'c' || c == 'd' || c == 'e' || c == 'f';
        }

        private boolean canGoLeft(String hash) {
            char c = DigestUtils.md5Hex(hash).charAt(2);
            return c == 'b' || c == 'c' || c == 'd' || c == 'e' || c == 'f';
        }

        private boolean canGoRight(String hash) {
            char c = DigestUtils.md5Hex(hash).charAt(3);
            return c == 'b' || c == 'c' || c == 'd' || c == 'e' || c == 'f';
        }
    }
}
