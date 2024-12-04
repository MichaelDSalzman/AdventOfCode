package aoc2016.day10;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2016";
        String day = "10";

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
            Map<Integer, List<Integer>> botInstructions = new HashMap<>();
            Map<Integer, List<Integer>> botInventory = new HashMap<>();

            Pattern valueP = Pattern.compile("value (\\d+) goes to bot (\\d+)");
            Pattern givesP = Pattern.compile("bot (\\d+) gives low to (\\w+) (\\d+) and high to (\\w+) (\\d+)");

            for(String line : lines) {
                if(line.startsWith("value")) {
                    Matcher m = valueP.matcher(line);
                    if(m.find()) {
                        int value = Integer.parseInt(m.group(1));
                        int botNum = Integer.parseInt(m.group(2));
                        List<Integer> inventory = botInventory.getOrDefault(botNum, new ArrayList<>());
                        inventory.add(value);
                        botInventory.put(botNum, inventory);
                    } else {
                        throw new RuntimeException("BOO");
                    }
                } else if(line.contains("gives")){
                    Matcher m = givesP.matcher(line);
                    if(m.find()) {
                        int botNumGiving = Integer.parseInt(m.group(1));
                        int lowDestination = m.group(2).equals("bot") ? Integer.parseInt(m.group(3)) : -1 * Integer.parseInt(m.group(3)) - 10;
                        int highDestination = m.group(4).equals("bot") ? Integer.parseInt(m.group(5)) : -1 * Integer.parseInt(m.group(5)) - 10;

                        botInstructions.put(botNumGiving, List.of(lowDestination, highDestination));
                    } else {
                        throw new RuntimeException("BOO");
                    }
                } else {
                    throw new RuntimeException("BOO");
                }
            }

            int whereToStart = -1;
            for(Integer botNum : botInventory.keySet()) {
                if(botInventory.get(botNum).size() == 2) {
                    whereToStart = botNum;
                }
            }

            return botSwap(botInstructions, botInventory, whereToStart, List.of(61, 17));
        }

        private Integer botSwap(
            Map<Integer, List<Integer>> botInstructions,
            Map<Integer, List<Integer>> botInventory,
            Integer botToInvestigate,
            List<Integer> lookingFor)
        {
            // Get inventory (add a new list if there is none)
            List<Integer> inventory = botInventory.computeIfAbsent(botToInvestigate,
                k -> new ArrayList<>());

            if(lookingFor != null && inventory.containsAll(lookingFor)) {
                return botToInvestigate;
            }

            // If the bot needs to swap
            if(inventory.size() == 2) {
                //sort
                Collections.sort(inventory);
                //get the instructions
                List<Integer> instructions = botInstructions.get(botToInvestigate);
                //bot to send the low one to
                int lowBot = instructions.get(0);
                //bot to send the high one to
                int highBot = instructions.get(1);
                //Get the current inventory for the low destination
                List<Integer> lowBotInventory = botInventory.getOrDefault(lowBot, new ArrayList<>());
                //Get the current inventory for the high destination
                List<Integer> highBotInventory = botInventory.getOrDefault(highBot, new ArrayList<>());

                //Send the new things to the bots
                lowBotInventory.add(inventory.get(0));
                highBotInventory.add(inventory.get(1));

                //Reset the inventory of the bot the items are coming from
                botInventory.put(botToInvestigate, new ArrayList<>());

                    //Set the inventory for the low bot in the map
                    botInventory.put(lowBot, lowBotInventory);
                //same for high bot
                    botInventory.put(highBot, highBotInventory);


                //Investigate the high and low bots now
                if (lowBot >= 0) {
                    Integer found = botSwap(botInstructions, botInventory, lowBot, lookingFor);
                    if(found != null) {
                        return found;
                    }
                }

                if (highBot >= 0) {
                    Integer found = botSwap(botInstructions, botInventory, highBot, lookingFor);
                    if(found != null) {
                        return found;
                    }
                }
            }

            return null;
        }

        public int calculateP2(List<String> lines) {
            Map<Integer, List<Integer>> botInstructions = new HashMap<>();
            Map<Integer, List<Integer>> botInventory = new HashMap<>();

            Pattern valueP = Pattern.compile("value (\\d+) goes to bot (\\d+)");
            Pattern givesP = Pattern.compile("bot (\\d+) gives low to (\\w+) (\\d+) and high to (\\w+) (\\d+)");

            for(String line : lines) {
                if(line.startsWith("value")) {
                    Matcher m = valueP.matcher(line);
                    if(m.find()) {
                        int value = Integer.parseInt(m.group(1));
                        int botNum = Integer.parseInt(m.group(2));
                        List<Integer> inventory = botInventory.getOrDefault(botNum, new ArrayList<>());
                        inventory.add(value);
                        botInventory.put(botNum, inventory);
                    } else {
                        throw new RuntimeException("BOO");
                    }
                } else if(line.contains("gives")){
                    Matcher m = givesP.matcher(line);
                    if(m.find()) {
                        int botNumGiving = Integer.parseInt(m.group(1));
                        int lowDestination = m.group(2).equals("bot") ? Integer.parseInt(m.group(3)) : -1 * Integer.parseInt(m.group(3)) - 10;
                        int highDestination = m.group(4).equals("bot") ? Integer.parseInt(m.group(5)) : -1 * Integer.parseInt(m.group(5)) - 10;

                        botInstructions.put(botNumGiving, List.of(lowDestination, highDestination));
                    } else {
                        throw new RuntimeException("BOO");
                    }
                } else {
                    throw new RuntimeException("BOO");
                }
            }

            int whereToStart = -1;
            for(Integer botNum : botInventory.keySet()) {
                if(botInventory.get(botNum).size() == 2) {
                    whereToStart = botNum;
                }
            }

            botSwap(botInstructions, botInventory, whereToStart, null);

            return botInventory.get(-10).get(0) * botInventory.get(-11).get(0) * botInventory.get(-12).get(0);
        }
    }
}
