package aoc2023.day01;

import java.io.IOException;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        FileReader fileReader = new FileReader("src/day01/01.txt");
        // util.FileReader fileReader = new util.FileReader("src/inputs/sample.txt");
        List<String> lines = fileReader.getLines();

        System.out.println(lines);

        Problem1 problem = new Problem1();
        String answer1 = problem.calculateP1(lines);
        System.out.println("P1 " + answer1);

        String answer2 = problem.calculateP2(lines);
        System.out.println("P2 " + answer2);
    }
}
