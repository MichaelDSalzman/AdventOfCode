package aoc2022.day13;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String day = "13";

        // FileReader fileReader = new FileReader("src/aoc2022/day" + day + "/sample.txt");
        FileReader fileReader = new FileReader("src/aoc2022/day" + day + "/input.txt");
        List<String> lines = fileReader.getLines();

        Problem problem = new Problem();
        System.out.println ("P1 : " + problem.calculateP1(lines));
        System.out.println ("P2 : " + problem.calculateP2(lines));
    }

    public static class Problem {

        public int calculateP1(List<String> lines) {
            int total = 0;
            int counter = 1;
            JSONArray left = null;
            JSONArray right = null;

            for(String line : lines) {
                if(line.isEmpty()) {
                    counter++;
                    left = null;
                    right = null;
                    continue;
                }

                if(left == null) {
                    left = new JSONArray(line);
                } else if(right == null) {
                    right = new JSONArray(line);

                    if(compare(left.toList(), right.toList()) == -1) {
                        total+=counter;
                    }
                }
            }

            return total;
        }

        public int calculateP2(List<String> lines) {
            List<StrangeObject> strangeObjects = new ArrayList<>();
            StrangeObject s1 = new StrangeObject("[[2]]");
            StrangeObject s2 = new StrangeObject("[[6]]");
            strangeObjects.add(s1);
            strangeObjects.add(s2);

            for(String line : lines) {
                if(line.isEmpty()) {
                    continue;
                }

                strangeObjects.add(new StrangeObject(line));
            }

            Collections.sort(strangeObjects);

            return (strangeObjects.indexOf(s1) + 1) * (strangeObjects.indexOf(s2) + 1);
        }

        public static int compare(List<Object> left, List<Object> right) {
            if(left.isEmpty() && !right.isEmpty()) {
                return -1;
            }

            for(int i=0; i<left.size(); i++) {
                if(right.size() < i + 1) {
                    return 1;
                }
                Object leftObject = left.get(i);
                Object rightObject = right.get(i);

                if(leftObject instanceof Integer && rightObject instanceof Integer) {
                    if((Integer) leftObject < (Integer) rightObject) {
                        return -1;
                    } else if ((Integer) leftObject > (Integer) rightObject) {
                        return 1;
                    }
                } else if (leftObject instanceof List && rightObject instanceof List) {
                    int compare = compare((List<Object>) leftObject, (List<Object>) rightObject);
                    if(compare != 0) {
                        return compare;
                    }
                } else {
                    List convertedLeft = leftObject instanceof Integer ? Arrays.asList(leftObject) :
                        (List) leftObject;

                    List convertedRight = rightObject instanceof Integer ? Arrays.asList(rightObject) :
                        (List) rightObject;

                    int compare = compare(convertedLeft, convertedRight);
                    if(compare != 0) {
                        return compare;
                    }
                }

                //The case where we're iterating over arrays and have reached the end of left and right still has more
                if(i == left.size() - 1 && right.size() > left.size()) {
                    return -1;
                }
            }
            return 0;
        }
    }

    public static class StrangeObject implements Comparable<StrangeObject> {

        private final String value;

        public StrangeObject(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        @Override
        public int compareTo(StrangeObject o) {
            return Main.Problem.compare(new JSONArray(this.getValue()).toList(), new JSONArray(o.getValue()).toList());
        }

        @Override
        public String toString() {
            return "StrangeObject{" +
                "value=" + value +
                '}';
        }
    }
}
