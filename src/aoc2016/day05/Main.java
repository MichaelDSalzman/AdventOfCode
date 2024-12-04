package aoc2016.day05;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.math.NumberUtils;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2016";
        String day = "05";

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

        public static final String hash = "ojvtpuvg";

        public int calculateP1(List<String> lines) {
            boolean found = false;
            int count = 0;
            String[] password = new String[8];
            while(true) {
                String test = DigestUtils.md5Hex(hash + count);
                if(test.startsWith("00000")) {
                    System.out.printf("FOUND %s%n", test);
                    String position = test.substring(5,6);
                    String value = test.substring(6,7);
                    if(NumberUtils.isCreatable(position) && NumberUtils.toInt(position) < 8) {
                        int positionInt = NumberUtils.toInt(position);
                        if(password[positionInt] == null) {
                            password[positionInt] = value;
                        }

                        if(Arrays.stream(password).allMatch(Objects::nonNull)) {
                            System.out.println(Arrays.toString(password));
                            return -1;
                        }
                    }
                }
                count++;
            }
        }

        public int calculateP2(List<String> lines) {
            return -1;
        }
    }
}
