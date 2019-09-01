package csparksfly.gradle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class DependencyBuilder {


    public static List<Dependency> FromFile(String filename) throws IOException {

        List<Dependency> dependencies = new LinkedList<>();

        List<String> allLines = Files.readAllLines(Paths.get(filename));
        for (String rawLine : allLines) {
            dependencies.add(Dependency.FromLine(rawLine));
        }

        return dependencies;

    }


    public static void main(String[] args) throws Exception{

        for (Dependency dep : FromFile("src/test/resources/deps.txt")
             ) {
            System.out.println(dep);
        }
        System.out.println("Done");

    }
}
