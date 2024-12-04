package aoc2017.day18;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.apache.commons.lang3.math.NumberUtils;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2017";
        String day = "18";

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

        public long calculateP1(List<String> lines) {
            return valueOfFirstReceive(lines, true);
        }

        public int calculateP2(List<String> lines) {
            return twoProgramsSimultaneously(lines);
        }

        private long valueOfFirstReceive(List<String> lines, boolean sendFirstRecovered) {
            Map<String, Long> registers = new HashMap<>();
            long lastFrequencySent = -1;

            for(int i=0; i<lines.size(); i++) {
                String line = lines.get(i);
                if(line.startsWith("set")) {
                    String[] split = line.split("\\s+");
                    String destination = split[1];
                    String value = split[2];

                    registers.put(destination, NumberUtils.isCreatable(value) ? Integer.parseInt(value) : registers.getOrDefault(value, 0L));
                } else if(line.startsWith("add")) {
                    String[] split = line.split("\\s+");
                    String destination = split[1];
                    String value = split[2];

                    long sum = registers.getOrDefault(destination, 0L);
                    sum += NumberUtils.isCreatable(value) ? Integer.parseInt(value) : registers.getOrDefault(value, 0L);

                    registers.put(destination, sum);
                } else if(line.startsWith("mul")) {
                    String[] split = line.split("\\s+");
                    String destination = split[1];
                    String value = split[2];

                    long product = registers.getOrDefault(destination, 0L);
                    product *= NumberUtils.isCreatable(value) ? Integer.parseInt(value) : registers.getOrDefault(value, 0L);

                    registers.put(destination, product);
                } else if(line.startsWith("mod")) {
                    String[] split = line.split("\\s+");
                    String destination = split[1];
                    String value = split[2];

                    long remainder = registers.getOrDefault(destination, 0L);
                    remainder %= NumberUtils.isCreatable(value) ? Integer.parseInt(value) :
                        registers.getOrDefault(value, 0L);

                    registers.put(destination, remainder);
                } else if(line.startsWith("jgz")) {
                    String[] split = line.split("\\s+");
                    String source = split[1];
                    String distance = split[2];

                    if((NumberUtils.isCreatable(source) ?  Integer.parseInt(source) : registers.getOrDefault(source, 0L)) > 0) {
                        i += NumberUtils.isCreatable(distance) ? Integer.parseInt(distance) : registers.getOrDefault(distance, 0L);
                        i--; //to offset the i++ of the loop
                    }
                } else if(line.startsWith("snd")) {
                    String[] split = line.split("\\s+");
                    lastFrequencySent = NumberUtils.isCreatable(split[1]) ? Integer.parseInt(split[1]) : registers.getOrDefault(split[1], 0L);
                } else if(line.startsWith("rcv")) {
                    String[] split = line.split("\\s+");
                    if(sendFirstRecovered && (NumberUtils.isCreatable(split[1]) ? Integer.parseInt(split[1]) : registers.getOrDefault(split[1], 0L)) != 0) {
                        return lastFrequencySent;
                    }
                } else {
                    throw new RuntimeException("MISSED " + line);
                }
            }

            return -1;
        }

        private int twoProgramsSimultaneously(List<String> lines) {

            Map<String, Long> registers0 = new HashMap<>();
            registers0.put("p", 0L);
            Map<String, Long> registers1 = new HashMap<>();
            registers1.put("p", 1L);

            Queue<Long> prog0Queue = new ArrayDeque<>();
            Queue<Long> prog1Queue = new ArrayDeque<>();

            int timesProg1Sent = 0;

            int prog0LineNum = 0;
            int prog1LineNum = 0;

            boolean prog0Waiting = false;
            boolean prog1Waiting = false;

            while(!prog0Waiting || !prog1Waiting) {
                boolean prog0Active = !prog0Waiting;

                int lineNum = prog0Active ? prog0LineNum : prog1LineNum;
                String line = lines.get(lineNum);
                Map<String, Long> registers = prog0Active ? registers0 : registers1;

                if(line.startsWith("set")) {
                    String[] split = line.split("\\s+");
                    String destination = split[1];
                    String value = split[2];

                    registers.put(destination, NumberUtils.isCreatable(value) ? Integer.parseInt(value) : registers.getOrDefault(value, 0L));
                } else if(line.startsWith("add")) {
                    String[] split = line.split("\\s+");
                    String destination = split[1];
                    String value = split[2];

                    long sum = registers.getOrDefault(destination, 0L);
                    sum += NumberUtils.isCreatable(value) ? Integer.parseInt(value) : registers.getOrDefault(value, 0L);

                    registers.put(destination, sum);
                } else if(line.startsWith("mul")) {
                    String[] split = line.split("\\s+");
                    String destination = split[1];
                    String value = split[2];

                    long product = registers.getOrDefault(destination, 0L);
                    product *= NumberUtils.isCreatable(value) ? Integer.parseInt(value) : registers.getOrDefault(value, 0L);

                    registers.put(destination, product);
                } else if(line.startsWith("mod")) {
                    String[] split = line.split("\\s+");
                    String destination = split[1];
                    String value = split[2];

                    long remainder = registers.getOrDefault(destination, 0L);
                    remainder %= NumberUtils.isCreatable(value) ? Integer.parseInt(value) :
                        registers.getOrDefault(value, 0L);

                    registers.put(destination, remainder);
                } else if(line.startsWith("jgz")) {
                    String[] split = line.split("\\s+");
                    String source = split[1];
                    String distance = split[2];

                    if((NumberUtils.isCreatable(source) ?  Integer.parseInt(source) : registers.getOrDefault(source, 0L)) > 0) {
                        long offset = NumberUtils.isCreatable(distance) ? Integer.parseInt(distance) : registers.getOrDefault(distance, 0L);
                        if(prog0Active) {
                            prog0LineNum += offset;
                        } else {
                            prog1LineNum += offset;
                        }
                        continue;
                    }
                } else if(line.startsWith("snd")) {
                    String[] split = line.split("\\s+");
                    long value = NumberUtils.isCreatable(split[1]) ? Integer.parseInt(split[1]) : registers.getOrDefault(split[1], 0L);
                    if(prog0Active) {
                        prog1Queue.add(value);
                        prog1Waiting = false;
                    } else {
                        prog0Queue.add(value);
                        prog0Waiting = false;
                        timesProg1Sent++;
                    }
                } else if(line.startsWith("rcv")) {
                    String[] split = line.split("\\s+");
                    Queue<Long> queue = prog0Active ? prog0Queue : prog1Queue;
                    if(!queue.isEmpty()) {
                        long value = queue.remove();
                        registers.put(split[1], value);
                    } else {
                        if(prog0Active) {
                            prog0Waiting = true;
                        } else {
                            prog1Waiting = true;
                        }
                        continue;
                    }
                } else {
                    throw new RuntimeException("MISSED " + line);
                }

                if(prog0Active) {
                    prog0LineNum++;
                } else {
                    prog1LineNum++;
                }
            }

            return timesProg1Sent;
        }
    }
}
