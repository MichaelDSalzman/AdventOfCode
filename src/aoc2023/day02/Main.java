package aoc2023.day02;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        FileReader fileReader = new FileReader("src/day02/input.txt");
        // FileReader fileReader = new FileReader("src/day02/sample.txt");

        List<String> lines = fileReader.getLines();

        Map<String, Integer> limits = new HashMap<>();
        limits.put("red", 12);
        limits.put("green", 13);
        limits.put("blue", 14);
        Problem problem = new Problem(limits);

        System.out.println("P1 " + problem.calculateValidGames(lines));
        System.out.println("P2 " + problem.calculatePowers(lines));

    }
}
