package aoc2015.day04;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.apache.commons.codec.digest.DigestUtils;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        String year = "2015";
        String day = "04";

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

        public long calculateP1(List<String> lines) throws NoSuchAlgorithmException {
            String key = lines.get(0);
            long count = 1;
            while(true) {
                String string = key + count;
                String hex = DigestUtils.md5Hex(string);
                if(hex.startsWith("00000")) {
                    return count;
                }

                count++;
            }
        }

        public long calculateP2(List<String> lines) {

            String key = lines.get(0);
            long count = 1;
            while(true) {
                String string = key + count;
                String hex = DigestUtils.md5Hex(string);
                if(hex.startsWith("000000")) {
                    return count;
                }

                count++;
            }
        }
    }
}
