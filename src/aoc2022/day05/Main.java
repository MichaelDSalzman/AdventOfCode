package aoc2022.day05;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String day = "05";

        // util.FileReader fileReader = new util.FileReader("src/aoc2022/day" + day + "/sample.txt");
        FileReader fileReader = new FileReader("src/aoc2022/day" + day + "/input.txt");
        List<String> lines = fileReader.getLines();

        Problem problem = new Problem();
        System.out.println ("P1 : " + problem.calculateP1(lines));
        System.out.println ("P2 : " + problem.calculateP2(lines));
    }

    public static class Problem {

        public String calculateP1(List<String> lines) {

            List<Stack<String>> stacks = new ArrayList<>();

            for(String line:  lines) {
                if(line.contains("[") && line.contains("]")) {
                    Pattern p = Pattern.compile("\\[");
                    Matcher m = p.matcher(line);
                    while(m.find()) {
                        int startingBracketIndex = m.start();
                        int stackNum = startingBracketIndex / 4;
                        String value = String.valueOf(line.charAt(startingBracketIndex+1));

                        for (int i=0; i<=stackNum; i++) {
                            if(stacks.size() <= stackNum) {
                                stacks.add(new Stack<>());
                            }
                        }

                        stacks.get(stackNum).add(value);
                    }
                }

                if(line.isEmpty()) {
                    for(List stack : stacks) {
                        Collections.reverse(stack);
                    }
                }

                if(line.startsWith("move")) {
                    String[] parts = line.split(" ");
                    int quantity = Integer.parseInt(parts[1]);
                    int sourceStack = Integer.parseInt(parts[3]) - 1;
                    int destinationStack = Integer.parseInt(parts[5]) - 1;

                    for(int i = 0; i<quantity; i++) {
                        stacks.get(destinationStack).push(stacks.get(sourceStack).pop());
                    }
                }
            }

            StringBuilder result = new StringBuilder();
            for(Stack<String> stack : stacks) {
                result.append(stack.pop());
            }

            return result.toString();
        }

        public String calculateP2(List<String> lines) {

            List<Stack<String>> stacks = new ArrayList<>();

            for(String line:  lines) {
                if(line.contains("[") && line.contains("]")) {
                    Pattern p = Pattern.compile("\\[");
                    Matcher m = p.matcher(line);
                    while(m.find()) {
                        int startingBracketIndex = m.start();
                        int stackNum = startingBracketIndex / 4;
                        String value = String.valueOf(line.charAt(startingBracketIndex+1));

                        for (int i=0; i<=stackNum; i++) {
                            if(stacks.size() <= stackNum) {
                                stacks.add(new Stack<>());
                            }
                        }

                        stacks.get(stackNum).add(value);
                    }
                }

                if(line.isEmpty()) {
                    for(List stack : stacks) {
                        Collections.reverse(stack);
                    }
                }

                if(line.startsWith("move")) {
                    String[] parts = line.split(" ");
                    int quantity = Integer.parseInt(parts[1]);
                    int sourceStack = Integer.parseInt(parts[3]) - 1;
                    int destinationStack = Integer.parseInt(parts[5]) - 1;

                    Stack<String > tempStack = new Stack<>();
                    for(int i = 0; i<quantity; i++) {
                        tempStack.push(stacks.get(sourceStack).pop());
                    }
                    for(int i = 0; i<quantity; i++) {
                        stacks.get(destinationStack).push(tempStack.pop());
                    }
                }
            }

            StringBuilder result = new StringBuilder();
            for(Stack<String> stack : stacks) {
                result.append(stack.pop());
            }

            return result.toString();
        }
    }
}
