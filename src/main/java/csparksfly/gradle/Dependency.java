package csparksfly.gradle;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.TopologicalOrderIterator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class Dependency {
    public MavenModule getFrom() {
        return from;
    }

    public MavenModule getTo() {
        return to;
    }

    private final MavenModule from;
    private final MavenModule to;

    public Dependency(MavenModule from, MavenModule to) {
     this.from = from;
     this.to = to;
    }


    /*

    from here: https://stackoverflow.com/questions/36536561/is-there-a-way-to-list-all-gradle-dependencies-programatically

    task printDeps {
    doLast {
      configurations.compile.incoming.getResolutionResult().getAllDependencies().each { depResult  ->
      println "from:" + depResult.getFrom() + " requested:" + depResult.getRequested() + ""
      }
     }
    }

     */

    private static String parseModuleNameFrom(String rawString){
        String candidate = rawString
                .replace("from:","")
                .replace("project :","")
                .trim();
        if (candidate.isEmpty())
            candidate = "rootProject";
        return candidate;
    }

    public static Dependency FromLine(String rawLine, TreeMap<MavenModule, MavenModule> modules){

        String[] fromAndTo = rawLine.split("requested:");
        MavenModule fromModule = FromMapOrNew(MavenModule.FromFullSpec(parseModuleNameFrom(fromAndTo[0])), modules);
        MavenModule dependencyModule = FromMapOrNew(MavenModule.FromFullSpec(parseModuleNameFrom(fromAndTo[1])), modules);

        return new Dependency(fromModule, dependencyModule);
    }

    private static MavenModule FromMapOrNew(MavenModule candidateMavenModule, TreeMap<MavenModule, MavenModule> modules) {
        if (modules.containsKey(candidateMavenModule))
            candidateMavenModule = modules.get(candidateMavenModule);
        else
            modules.put(candidateMavenModule, candidateMavenModule);
        return candidateMavenModule;
    }

    public static List<Dependency> FromFile(String filename) throws IOException {

        List<Dependency> dependencies = new LinkedList<>();

        TreeMap<MavenModule, MavenModule> modules = new TreeMap<>();
        List<String> allLines = Files.readAllLines(Paths.get(filename));
        for (String rawLine : allLines) {
            if(rawLine.startsWith("from"){
                dependencies.add(Dependency.FromLine(rawLine, modules));
            }
        }

        return dependencies;

    }

    public static Graph<MavenModule, DefaultEdge> Create(List<Dependency> dependencies){
        Graph<MavenModule, DefaultEdge> graph = new DirectedAcyclicGraph<>(DefaultEdge.class);

        for (Dependency dep: dependencies) {
            graph.addVertex(dep.getFrom());
            graph.addVertex(dep.getTo());
            graph.addEdge(dep.getFrom(), dep.getTo());
        }

        return graph;
    }

    @Override
    public String toString() {
        return from.getFullModuleSpec() + "    ->    "+to.getFullModuleSpec();
    }


    public static void main(String[] args) throws Exception{

        List<Dependency> dependencies = Dependency.FromFile("src/test/resources/deps.txt");

        Graph<MavenModule, DefaultEdge> graph = Dependency.Create(dependencies);

        List<MavenModule> sortedTopological = new LinkedList<>();
        Iterator<MavenModule> iter = new TopologicalOrderIterator<>(graph);
        while (iter.hasNext()) {
            sortedTopological.add(iter.next());
        }

        System.out.println("Sorted Topological:");
        System.out.println("===================");
        sortedTopological.stream().forEach(s-> System.out.println(s));

        System.out.println("\nDetails:");
        System.out.println("========");

        for(MavenModule vertex : sortedTopological){
            //System.out.println("Vertex " + vertex + " is connected to: " + graph.edgesOf(vertex).toString());

            StringJoiner sj = new StringJoiner(" -> ");
            Iterator<MavenModule> iterator = new BreadthFirstIterator<>(graph, vertex);
            while (iterator.hasNext()) {
                sj.add(iterator.next().toString());
            }
            System.out.println(sj.toString());
        }

    }
}
