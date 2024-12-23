package aoc2024.day21;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.collections4.map.MultiKeyMap;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = null;
        String day = null;

        Pattern p = Pattern.compile("aoc(\\d+)\\.day(\\d+).*");
        Matcher m = p.matcher(Main.class.getName());
        if(m.find()) {
            year = m.group(1);
            day = m.group(2);
        }

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

        public Object calculateP1(List<String> lines) {
            return solve(lines, 2);
        }

        public Object calculateP2(List<String> lines) {
            return solve(lines, 25);
        }

        private long solve(List<String> lines, int iterations) {

            List<Button> numericButtons = new ArrayList<>();
            numericButtons.add(new Button(0,0,"7"));
            numericButtons.add(new Button(1,0,"8"));
            numericButtons.add(new Button(2,0,"9"));
            numericButtons.add(new Button(0,1,"4"));
            numericButtons.add(new Button(1,1,"5"));
            numericButtons.add(new Button(2,1,"6"));
            numericButtons.add(new Button(0,2,"1"));
            numericButtons.add(new Button(1,2,"2"));
            numericButtons.add(new Button(2,2,"3"));
            numericButtons.add(new Button(1,3, "0"));
            Button numbericAButton = new Button(2,3,"A");
            numericButtons.add(numbericAButton);

            Map<String, String> buttonDirectionCombinations = new HashMap<>();
            for(int i=0; i<numericButtons.size(); i++) {
                for(int j=0; j<numericButtons.size(); j++) {
                    Button start = numericButtons.get(i);
                    Button end = numericButtons.get(j);
                    String instruction = end.value;
                    String path = typeCode(instruction, numericButtons, start);

                    buttonDirectionCombinations.put(start.value + end.value, path);
                }
            }

            List<Button> directionButtons = new ArrayList<>();
            directionButtons.add(new Button(1,0, "^"));
            directionButtons.add(new Button(0, 1, "<"));
            directionButtons.add(new Button(1,1,"v"));
            directionButtons.add(new Button(2,1,">"));
            Button directionAButton = new Button(2,0,"A");
            directionButtons.add(directionAButton);
            for(int i=0; i<directionButtons.size(); i++) {
                for(int j=0; j<directionButtons.size(); j++) {
                    Button start = directionButtons.get(i);
                    Button end = directionButtons.get(j);
                    String instruction = end.value;
                    String path = typeCode(instruction, directionButtons, start);

                    buttonDirectionCombinations.put(start.value + end.value, path);
                }
            }

            long complexity = 0L;
            for(String line : lines) {
                long lengthOfCode = lengthOfCode(line, buttonDirectionCombinations, numbericAButton, iterations+1);

                Matcher numberInCodeMatcher = Pattern.compile("\\D*(\\d+)\\D*").matcher(line);
                if(numberInCodeMatcher.find()) {
                    int numberInCode = Integer.parseInt(numberInCodeMatcher.group(1));
                    long codeComplexity = ((long) numberInCode * lengthOfCode);
                    complexity += codeComplexity;
                }
            }
            return complexity;
        }

        private long lengthOfCode(String code,
                                  Map<String, String> buttonDirectionCombinations,
                                  Button startButton,
                                  int numberOfRobots) {

            Map<String, Long> transitions = new HashMap<>();
            transitions.put(code, 1L);

            for(int i=0; i<numberOfRobots; i++) {
                Map<String, Long> transitionsForRobot = new HashMap<>();
                for(String key : transitions.keySet()) {
                    Long numberOfTimes = transitions.get(key);
                    key = startButton.value + key;
                    String[] chars = key.split("");

                    for(int j=0; j<chars.length-1; j++) {
                        String instruction = chars[j] + chars[j+1];
                        String path = buttonDirectionCombinations.get(instruction);
                        Long count = transitionsForRobot.getOrDefault(path, 0L);
                        count += numberOfTimes;
                        transitionsForRobot.put(path, count);
                    }
                }
                transitions = transitionsForRobot;
            }
            long count = 0;
            for(String key : transitions.keySet()) {
                count += key.length() * transitions.get(key);
            }

            return count;
        }

        private String typeCode(String code, List<Button> buttons, Button startButton) {
            StringBuilder sequence = new StringBuilder();

            Button currentPosition = startButton;

            for(String buttonValue : code.split("")) {
                Button buttonToPush = buttons.stream()
                    .filter(b -> b.value.equals(buttonValue))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No button for " + buttonValue + " in " + buttons));

                while (!buttonToPush.equals(currentPosition)) {

                    int numLeft = currentPosition.horizontal - buttonToPush.horizontal;
                    int numRight = -numLeft;
                    int numUp = currentPosition.vertical - buttonToPush.vertical;
                    int numDown = -numUp;

                    // want to go left before up/down if possible
                    if(numLeft >= 0) {
                        boolean allButtonsExist = true;
                        for(int i=1; i<=numLeft; i++) {
                            int finalI = i;
                            Button finalCurrentPosition = currentPosition;
                            allButtonsExist = allButtonsExist && buttons.stream().anyMatch(b -> b.vertical == finalCurrentPosition.vertical && b.horizontal == finalCurrentPosition.horizontal- finalI);
                        }
                        if(allButtonsExist) {
                            sequence.append("<".repeat(numLeft));
                            if(numUp > 0) {
                                sequence.append("^".repeat(numUp));
                            } else {
                                sequence.append("v".repeat(numDown));
                            }
                        } else {
                            if(numUp > 0) {
                                sequence.append("^".repeat(numUp));
                            } else {
                                sequence.append("v".repeat(numDown));
                            }
                            sequence.append("<".repeat(numLeft));
                        }
                    }
                    // Want to go up/down before right if possible
                    else if(numRight >= 0) {
                        if (numUp >= 0) {
                            boolean allButtonsExist = true;
                            for(int i=1; i<=numUp; i++) {
                                int finalI = i;
                                Button finalCurrentPosition = currentPosition;
                                allButtonsExist = allButtonsExist && buttons.stream().anyMatch(b -> b.horizontal == finalCurrentPosition.horizontal && b.vertical == finalCurrentPosition.vertical - finalI);
                            }
                            if(allButtonsExist) {
                                sequence.append("^".repeat(numUp));
                                sequence.append(">".repeat(numRight));
                            } else {
                                sequence.append(">".repeat(numRight));
                                sequence.append("^".repeat(numUp));
                            }
                        } else if(numDown > 0) {
                            boolean allButtonsExist = true;
                            for(int i=1; i<=numDown; i++) {
                                int finalI = i;
                                Button finalCurrentPosition = currentPosition;
                                allButtonsExist = allButtonsExist && buttons.stream().anyMatch(b -> b.horizontal == finalCurrentPosition.horizontal && b.vertical == finalCurrentPosition.vertical + finalI);
                            }
                            if(allButtonsExist) {
                                sequence.append("v".repeat(numDown));
                                sequence.append(">".repeat(numRight));
                            } else {
                                sequence.append(">".repeat(numRight));
                                sequence.append("v".repeat(numDown));
                            }
                        }
                    }

                    currentPosition = buttonToPush;
                }

                if(currentPosition.equals(buttonToPush)) {
                    sequence.append("A");
                }
            }

            return sequence.toString();
        }

        private Map<String, Integer> solveDirectionPad(String code) {
            for(String string : code.split("")) {

            }

            return null;
        }

        private Map<String, Integer> solveDirectionPad(Map<String, Integer> patternCountMap) {
return null;
        }

        record Button(int horizontal, int vertical, String value) {}
    }
}
