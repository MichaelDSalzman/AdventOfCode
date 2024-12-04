package aoc2015.day11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2015";
        String day = "11";

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

        public String calculateP1(List<String> lines) {
            String currentPassword = lines.get(0);

            String prospectivePassword = nextPassword(currentPassword);
            while(!passwordValidP1(prospectivePassword)) {
                prospectivePassword = nextPassword(prospectivePassword);
            }

            return prospectivePassword;
        }

        private String nextPassword(String password) {
            List<String> charactersReversed = new ArrayList<>();
            for(int i=password.length()-1; i>=0; i--) {
                String character = password.substring(i, i+1);
                charactersReversed.add(character);
            }

            StringBuilder sb = new StringBuilder();
            boolean needCarry = true;
            for(String character : charactersReversed) {
                char c = (char) (character.charAt(0));
                if(needCarry) {
                    c++;
                    if (c == 'i' || c == 'o' || c == 'l') {
                        c++;
                    }
                }
                if(c > 'z') {
                    c = 'a';
                    needCarry = true;
                } else {
                    needCarry = false;
                }
                sb.append(c);
            }
            if(needCarry) {
                sb.append('a');
            }

            return sb.reverse().toString();
        }

        private boolean passwordValidP1(String password) {
            if(password.contains("i") || password.contains("l") || password.contains("o")) {
                return false;
            }

            Pattern p = Pattern.compile("(\\w)\\1.*(\\w)\\2");
            Matcher m = p.matcher(password);
            if(!m.find()) {
                return false;
            }

            Pattern sequencial = Pattern.compile("abc|bcd|cde|def|efg|fgh|ghi|hij|ijk|jkl|klm|lmn|mno|nop|opq|pqr|qrs|rst|stu|tuv|uvx|vwx|wxy|xyz");
            return sequencial.matcher(password).find();
        }

        public int calculateP2(List<String> lines) {
            return -1;
        }
    }
}
