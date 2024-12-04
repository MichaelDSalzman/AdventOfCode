package aoc2015.day21;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2015";
        String day = "21";

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

            List<List<Item>> itemLists = getEquipmentList();
            int cheapest = Integer.MAX_VALUE;

            for(List<Item> items : itemLists) {
                int cost = items.stream().map(i -> i.cost).reduce(Integer::sum).get();

                if(calculateFight(100, items, 103, 9, 2) && cost < cheapest){
                    cheapest = cost;
                }
            }
            return cheapest;
        }

        public int calculateP2(List<String> lines) {
            List<List<Item>> itemLists = getEquipmentList();
            int mostExpensive = 0;

            for(List<Item> items : itemLists) {
                int cost = items.stream().map(i -> i.cost).reduce(Integer::sum).get();

                if(!calculateFight(100, items, 103, 9, 2) && cost > mostExpensive){
                    mostExpensive = cost;
                }
            }
            return mostExpensive;
        }

        private boolean calculateFight(int playerHP, List<Item> items, int enemyHP, int enemyDamage, int enemyArmor) {
            while(true) {
                int damage = items.stream().map(i -> i.damage).reduce(Integer::sum).get() - enemyArmor;
                enemyHP -= Math.max(1, damage);

                if(enemyHP <= 0) {
                    return true;
                }

                damage = enemyDamage - items.stream().map(i -> i.armor).reduce(Integer::sum).get();
                playerHP -= Math.max(1, damage);
                if(playerHP <= 0) {
                    return false;
                }
            }
        }

        private List<List<Item>> getEquipmentList() {
            List<Item> weapons = getWeapons();
            List<Item> armor = getArmor();
            List<Item> rings = getRings();

            List<List<Item>> equipmentList = new ArrayList<>();

            for(int weaponIndex=0; weaponIndex<Math.pow(2, weapons.size()); weaponIndex++) {
                String weaponBinary = Integer.toBinaryString(weaponIndex);
                weaponBinary = String.format("%" + weapons.size() + "s", weaponBinary).replaceAll(" ", "0");
                if(weaponBinary.replaceAll("0", "").length() != 1) {
                    continue;
                }

                for(int armorIndex=0; armorIndex<Math.pow(2, armor.size()); armorIndex++) {
                    String armorBinary = Integer.toBinaryString(armorIndex);
                    armorBinary = String.format("%" + armor.size() + "s", armorBinary).replaceAll(" ", "0");

                    if(armorBinary.replaceAll("0", "").length() > 1) {
                        continue;
                    }

                    for(int ringIndex=0; ringIndex<Math.pow(2, rings.size()); ringIndex++) {
                        String ringBinary = Integer.toBinaryString(ringIndex);
                        ringBinary = String.format("%" + rings.size() + "s", ringBinary).replaceAll(" ", "0");
                        if(ringBinary.replaceAll("0", "").length() > 2) {
                            continue;
                        }

                        List<Item> items = new ArrayList<>();
                        for(int i=0; i<weapons.size(); i++) {
                            if(weaponBinary.charAt(i) == '1') {
                                items.add(weapons.get(i));
                            }
                        }

                        for(int i=0; i<armor.size(); i++) {
                            if(armorBinary.charAt(i) == '1') {
                                items.add(armor.get(i));
                            }
                        }

                        for(int i=0; i<rings.size(); i++) {
                            if(ringBinary.charAt(i) == '1') {
                                items.add(rings.get(i));
                            }
                        }

                        equipmentList.add(items);
                    }
                }
            }
            return equipmentList;
        }

        private List<Item> getWeapons() {
            return Arrays.asList(
                new Item("Dagger", 8, 4, 0),
                new Item("Shortswort", 10, 5, 0),
                new Item("Warhammer", 25, 6, 0),
                new Item("Longsword", 40, 7, 0),
                new Item("Greataxe", 74, 8, 0)
            );
        }


        private List<Item> getArmor() {
            return Arrays.asList(
                new Item("Leather", 13, 0, 1),
                new Item("Chainmail", 31, 0, 2),
                new Item("Splintmail", 53, 0, 3),
                new Item("Bandedmail", 75, 0, 4),
                new Item("Platemail", 102, 0, 5)
            );
        }

        private List<Item> getRings() {
            return Arrays.asList(
                new Item("Damage +1", 25, 1, 0),
                new Item("Damage +2", 50, 2, 0),
                new Item("Damage +3", 100, 3, 0),
                new Item("Defense +1", 20, 0, 1),
                new Item("Defense +2", 40, 0, 2),
                new Item("Defense +3", 80, 0, 3)
            );
        }
    }

    public record Item(String name, int cost, int damage, int armor){};
}
