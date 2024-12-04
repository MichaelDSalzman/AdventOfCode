package aoc2023.day02;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Problem {

    Map<String, Integer> limitMap;

    public Problem(Map<String, Integer> limitMap) {
        this.limitMap = limitMap;
    }

    public String calculateValidGames(List<String> lines) {
        int total = 0;

        for(String line: lines) {
            boolean valid = true;

            String[] game = line.split(":");
            Integer gameNum = Integer.parseInt(game[0].trim().split(" ")[1]);

            String[] pulls = game[1].split(";");
            for(String pull: pulls) {
                String[] colors = pull.trim().split(",");
                for(String color: colors) {
                    String[] components = color.trim().split(" ");

                    Integer quantity = Integer.parseInt(components[0].trim());
                    String colorName = components[1].trim();
                    if(quantity > limitMap.get(colorName)) {
                        valid = false;
                    }
                }
            }

            if(valid) {
                total += gameNum;
            }
        }
        return "TOTAL " + total;
    }

    public String calculatePowers(List<String> lines) {
        int total = 0;

        for(String line: lines) {
            Map<String, Integer> minCubeMap = new HashMap<>();

            String[] game = line.split(":");

            String[] pulls = game[1].split(";");
            for(String pull: pulls) {
                String[] colors = pull.trim().split(",");
                for(String color: colors) {
                    String[] components = color.trim().split(" ");

                    Integer quantity = Integer.parseInt(components[0].trim());
                    String colorName = components[1].trim();

                    Integer minCubes = minCubeMap.get(colorName);
                    if(minCubes == null) {
                        minCubeMap.put(colorName, quantity);
                    } else if (minCubes < quantity) {
                        minCubeMap.put(colorName, quantity);
                    }
                }
            }

            Integer power = 1;
            for(Integer value : minCubeMap.values()) {
                power *= value;
            }

            total += power;
        }
        return "TOTAL " + total;
    }
}
