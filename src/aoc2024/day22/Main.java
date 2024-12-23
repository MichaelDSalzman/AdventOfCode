package aoc2024.day22;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = null;
        String day = null;

        Pattern p = Pattern.compile("aoc(\\d+)\\.day(\\d+).*");
        Matcher m = p.matcher(Main.class.getName());
        if(m.find()) {
            year = m.group(1);
            day = m.group(2);
        }

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

        public Object calculateP1(List<String> lines) {
            long sum = 0;
            for(String line: lines) {
                long secretNumber = Long.parseLong(line);

                for(int i=0; i<2000; i++) {
                    secretNumber = iterate(secretNumber);
                }

                sum += secretNumber;
            }

            return sum;
        }

        // TODO Potential performance increase, build up map of sequence->price while iterating over the secret numbers instead of doing it twice
        public Object calculateP2(List<String> lines) {
            List<List<Integer>> allBuyerBids = new ArrayList<>();
            for(String line : lines) {
                List<Integer> buyerBids = new ArrayList<>();
                long secretNumber = Long.parseLong(line);
                buyerBids.add((int) (secretNumber % 10));

                for(int i=0; i<2000; i++) {
                    secretNumber = iterate(secretNumber);
                    buyerBids.add((int) (secretNumber % 10));
                }
                allBuyerBids.add(buyerBids);
            }

            Map<String, Long> sequenceToNumberOfBananas = new HashMap<>();
            for(int buyerBidIndex=0; buyerBidIndex< allBuyerBids.size(); buyerBidIndex++) {
                List<Integer> buyerBids = allBuyerBids.get(buyerBidIndex);

                System.out.println("SEARCHING THROUGH BUYER " + buyerBidIndex);
                int numSkipped = 0;
                for(int i=4; i<buyerBids.size(); i++) {
                    List<Integer> differences = new ArrayList<>();
                    for(int j = 4; j>0; j--) {
                        differences.add(buyerBids.get(i-j+1) - buyerBids.get(i-j));
                    }
                    String sequence = StringUtils.join(differences, ",");
                    if(sequenceToNumberOfBananas.containsKey(sequence)) {
                        numSkipped++;
                        continue;
                    }

                    //Iterate over ALL buyer bids to find when this sequence first appears, and keep track of bananas
                    long count = 0;
                    for(int buyerBidLookingForSequenceIndex = buyerBidIndex; buyerBidLookingForSequenceIndex < allBuyerBids.size(); buyerBidLookingForSequenceIndex++) {
                        List<Integer> buyerBidLookingForSequence = allBuyerBids.get(buyerBidLookingForSequenceIndex);
                        for(int j=0; j<buyerBidLookingForSequence.size()-4; j++) {
                            if(buyerBidLookingForSequence.get(j+1) - buyerBidLookingForSequence.get(j) == differences.get(0) &&
                                buyerBidLookingForSequence.get(j+2) - buyerBidLookingForSequence.get(j+1) == differences.get(1) &&
                                buyerBidLookingForSequence.get(j+3) - buyerBidLookingForSequence.get(j+2) == differences.get(2) &&
                                buyerBidLookingForSequence.get(j+4) - buyerBidLookingForSequence.get(j+3) == differences.get(3)) {

                                count += buyerBidLookingForSequence.get(j+4);
                                break;
                            }
                        }
                    }
                    //Add the total bananas for this sequence to map
                    sequenceToNumberOfBananas.put(sequence, count);
                }
                System.out.println("SKIPPED: " + numSkipped);
                System.out.println("MAX SO FAR: " + sequenceToNumberOfBananas.values().stream().mapToLong(Long::longValue).max().orElseThrow());
            }

            // Find top value in map and return
            long max = sequenceToNumberOfBananas.values().stream().mapToLong(Long::longValue).max().orElseThrow();

            System.out.println("SEQUENCE: " + sequenceToNumberOfBananas.keySet().stream().filter(k -> sequenceToNumberOfBananas.get(k) == max).toList());
            return max;
        }

        private long iterate(long secretNumber) {
            secretNumber = mixAndPrune(secretNumber, secretNumber << 6);
            secretNumber = mixAndPrune(secretNumber, secretNumber >> 5);
            secretNumber = mixAndPrune(secretNumber, secretNumber << 11);

            return secretNumber;
        }

        private long mixAndPrune(long secretNumber, long mixer) {
            return prune(mix(secretNumber, mixer));
        }
        private long mix(long secretNumber, long mixer) {
            return secretNumber ^ mixer;
        }

        private long prune(long secretNumber) {
            return secretNumber % 16777216;
        }
    }
}
