package aoc2017.day12;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2017";
        String day = "12";

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
            return howManyConnected(lines, 0);
        }

        public int calculateP2(List<String> lines) {
            return howManyGroups(lines);
        }

        private int howManyConnected(List<String> lines, int start) {
            Map<Integer, List<Integer>> programConnections = new HashMap<>();

            for(String line : lines) {
                String[] split = line.split("\\s*<->\\s*");
                int programId = Integer.parseInt(split[0]);
                List<Integer> connectedProgramIds = Arrays.stream(split[1].split(",\\s*")).mapToInt(Integer::parseInt).boxed().toList();

                programConnections.put(programId, connectedProgramIds);
            }

            Set<Integer> connectedPrograms = new HashSet<>();
            Queue<Integer> queue = new ArrayDeque<>();
            queue.add(start);
            while(!queue.isEmpty()) {
                Integer connectedProgram = queue.remove();
                if(connectedPrograms.contains(connectedProgram)) {
                    continue;
                }

                connectedPrograms.add(connectedProgram);
                for(Integer neighbor : programConnections.get(connectedProgram)) {
                    queue.add(neighbor);
                }
            }

            return connectedPrograms.size();
        }

        private int howManyGroups(List<String> lines) {
            Map<Integer, List<Integer>> programConnections = new HashMap<>();

            for(String line : lines) {
                String[] split = line.split("\\s*<->\\s*");
                int programId = Integer.parseInt(split[0]);
                List<Integer> connectedProgramIds = Arrays.stream(split[1].split(",\\s*")).mapToInt(Integer::parseInt).boxed().toList();

                programConnections.put(programId, connectedProgramIds);
            }

            Set<Integer> connectedPrograms = new HashSet<>();
            int numGroups = 0;
            while(connectedPrograms.size() != programConnections.keySet().size()) {
                numGroups++;
                Integer nextStart = programConnections.keySet().stream().filter(i -> !connectedPrograms.contains(i)).findFirst().get();

                Queue<Integer> queue = new ArrayDeque<>();
                queue.add(nextStart);
                while (!queue.isEmpty()) {
                    Integer connectedProgram = queue.remove();
                    if (connectedPrograms.contains(connectedProgram)) {
                        continue;
                    }

                    connectedPrograms.add(connectedProgram);
                    for (Integer neighbor : programConnections.get(connectedProgram)) {
                        queue.add(neighbor);
                    }
                }
            }

            return numGroups;
        }
    }
}
