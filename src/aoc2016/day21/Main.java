package aoc2016.day21;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2016";
        String day = "21";

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

            return scramble("abcdefgh", lines);
        }

        public String calculateP2(List<String> lines) {
            return unscramble("fbgdceah", lines);
        }

        private String scramble(String input, List<String> instructions) {
            String output = input;

            Pattern reverseP = Pattern.compile("reverse positions (\\d+) through (\\d+)");
            Pattern rotateP = Pattern.compile("rotate based on position of letter (.)");
            Pattern swapP = Pattern.compile("swap position (\\d+) with position (\\d+)");
            Pattern moveP = Pattern.compile("move position (\\d+) to position (\\d+)");
            Pattern swapLetterP = Pattern.compile("swap letter (.) with letter (.)");
            Pattern rotateRightP = Pattern.compile("rotate right (\\d+) steps?");
            Pattern rotateLeftP = Pattern.compile("rotate left (\\d+) steps?");

            for(String instruction : instructions) {
                Matcher reverseM = reverseP.matcher(instruction);
                Matcher rotateM = rotateP.matcher(instruction);
                Matcher swapM = swapP.matcher(instruction);
                Matcher moveM = moveP.matcher(instruction);
                Matcher swapLetterM = swapLetterP.matcher(instruction);
                Matcher rotateRightM = rotateRightP.matcher(instruction);
                Matcher rotateLeftM = rotateLeftP.matcher(instruction);

                if(reverseM.find()){
                    int pos1 = Integer.parseInt(reverseM.group(1));
                    int pos2 = Integer.parseInt(reverseM.group(2));
                    String toReverse = output.substring(pos1, pos2+1);
                    String reversed = StringUtils.reverse(toReverse);
                    output = output.replace(toReverse, reversed);
                } else if(rotateM.find()) {
                    String letter = rotateM.group(1);
                    int index = output.indexOf(letter);
                    output = StringUtils.rotate(output, index + 1);
                    if(index >= 4) {
                        output = StringUtils.rotate(output, 1);
                    }
                } else if(swapM.find()){
                    int pos1 = Integer.parseInt(swapM.group(1));
                    int pos2 = Integer.parseInt(swapM.group(2));
                    char char1 = output.charAt(pos1);
                    char char2 = output.charAt(pos2);
                    output = output.replace(char1, '!');
                    output = output.replace(char2, char1);
                    output = output.replace('!', char2);
                } else if(swapLetterM.find()) {
                    String char1 = swapLetterM.group(1);
                    String char2 = swapLetterM.group(2);
                    output = output.replace(char1, "!");
                    output = output.replace(char2, char1);
                    output = output.replace("!", char2);
                } else if(moveM.find()) {
                    int pos1 = Integer.parseInt(moveM.group(1));
                    int pos2 = Integer.parseInt(moveM.group(2));
                    char charToMove = output.charAt(pos1);
                    output = output.substring(0, pos1) + output.substring(pos1+1);
                    if(pos2 >= output.length()) {
                        output = output + charToMove;
                    } else {
                        output = output.substring(0, pos2) + charToMove + output.substring(pos2);
                    }
                } else if(rotateRightM.find()) {
                    int steps = Integer.parseInt(rotateRightM.group(1));
                    output = StringUtils.rotate(output,steps);
                } else if(rotateLeftM.find()) {
                    int steps = Integer.parseInt(rotateLeftM.group(1));
                    steps = output.length() - steps;
                    output = StringUtils.rotate(output,steps);
                } else {
                    throw new RuntimeException("NO INSTRUCTION MATCHES: " + instruction);
                }

            }

            return output;
        }

        private String unscramble(String input, List<String> instructions) {

            Collections.reverse(instructions);
            String output = input;

            Pattern reverseP = Pattern.compile("reverse positions (\\d+) through (\\d+)");
            Pattern rotateP = Pattern.compile("rotate based on position of letter (.)");
            Pattern swapP = Pattern.compile("swap position (\\d+) with position (\\d+)");
            Pattern moveP = Pattern.compile("move position (\\d+) to position (\\d+)");
            Pattern swapLetterP = Pattern.compile("swap letter (.) with letter (.)");
            Pattern rotateRightP = Pattern.compile("rotate right (\\d+) steps?");
            Pattern rotateLeftP = Pattern.compile("rotate left (\\d+) steps?");

            for(String instruction : instructions) {
                Matcher reverseM = reverseP.matcher(instruction);
                Matcher rotateM = rotateP.matcher(instruction);
                Matcher swapM = swapP.matcher(instruction);
                Matcher moveM = moveP.matcher(instruction);
                Matcher swapLetterM = swapLetterP.matcher(instruction);
                Matcher rotateRightM = rotateRightP.matcher(instruction);
                Matcher rotateLeftM = rotateLeftP.matcher(instruction);

                // Reverse will be the same
                if(reverseM.find()){
                    int pos1 = Integer.parseInt(reverseM.group(1));
                    int pos2 = Integer.parseInt(reverseM.group(2));
                    String toReverse = output.substring(pos1, pos2+1);
                    String reversed = StringUtils.reverse(toReverse);
                    output = output.replace(toReverse, reversed);
                } else if(rotateM.find()) {
                    String letter = rotateM.group(1);
                    int index = output.indexOf(letter);
                    int indexToRotate = switch (index) {
                        case 0, 1 -> 7;
                        case 2 -> 2;
                        case 3 -> 6;
                        case 4 -> 1;
                        case 5 -> 5;
                        case 7 -> 4;
                        default -> 0;
                    };
                    output = StringUtils.rotate(output, indexToRotate);
                } else if(swapM.find()){
                    // SWAP SHOULD BE THE SAME
                    int pos1 = Integer.parseInt(swapM.group(1));
                    int pos2 = Integer.parseInt(swapM.group(2));
                    char char1 = output.charAt(pos1);
                    char char2 = output.charAt(pos2);
                    output = output.replace(char1, '!');
                    output = output.replace(char2, char1);
                    output = output.replace('!', char2);
                } else if(swapLetterM.find()) {
                    // SWAP SHOULD BE THE SAME
                    String char1 = swapLetterM.group(1);
                    String char2 = swapLetterM.group(2);
                    output = output.replace(char1, "!");
                    output = output.replace(char2, char1);
                    output = output.replace("!", char2);
                } else if(moveM.find()) {
                    //MOVE WILL BE THE OPPOSITE?
                    int pos1 = Integer.parseInt(moveM.group(2)); // SWAP NUMS HERE
                    int pos2 = Integer.parseInt(moveM.group(1));
                    char charToMove = output.charAt(pos1);
                    output = output.substring(0, pos1) + output.substring(pos1+1);
                    if(pos2 >= output.length()) {
                        output = output + charToMove;
                    } else {
                        output = output.substring(0, pos2) + charToMove + output.substring(pos2);
                    }
                } else if(rotateRightM.find()) {
                    //ROTATE RIGHT NEEDS TO ROTATE LEFT NOW
                    int steps = Integer.parseInt(rotateRightM.group(1));
                    steps = output.length() - steps;
                    output = StringUtils.rotate(output,steps);
                } else if(rotateLeftM.find()) {
                    //ROTATE LEFT NEEDS TO ROTATE RIGHT
                    int steps = Integer.parseInt(rotateLeftM.group(1));
                    output = StringUtils.rotate(output,steps);
                } else {
                    throw new RuntimeException("NO INSTRUCTION MATCHES: " + instruction);
                }
            }

            return output;
        }
    }
}
