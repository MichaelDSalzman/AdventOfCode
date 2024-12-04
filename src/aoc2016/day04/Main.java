package aoc2016.day04;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2016";
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
            int sum = 0;
            Pattern p = Pattern.compile("(.*)-(\\d+)\\[(.*)\\]");
            for(String line : lines) {
                Matcher m = p.matcher(line);
                Map<Character, Integer> charMap = new HashMap<>();
                if(m.find()) {
                    char[] characters = m.group(1).replaceAll("-", "").toCharArray();
                    for(char character : characters) {
                        Integer count = charMap.get(character);
                        if(count == null) {
                            count = 0;
                        }
                        charMap.put(character, ++count);
                    }

                    Map<Integer, List<Character>> charMapReversed = new HashMap<>();
                    for(Character character : charMap.keySet()) {
                        List<Character> characterList = charMapReversed.get(charMap.get(character));
                        if(characterList == null) {
                            characterList = new ArrayList<>();
                        }
                        characterList.add(character);
                        charMapReversed.put(charMap.get(character), characterList);
                    }

                    StringBuilder builder = new StringBuilder();
                    List<Integer> sortedQuantity = new ArrayList<>(charMapReversed.keySet().stream().sorted().toList());

                    Collections.reverse(sortedQuantity);
                    for(Integer quantity : sortedQuantity) {
                        List<Character> characterList = charMapReversed.get(quantity);
                        Collections.sort(characterList);
                        characterList.forEach(builder::append);
                    }

                    String expectedChecksum = builder.toString().substring(0, 5);
                    if(expectedChecksum.equals(m.group(3))) {
                        sum+=Integer.parseInt(m.group(2));
                    }
                }
            }
            return sum;
        }

        public int calculateP2(List<String> lines) {

            Pattern p = Pattern.compile("(.*)-(\\d+)\\[(.*)\\]");
            for(String line : lines) {
                Matcher m = p.matcher(line);
                Map<Character, Integer> charMap = new HashMap<>();
                if(m.find()) {
                    char[] characters = m.group(1).replaceAll("-", "").toCharArray();
                    for(char character : characters) {
                        Integer count = charMap.get(character);
                        if(count == null) {
                            count = 0;
                        }
                        charMap.put(character, ++count);
                    }

                    Map<Integer, List<Character>> charMapReversed = new HashMap<>();
                    for(Character character : charMap.keySet()) {
                        List<Character> characterList = charMapReversed.get(charMap.get(character));
                        if(characterList == null) {
                            characterList = new ArrayList<>();
                        }
                        characterList.add(character);
                        charMapReversed.put(charMap.get(character), characterList);
                    }

                    StringBuilder builder = new StringBuilder();
                    List<Integer> sortedQuantity = new ArrayList<>(charMapReversed.keySet().stream().sorted().toList());

                    Collections.reverse(sortedQuantity);
                    for(Integer quantity : sortedQuantity) {
                        List<Character> characterList = charMapReversed.get(quantity);
                        Collections.sort(characterList);
                        characterList.forEach(builder::append);
                    }

                    String expectedChecksum = builder.toString().substring(0, 5);
                    if(expectedChecksum.equals(m.group(3))) {
                        decypher(m.group(1), Integer.parseInt(m.group(2)));
                    }
                }
            }
            return -1;
        }

        private void decypher(String code, int shift) {
            int newShift = shift % 26;
            StringBuilder builder = new StringBuilder();
            for(Character character : code.toCharArray()) {
                if(character.equals('-')) {
                    builder.append(" ");
                } else {
                    char newCharacter = (char) (character + newShift);
                    if(newCharacter > 122) {
                        newCharacter -= 26;
                    }
                    builder.append(newCharacter);
                }
            }
            if(builder.toString().toLowerCase().contains("north")) {
                System.out.printf("%s %s%n", builder, shift);
            }
        }
    }
}
