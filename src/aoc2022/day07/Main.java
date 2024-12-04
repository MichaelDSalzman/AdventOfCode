package aoc2022.day07;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String day = "07";

        // FileReader fileReader = new FileReader("src/aoc2022/day" + day + "/sample.txt");
        FileReader fileReader = new FileReader("src/aoc2022/day" + day + "/input.txt");
        List<String> lines = fileReader.getLines();

        Problem problem = new Problem();
        System.out.println ("P1 : " + problem.calculateP1(lines));
        System.out.println ("P2 : " + problem.calculateP2(lines));
    }

    public static class Problem {

        public int calculateP1(List<String> lines) {

            Map<String, Integer> directoryToSize = getDirectorySizes(lines);
            int total = 0;
            for(String dir : directoryToSize.keySet()) {
                if(directoryToSize.get(dir) <= 100000) {
                    total += directoryToSize.get(dir);
                }
            }
            return total;
        }

        private Map<String, Integer> getDirectorySizes(List<String> lines) {

            Map<String, Integer> directoryToSize = new HashMap<>();
            String currentDir = "";

            for (String line : lines) {
                if(line.startsWith("$")) {
                    if(line.startsWith("$ cd")) {
                        String dir = line.split(" ")[2];
                        if(dir.equals("..")) {
                            currentDir = currentDir.substring(0, currentDir.lastIndexOf("/"));
                        } else if(dir.equals("/")) {
                            currentDir = "/";
                        } else {
                            if(!currentDir.endsWith("/")) {
                                currentDir += "/";
                            }
                            currentDir += dir ;
                        }
                    } else if (line.startsWith("$ ls")) {
                        // Needed?
                    }
                } else if(line.startsWith("dir")) {

                } else {
                    String[] parts = line.split(" ");
                    Integer size = Integer.parseInt(parts[0]);

                    String directory = currentDir;
                    while(!directory.isEmpty()) {
                        Integer currentSize = directoryToSize.get(directory);
                        if (currentSize == null) {
                            currentSize = 0;
                        }
                        directoryToSize.put(directory, currentSize + size);

                        if(directory.equals("/")) {
                            directory = "";
                        } else {
                            directory = directory.substring(0, directory.lastIndexOf("/"));
                            if(directory.isEmpty()) {
                                directory = "/";
                            }
                        }
                    }
                }
            }

            return directoryToSize;
        }

        public int calculateP2(List<String> lines) {
            Map<String, Integer> directoryToSize = getDirectorySizes(lines);
            int totalSpace = directoryToSize.get("/");
            int freeSpace = 70000000 - totalSpace;
            int neededSpace = 30000000 - freeSpace;

            Integer smallestSizeToDelete = Integer.MAX_VALUE;
            for(String dir : directoryToSize.keySet()) {
                if(directoryToSize.get(dir) >= neededSpace && directoryToSize.get(dir) < smallestSizeToDelete) {
                    smallestSizeToDelete = directoryToSize.get(dir);
                }
            }

            return smallestSizeToDelete;
        }
    }
}
