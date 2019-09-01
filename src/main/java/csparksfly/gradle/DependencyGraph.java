package csparksfly.gradle;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;

import java.util.List;

public class DependencyGraph {

    public static Graph<String, DefaultEdge> Create(List<Dependency> dependencies){
        Graph<String, DefaultEdge> graph = new DirectedAcyclicGraph<>(DefaultEdge.class);

        for (Dependency dep: dependencies) {
            graph.addVertex(dep.getFrom().getName());
            graph.addVertex(dep.getTo().getName());
            graph.addEdge(dep.getFrom().getName(), dep.getTo().getName());
        }

        return graph;
    }
}
