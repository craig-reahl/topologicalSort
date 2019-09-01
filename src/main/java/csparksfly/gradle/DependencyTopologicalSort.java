package csparksfly.gradle;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.traverse.TopologicalOrderIterator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DependencyTopologicalSort {


    public static List<String> Show(List<Dependency> dependencies){

        Graph<String, DefaultEdge> graph = new DirectedAcyclicGraph<>(DefaultEdge.class);

        for (Dependency dep: dependencies) {
            graph.addVertex(dep.getFrom().getName());
            graph.addVertex(dep.getTo().getName());
            graph.addEdge(dep.getFrom().getName(), dep.getTo().getName());
        }

        List<String> sorted = new LinkedList<>();
        Iterator<String> iterator = new TopologicalOrderIterator<>(graph);
        while (iterator.hasNext()) {
            String s = iterator.next();
            sorted.add(s);
        }

        return sorted;
    }

    public static void main(String[] args) throws Exception {

        List<Dependency> dependencies = DependencyBuilder.FromFile("src/test/resources/deps.txt");


        List<String> sorted = Show(dependencies);
        for(String s : sorted) {
            System.out.println(s);
        }

    }

}
