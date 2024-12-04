package aoc2016.day11;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2016";
        String day = "11";

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

        public int calculateP1(List<String> lines) {

            GameState initialGameState = new GameState();
            // Sample
            // initialGameState.chipLocations.put("H", 1);
            // initialGameState.chipLocations.put("L", 1);
            // initialGameState.genLocations.put("H", 2);
            // initialGameState.genLocations.put("L", 3);
            // Problem setup
            initialGameState.genLocations.put("P", 1);
            initialGameState.chipLocations.put("P", 1);
            initialGameState.genLocations.put("C", 2);
            initialGameState.genLocations.put("U", 2);
            initialGameState.genLocations.put("R", 2);
            initialGameState.genLocations.put("L", 2);
            initialGameState.chipLocations.put("C", 3);
            initialGameState.chipLocations.put("U", 3);
            initialGameState.chipLocations.put("R", 3);
            initialGameState.chipLocations.put("L", 3);

            return findSolution(initialGameState);
        }

        public int calculateP2(List<String> lines) {
            GameState initialGameState = new GameState();
            initialGameState.genLocations.put("E", 1);
            initialGameState.chipLocations.put("E", 1);
            initialGameState.genLocations.put("D", 1);
            initialGameState.chipLocations.put("D", 1);
            initialGameState.genLocations.put("P", 1);
            initialGameState.chipLocations.put("P", 1);
            initialGameState.genLocations.put("C", 2);
            initialGameState.genLocations.put("U", 2);
            initialGameState.genLocations.put("R", 2);
            initialGameState.genLocations.put("L", 2);
            initialGameState.chipLocations.put("C", 3);
            initialGameState.chipLocations.put("U", 3);
            initialGameState.chipLocations.put("R", 3);
            initialGameState.chipLocations.put("L", 3);

            return findSolution(initialGameState);
        }

        private int findSolution(GameState initialGameState) {
            Queue<GameState> queue = new ArrayDeque<>();
            Set<String> seenStates = new HashSet<>();

            queue.add(initialGameState);

            while(!queue.isEmpty()) {
                GameState gameState = queue.remove();
                if(seenStates.contains(gameState.getStateString())) {
                    continue;
                }

                seenStates.add(gameState.getStateString());

                if(gameState.isWinner()) {
                    return gameState.steps;
                }

                List<GameState> nextGameStates = gameState.getNextGameStates();
                for(GameState nextGameState : nextGameStates) {
                    if(!seenStates.contains(nextGameState.getStateString())) {
                        queue.add(nextGameState);
                    }
                }
            }

            return -1;
        }

    }

    public static class GameState implements Cloneable {
        final int numberOfFloors = 4;
        int elevatorFloor = 1;
        int steps = 0;
        Map<String, Integer> chipLocations = new HashMap<>();
        Map<String, Integer> genLocations = new HashMap<>();
        List<String> history = new ArrayList<>();

        public boolean isWinner() {
            boolean winner = chipLocations.values().stream().allMatch(c -> c == numberOfFloors) && genLocations.values().stream().allMatch(g -> g == numberOfFloors);
            if(winner) {
                // this.history.forEach(System.out::println);
            }
            return winner;
        }

        private void moveItem(String item, int floorNum) {
            if(item.charAt(0) == 'C') {
                this.chipLocations.put(item.substring(1,2), floorNum);
            } else {
                this.genLocations.put(item.substring(1,2), floorNum);
            }
        }

        public List<GameState> getNextGameStates() {
            List<GameState> gameStates = new ArrayList<>();
            List<String> itemsOnFloor = new ArrayList<>();
            for(String chip : chipLocations.keySet()) {
                if(chipLocations.get(chip) == elevatorFloor) {
                    itemsOnFloor.add("C" + chip);
                }
            }
            for(String gen : genLocations.keySet()) {
                if(genLocations.get(gen) == elevatorFloor) {
                    itemsOnFloor.add("G" + gen);
                }
            }

            // Iterate over and make moves with a single item
            for(String item : itemsOnFloor) {
                //able to move down
                if(elevatorFloor > 1) {
                    GameState gameState = this.clone();
                    gameState.steps = this.steps + 1;
                    gameState.moveItem(item, this.elevatorFloor-1);
                    gameState.elevatorFloor = this.elevatorFloor-1;
                    if(gameState.isValidState()) {
                        gameStates.add(gameState);
                    }
                }

                //able to move up
                if(elevatorFloor < numberOfFloors) {
                    GameState gameState = this.clone();
                    gameState.steps = this.steps + 1;
                    gameState.moveItem(item, this.elevatorFloor+1);
                    gameState.elevatorFloor = this.elevatorFloor+1;
                    if(gameState.isValidState()) {
                        gameStates.add(gameState);
                    }
                }
            }


            //iterate and move two items
            for(int i=0; i<itemsOnFloor.size()-1; i++) {
                for(int j=i+1; j<itemsOnFloor.size(); j++) {
                    //able to move down
                    if(elevatorFloor>1) {
                        GameState gameState = this.clone();
                        gameState.steps = this.steps + 1;
                        gameState.moveItem(itemsOnFloor.get(i), elevatorFloor-1);
                        gameState.moveItem(itemsOnFloor.get(j), elevatorFloor-1);
                        gameState.elevatorFloor = this.elevatorFloor-1;

                        if(gameState.isValidState()) {
                            gameStates.add(gameState);
                        }
                    }

                    //able to move up
                    if(elevatorFloor < numberOfFloors) {
                        GameState gameState = this.clone();
                        gameState.steps = this.steps + 1;
                        gameState.moveItem(itemsOnFloor.get(i), elevatorFloor+1);
                        gameState.moveItem(itemsOnFloor.get(j), elevatorFloor+1);
                        gameState.elevatorFloor = this.elevatorFloor+1;

                        if(gameState.isValidState()) {
                            gameStates.add(gameState);
                        }
                    }
                }
            }
            return gameStates;
        }

        public String getStateString() {
            List<String> chipGenLocationCombos = new ArrayList<>();
            for(String chip : chipLocations.keySet()) {
                String chipGenLocation = chipLocations.get(chip).toString() + genLocations.get(chip).toString();
                chipGenLocationCombos.add(chipGenLocation);
            }

            Collections.sort(chipGenLocationCombos);


            return elevatorFloor + "-" + String.join(",", chipGenLocationCombos);
        }

        public boolean isValidState() {
            if(elevatorFloor < 0 || elevatorFloor > numberOfFloors) {
                return false;
            }

            for(int i=1; i<=numberOfFloors; i++) {
                int numGensOnFloor = Collections.frequency(genLocations.values(), i);
                int numChipsOnFloor = Collections.frequency(chipLocations.values(), i);

                if(numGensOnFloor == 0) {
                    continue;
                }

                if(numChipsOnFloor > numGensOnFloor) {
                    return false;
                }
                for(String chip : chipLocations.keySet()) {
                    if(chipLocations.get(chip) == i && genLocations.get(chip) != i) {
                        return false;
                    }
                }
            }

            return true;
        }

        public static GameState createFromString(String gameStateString) {
            GameState gameState = new GameState();

            gameState.elevatorFloor = Integer.parseInt(gameStateString.split("-")[0]);
            String[] chipGenCombos = gameStateString.split("-")[1].split(",");
            for(int i=0; i<chipGenCombos.length; i++) {
                String name = String.valueOf((char)('A' + i));
                int chipLocation = Integer.parseInt(chipGenCombos[i].substring(0, 1));
                int genLocation = Integer.parseInt(chipGenCombos[i].substring(1));

                gameState.chipLocations.put(name, chipLocation);
                gameState.genLocations.put(name, genLocation);
            }

            return gameState;
        }

        @Override
        public GameState clone() {
            try {
                GameState clone = (GameState) super.clone();
                // TODO: copy mutable state here, so the clone can't change the internals of the original
                clone.chipLocations = new HashMap<>(this.chipLocations);
                clone.genLocations = new HashMap<>(this.genLocations);
                clone.elevatorFloor = this.elevatorFloor;
                clone.steps = this.steps;
                clone.history = new ArrayList<>(this.history);
                clone.history.add(this.getStateString());

                return clone;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }
    }
}
