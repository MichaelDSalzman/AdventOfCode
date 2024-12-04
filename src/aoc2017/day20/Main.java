package aoc2017.day20;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.FileReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String year = "2017";
        String day = "20";

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
            return simulate(lines, false);
        }

        public int calculateP2(List<String> lines) {
            return simulate(lines, true);
        }

        private int simulate(List<String> lines, boolean removeCollisions) {
            List<Particle> particles = new ArrayList<>();
            Map<Integer, Integer> particleToClosest = new HashMap<>();

            Pattern p = Pattern.compile(".=<(-?\\d+),(-?\\d+),(-?\\d+)>,?");

            for(int i=0; i<lines.size(); i++) {
                String line = lines.get(i);
                long x = 0, y = 0, z = 0, velX = 0, velY = 0, velZ = 0, accelX = 0, accelY = 0, accelZ = 0;
                String[] split = line.split("\\s+");
                Matcher m = p.matcher(split[0]);
                if(m.find()) {
                    x = Long.parseLong(m.group(1));
                    y = Long.parseLong(m.group(2));
                    z = Long.parseLong(m.group(3));
                } else {
                    throw new RuntimeException(split[0]);
                }

                m = p.matcher(split[1]);
                if(m.find()) {
                    velX = Long.parseLong(m.group(1));
                    velY = Long.parseLong(m.group(2));
                    velZ = Long.parseLong(m.group(3));
                } else {
                    throw new RuntimeException(split[1]);
                }

                m = p.matcher(split[2]);
                if(m.find()) {
                    accelX = Long.parseLong(m.group(1));
                    accelY = Long.parseLong(m.group(2));
                    accelZ = Long.parseLong(m.group(3));
                } else {
                    throw new RuntimeException(split[2]);
                }

                particles.add(new Particle(i, x, y, z, velX, velY, velZ, accelX, accelY, accelZ));
            }

            for(int i=0; i<100000; i++) {
                int id = -1;
                long distance = Long.MAX_VALUE;
                Map<String, Particle> positionsToParticle = new HashMap<>();

                for(Particle particle : particles) {
                    if(removeCollisions && particle.collided) {
                        continue;
                    }
                    if(particle.distanceFromZero() <= distance) {
                        distance = particle.distanceFromZero();
                        id = particle.getId();
                    }
                    particle.move();
                    if(removeCollisions) {
                        String position = particle.x + "," + particle.y + "," + particle.z;
                        if(positionsToParticle.containsKey(position)) {
                            particle.collided();
                            positionsToParticle.get(position).collided();
                        } else {
                            positionsToParticle.put(position, particle);
                        }
                    }
                }

                particleToClosest.put(id, particleToClosest.getOrDefault(id, 0) + 1);
            }

            System.out.println("PARTICLES LEFT AFTER COLLISIONS: " + particles.stream().filter(particle -> !particle.collided).toList().size());
            int maxId = -1;
            for(int id : particleToClosest.keySet()) {
                if(maxId < 0) {
                    maxId = id;
                } else if(particleToClosest.get(id) > particleToClosest.get(maxId)) {
                    maxId = id;
                }
            }
            return maxId;
        }
    }

    public static class Particle {
        private final int id;
        private long x;
        private long y;
        private long z;
        private long velX;
        private long velY;
        private long velZ;
        private final long accelX;
        private final long accelY;
        private final long accelZ;

        private boolean collided = false;

        public Particle(int id, long x, long y, long z, long velX, long velY, long velZ, long accelX,
                        long accelY,
                        long accelZ) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.z = z;
            this.velX = velX;
            this.velY = velY;
            this.velZ = velZ;
            this.accelX = accelX;
            this.accelY = accelY;
            this.accelZ = accelZ;
        }

        public void collided() {
            collided = true;
        }

        public int getId() {
            return id;
        }

        public void move() {
            velX += accelX;
            velY += accelY;
            velZ += accelZ;
            x += velX;
            y += velY;
            z += velZ;
        }

        public long distanceFromZero() {
            return Math.abs(x) + Math.abs(y) + Math.abs(z);
        }
    }
}
