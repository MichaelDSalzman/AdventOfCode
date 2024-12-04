package aoc2016.day14;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.digest.DigestUtils;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2016";
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

        public static String INPUT = "ihaygndm";
        public static String SAMPLE = "abc";

        public int calculateP1(List<String> lines) {
            return find64Keys(INPUT, false);
            // return find64Keys(SAMPLE, false);
        }


        public int calculateP2(List<String> lines) {
            return find64Keys(INPUT, true);
            // return find64Keys(SAMPLE, true);
        }

        private int find64Keys(String input, boolean stretchHash) {
            int keysFound = 0;
            Map<Integer, String> potentialKeys = new HashMap<>();
            List<Integer> foundKeys = new ArrayList<>();
            int count = 0;
            int largestKey = 0;

            Pattern tripleP = Pattern.compile("(.)\\1\\1");
            while(foundKeys.size() < 64 && potentialKeys.size() >= 0) {
                String toHash = input + count;
                String hash = DigestUtils.md5Hex(toHash);
                if(stretchHash) {
                    for(int i=0; i<2016; i++) {
                        hash = DigestUtils.md5Hex(hash);
                    }
                }

                List<Integer> keysToRemove = new ArrayList<>();
                List<Integer> keyStartSorted = potentialKeys.keySet().stream().sorted().toList();

                for (Integer keyStart : keyStartSorted) {
                    String keyCharacter = potentialKeys.get(keyStart);
                    if (count - keyStart < 1000) {
                        if (hash.contains(
                            keyCharacter + keyCharacter + keyCharacter + keyCharacter
                                + keyCharacter)) {
                            foundKeys.add(keyStart);
                            Collections.sort(foundKeys);
                            System.out.println(foundKeys.size() + " - FOUND KEY " + keyCharacter + " - " + keyStart + " -> " + count + " DIFF=" + (count-keyStart) + " HASH " + hash);
                            keysToRemove.add(keyStart);
                        }
                    } else {
                        keysToRemove.add(keyStart);
                    }
                }

                for (Integer keyToRemove : keysToRemove) {
                    potentialKeys.remove(keyToRemove);
                }

                if(foundKeys.size() < 64) {
                    Matcher m = tripleP.matcher(hash);
                    if (m.find()) {
                        String triplet = m.group();
                        potentialKeys.put(count, triplet.substring(0, 1));
                    }
                }
                count++;
            }

            return foundKeys.get(63);
        }

    }
}
