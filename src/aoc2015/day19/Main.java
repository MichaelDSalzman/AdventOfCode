package aoc2015.day19;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2015";
        String day = "19";

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

            Map<String, List<String>> replacementRules = new HashMap<>();
            Pattern p = Pattern.compile("(.*) => (.*)");
            String molecule = null;

            for (String line : lines) {
                Matcher m = p.matcher(line);
                if (m.find()) {
                    String origin = m.group(1);
                    String destination = m.group(2);

                    List<String> replacements = Objects.requireNonNullElse(
                        replacementRules.get(origin), new ArrayList<>());

                    replacements.add(destination);
                    replacementRules.put(origin, replacements);
                } else if (!line.isEmpty()) {
                    molecule = line;
                }
            }

            Set<String> distinctMolecules = new HashSet<>();

            for (int i = 0; i < molecule.length(); i++) {
                for (String replacement : replacementRules.keySet()) {
                    if (i + replacement.length() <= molecule.length() && molecule.substring(i,
                        i + (replacement.length())).equals(replacement)) {
                        for (String toReplace : replacementRules.get(replacement)) {
                            String newMolecule = molecule.substring(0, i) + toReplace
                                + molecule.substring(i + replacement.length());
                            distinctMolecules.add(newMolecule);
                        }
                    }
                }
            }

            return distinctMolecules.size();
        }

        public int calculateP2(List<String> lines) {

            Map<String, List<String>> replacementRules = new HashMap<>();
            Pattern p = Pattern.compile("(.*) => (.*)");

            Set<String> distinctMolecules = new HashSet<>();
            String originalMolecule = null;

            for (String line : lines) {
                Matcher m = p.matcher(line);
                if (m.find()) {
                    String origin = m.group(2);
                    String destination = m.group(1);

                    List<String> replacements = Objects.requireNonNullElse(
                        replacementRules.get(origin), new ArrayList<>());

                    replacements.add(destination);
                    replacementRules.put(origin, replacements);
                } else if (!line.isEmpty()) {
                    distinctMolecules.add(line);
                    originalMolecule = line;
                }
            }

            int count = 0;

            Set<String> foundMolecules = new HashSet<>();
            foundMolecules.add(originalMolecule);
            while (true) {
                distinctMolecules = foundMolecules;
                foundMolecules = new HashSet<>();
                count++;
                int setSize = 1;
                List<String> smallerList = distinctMolecules.stream().sorted(Comparator.comparingInt(String::length)).toList().subList(0, Math.min(setSize, distinctMolecules.size()));
                System.out.println("COUNT " + count + " SMALLEST LENGTH " + smallerList.get(0).length());

                for (String molecule : smallerList) {
                    for (int i = 0; i < molecule.length(); i++) {
                        for (String replacement : replacementRules.keySet()) {
                            if (i + replacement.length() <= molecule.length() && molecule.substring(
                                i,
                                i + (replacement.length())).equals(replacement)) {
                                String newMolecule = molecule.substring(0, i)
                                    + replacementRules.get(replacement).get(0)
                                    + molecule.substring(i + replacement.length());
                                foundMolecules.add(newMolecule);

                                if (newMolecule.equals("e")) {
                                    return count;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
