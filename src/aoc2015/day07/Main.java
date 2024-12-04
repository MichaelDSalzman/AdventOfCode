package aoc2015.day07;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2015";
        String day = "07";

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
            Map<String, Integer> wireValues = new HashMap<>();
            parseFile(lines, wireValues);
            return wireValues.get("a");
        }

        public int calculateP2(List<String> lines) {
            Map<String, Integer> wireValues = new HashMap<>();
            parseFile(lines, wireValues);
            Integer wireA = wireValues.get("a");

            wireValues.clear();
            wireValues.put("b", wireA);
            parseFile(lines, wireValues);
            return wireValues.get("a");
        }

        private void parseFile(List<String> lines, Map<String, Integer> wireValues) {
            Collections.shuffle(lines);
            boolean unknownWireFound = true;
            while(unknownWireFound) {
                unknownWireFound = false;
                System.out.println(wireValues.size());

                for (String line : lines) {
                    String[] parts = line.split("\\s+");
                    if (parts.length == 3 && parts[1].equals("->")) {
                        Integer value = null;
                        if(Pattern.compile("\\d+").matcher(parts[0]).find()) {
                            value = Integer.parseInt(parts[0]);
                        } else {
                            value = wireValues.get(parts[0]);
                            if(value == null) {
                                unknownWireFound = true;
                                continue;
                            }
                        }
                        String destination = parts[2];
                        wireValues.putIfAbsent(destination, value);
                    } else if (parts.length == 4 && parts[0].equals("NOT")) {
                        Integer value = wireValues.get(parts[1]);
                        if(value == null) {
                            unknownWireFound = true;
                            continue;
                        }
                        String destination = parts[3];
                        wireValues.put(destination, ~value);
                    } else if (parts.length == 5 && parts[3].equals("->")) {
                        if (parts[1].equals("AND")) {
                            Integer in1 = null;
                            Integer in2 = null;
                            if(Pattern.compile("\\d+").matcher(parts[0]).find()) {
                                in1 = Integer.parseInt(parts[0]);
                            } else {
                                in1 = wireValues.get(parts[0]);
                            }
                            if(Pattern.compile("\\d+").matcher(parts[2]).find()) {
                                in2 = Integer.parseInt(parts[2]);
                            } else {
                                in2 = wireValues.get(parts[2]);
                            }
                            if(in1 == null || in2 == null) {
                                unknownWireFound = true;
                                continue;
                            }
                            String destination = parts[4];
                            wireValues.put(destination, in1 & in2);
                        } else if (parts[1].equals("OR")) {
                            Integer in1 = null;
                            Integer in2 = null;
                            if(Pattern.compile("\\d+").matcher(parts[0]).find()) {
                                in1 = Integer.parseInt(parts[0]);
                            } else {
                                in1 = wireValues.get(parts[0]);
                            }
                            if(Pattern.compile("\\d+").matcher(parts[2]).find()) {
                                in2 = Integer.parseInt(parts[2]);
                            } else {
                                in2 = wireValues.get(parts[2]);
                            }
                            if(in1 == null || in2 == null) {
                                unknownWireFound = true;
                                continue;
                            }
                            String destination = parts[4];
                            wireValues.put(destination, in1 | in2);
                        } else if (parts[1].equals("LSHIFT")) {
                            Integer in1 = wireValues.get(parts[0]);
                            if(in1 == null) {
                                unknownWireFound = true;
                                continue;
                            }
                            int bitLength = Integer.parseInt(parts[2]);
                            String destination = parts[4];
                            wireValues.put(destination, in1 << bitLength);
                        } else if (parts[1].equals("RSHIFT")) {
                            Integer in1 = wireValues.get(parts[0]);
                            if(in1 == null) {
                                unknownWireFound = true;
                                continue;
                            }
                            int bitLength = Integer.parseInt(parts[2]);
                            String destination = parts[4];
                            wireValues.put(destination, in1 >> bitLength);
                        } else {
                            throw new RuntimeException("UNKNOWN " + line);
                        }
                    }
                }
            }

            for(String wireName : wireValues.keySet()) {
                int value = wireValues.get(wireName);
                value &= 0b00000000000000001111111111111111;
                wireValues.put(wireName, value);
            }
        }
    }
}
