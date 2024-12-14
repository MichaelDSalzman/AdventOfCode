package aoc2024.day14;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    private final String fileName;

    public FileReader(String fileName) {
        this.fileName = fileName;
    }

    public List<String> getLines() throws IOException {

        java.io.FileReader fileReader = new java.io.FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        List<String> lines = new ArrayList<>();
        String line = bufferedReader.readLine();

        while(line != null) {
            lines.add(line);
            line = bufferedReader.readLine();
        }

        return lines;
    }
}
