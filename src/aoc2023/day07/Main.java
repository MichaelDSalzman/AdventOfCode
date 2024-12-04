package aoc2023.day07;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2023";
        String day = "07";

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

            List<Hand> hands = new ArrayList<>();

            for(String line: lines) {
                String[] lineParts = line.split(" ");
                List<String> cards = Arrays.stream(lineParts[0].split("")).toList();

                Hand hand = new Hand(cards, Integer.parseInt(lineParts[1]), false);
                hands.add(hand);
            }

            Collections.sort(hands);

            List<Hand> reversed = new ArrayList<>();
            for(int i= hands.size()-1; i>=0; i--) {
                reversed.add(hands.get(i));
            }

            hands = reversed;

            int totalWinnings = 0;
            for(int i=0; i<hands.size(); i++) {
                int winnings = (i+1) * hands.get(i).bidAmount;
                totalWinnings += winnings;
            }
            return totalWinnings;
        }

        public int calculateP2(List<String> lines) {

            List<Hand> hands = new ArrayList<>();

            for(String line: lines) {
                String[] lineParts = line.split(" ");
                List<String> cards = Arrays.stream(lineParts[0].split("")).toList();

                Hand hand = new Hand(cards, Integer.parseInt(lineParts[1]), true);
                hands.add(hand);
            }

            Collections.sort(hands);

            List<Hand> reversed = new ArrayList<>();
            for(int i= hands.size()-1; i>=0; i--) {
                reversed.add(hands.get(i));
            }

            hands = reversed;

            int totalWinnings = 0;
            for(int i=0; i<hands.size(); i++) {
                int winnings = (i+1) * hands.get(i).bidAmount;
                totalWinnings += winnings;
            }
            return totalWinnings;
        }
    }

    public static class Hand implements Comparable<Hand> {
        List<String> cards = new ArrayList<>();
        Integer bidAmount;
        boolean jacksWild = false;

        public Hand(List<String> cards, Integer bidAmount, boolean jacksWild) {
            this.cards = cards;
            this.bidAmount = bidAmount;
            this.jacksWild = jacksWild;
        }

        public List<String> getCards() {
            return cards;
        }

        public Integer getBidAmount() {
            return bidAmount;
        }

        public HandType getHandType() {
            Map<String, Integer> cardMap = new HashMap<>();
            for(String card : this.getCards()) {
                Integer cardCount = cardMap.get(card);
                if(cardCount == null) {
                    cardCount = 0;
                }

                cardMap.put(card, ++cardCount);
            }

            int numberOfWild = jacksWild ? (cardMap.get("J") == null ? 0 : cardMap.get("J")) : 0;
            if(jacksWild) {
                cardMap.remove("J");
            }

            // If no wilds
            if(!jacksWild || numberOfWild == 0) {
                if (cardMap.containsValue(5)) {
                    return HandType.FIVE_OF_A_KIND;
                }

                if (cardMap.containsValue(4)) {
                    return HandType.FOUR_OF_A_KIND;
                }

                if (cardMap.containsValue(3) && cardMap.containsValue(2)) {
                    return HandType.FULL_HOUSE;
                }

                if (cardMap.containsValue(3)) {
                    return HandType.THREE_OF_A_KIND;
                }

                if (cardMap.values().stream().filter(i -> i == 2).toList().size() == 2) {
                    return HandType.TWO_PAIR;
                }

                if (cardMap.containsValue(2)) {
                    return HandType.ONE_PAIR;
                }

                return HandType.HIGH_CARD;
            }

            if(numberOfWild == 5) {
                return HandType.FIVE_OF_A_KIND;
            }

            int maxCardQuantity = cardMap.values().stream().max(Integer::compareTo).get();
            if(maxCardQuantity == 4) {
                return HandType.FIVE_OF_A_KIND;
            }

            if(maxCardQuantity == 3) {
                if(numberOfWild == 1) {
                    return HandType.FOUR_OF_A_KIND;
                }
                return HandType.FIVE_OF_A_KIND;
            }

            if(cardMap.values().stream().filter(i -> i == 2).toList().size() == 2) {
                return HandType.FULL_HOUSE;
            }

            if(maxCardQuantity == 2) {
                if(numberOfWild == 1) {
                    return HandType.THREE_OF_A_KIND;
                } else if (numberOfWild == 2) {
                    return HandType.FOUR_OF_A_KIND;
                } else {
                    return HandType.FIVE_OF_A_KIND;
                }
            }

            if(maxCardQuantity == 1) {
                if (numberOfWild == 1) {
                    return HandType.ONE_PAIR;
                } else if (numberOfWild == 2) {
                    return HandType.THREE_OF_A_KIND;
                } else if(numberOfWild == 3) {
                    return HandType.FOUR_OF_A_KIND;
                } else if(numberOfWild == 4) {
                    return HandType.FIVE_OF_A_KIND;
                }
            }

            return null;
        }

        @Override
        public int compareTo(Hand o) {
            if(this.getHandType().rank > o.getHandType().rank) {
                return -1;
            } else if (this.getHandType().rank < o.getHandType().rank) {
                return 1;
            }

            Map<String, Integer> cardRankMap = new HashMap<>();
            cardRankMap.put("2", 2);
            cardRankMap.put("3", 3);
            cardRankMap.put("4", 4);
            cardRankMap.put("5", 5);
            cardRankMap.put("6", 6);
            cardRankMap.put("7", 7);
            cardRankMap.put("8", 8);
            cardRankMap.put("9", 9);
            cardRankMap.put("T", 10);
            cardRankMap.put("J", jacksWild ? 1 : 11);
            cardRankMap.put("Q", 12);
            cardRankMap.put("K", 13);
            cardRankMap.put("A", 14);
            for(int cardIndex = 0; cardIndex< this.getCards().size(); cardIndex++) {
                Integer leftHandRank = cardRankMap.get(this.getCards().get(cardIndex));
                Integer rightHandRank = cardRankMap.get(o.getCards().get(cardIndex));

                if(leftHandRank > rightHandRank) {
                    return -1;
                } else if(leftHandRank < rightHandRank) {
                    return 1;
                }
            }

            return 0;
        }

        @Override
        public String toString() {
            return "Hand{" +
                "cards=" + cards +
                ", bidAmount=" + bidAmount +
                '}';
        }
    }

    public enum HandType {
        FIVE_OF_A_KIND(6),
        FOUR_OF_A_KIND(5),
        FULL_HOUSE(4),
        THREE_OF_A_KIND(3),
        TWO_PAIR(2),
        ONE_PAIR(1),
        HIGH_CARD(0);

        public int rank;

        HandType(int rank) {
            this.rank = rank;
        }
    }
}
