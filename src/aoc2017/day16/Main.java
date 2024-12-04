package aoc2017.day16;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2017";
        String day = "16";

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

        public String calculateP1(List<String> lines) {
            return dance(16, List.of(lines.get(0).split(",")), 1);
        }

        public String calculateP2(List<String> lines) {

            // return dance(5, List.of(lines.get(0).split(",")), 1000000000);
            return dance(16, List.of(lines.get(0).split(",")), 1000000000);
        }

        private String dance(int numPrograms, List<String> instructions, int numIterations) {
            List<Character> programs = new ArrayList<>();
            List<Character> original = new ArrayList<>();
            for(int i=0; i<numPrograms; i++) {
                char c = (char)(97+i);
                programs.add(c);
                original.add(c);
            }


            Map<String, Integer> seenStates = new HashMap<>();
            int loopdiff = -1;
            for(int iter=0; iter< numIterations; iter++) {
                StringBuilder sb = new StringBuilder();
                programs.forEach(sb::append);
                if(seenStates.containsKey(sb.toString())) {
                    System.out.println("CURRENTLY AT " + iter + " SAW STATE " + sb + " AT INDEX " + seenStates.get(sb.toString()));
                    loopdiff = iter - seenStates.get(sb.toString());
                    break;
                }
                seenStates.put(sb.toString(), iter);
                for (String instruction : instructions) {
                    if (instruction.startsWith("s")) {
                        int num = Integer.parseInt(instruction.substring(1));
                        for (int i = 0; i < num; i++) {
                            programs.add(0, programs.remove(numPrograms - 1));
                        }
                    } else if (instruction.startsWith("x")) {
                        int indexA = Integer.parseInt(instruction.substring(1).split("/")[0]);
                        int indexB = Integer.parseInt(instruction.substring(1).split("/")[1]);
                        char a = programs.get(indexA);
                        char b = programs.get(indexB);
                        programs.set(indexA, b);
                        programs.set(indexB, a);
                    } else if (instruction.startsWith("p")) {
                        char a = instruction.substring(1).split("/")[0].charAt(0);
                        char b = instruction.substring(1).split("/")[1].charAt(0);
                        int indexA = programs.indexOf(a);
                        int indexB = programs.indexOf(b);
                        programs.set(indexA, b);
                        programs.set(indexB, a);
                    }
                }
            }

            if(loopdiff != -1) {
                int offset = numIterations % loopdiff;
                for(String key : seenStates.keySet()) {
                    if(seenStates.get(key).equals(offset)) {
                        return key;
                    }
                }
            }

            StringBuilder sb = new StringBuilder();
            programs.forEach(sb::append);

            return sb.toString();
        }
    }
}
