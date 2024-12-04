package aoc2016.day22;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2016";
        String day = "22";

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
            List<DiskNode> diskNodes = parseNodes(lines);
            int viablePairs = 0;
            for(int i=0; i<diskNodes.size()-1; i++) {
                for(int j=i+1; j<diskNodes.size(); j++) {
                    DiskNode dn1 = diskNodes.get(i);
                    DiskNode dn2 = diskNodes.get(j);
                    if(dn1.used > 0 && dn1.used < dn2.available) {
                        viablePairs++;
                    }
                    if(dn2.used > 0 && dn2.used < dn1.available) {
                        viablePairs++;
                    }
                }
            }
            return viablePairs;
        }

        public int calculateP2(List<String> lines) {
            return -1;
        }

        private List<DiskNode> parseNodes(List<String> lines) {
            List<DiskNode> diskNodes = new ArrayList<>();

            Pattern p = Pattern.compile("/dev/grid/node-x(\\d+)-y(\\d+)\\s+(\\d+)T\\s+(\\d+)T\\s+(\\d+)T\\s+(\\d+)%");
            for(String line : lines) {
                Matcher m = p.matcher(line);
                if(m.find()) {
                    diskNodes.add(new DiskNode(
                        Integer.parseInt(m.group(1)),
                        Integer.parseInt(m.group(2)),
                        Integer.parseInt(m.group(3)),
                        Integer.parseInt(m.group(4)),
                        Integer.parseInt(m.group(5))
                    ));
                }
            }

            return diskNodes;
        }

        public static class DiskNode {
            private final int x;
            private final int y;
            private final int size;
            private final int used;
            private final int available;

            public DiskNode(int x, int y, int size, int used, int available) {
                this.x = x;
                this.y = y;
                this.size = size;
                this.used = used;
                this.available = available;
            }
        }
    }
}
