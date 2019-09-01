package csparksfly;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.traverse.TopologicalOrderIterator;

import java.util.Iterator;

public class TopologicalSortStringExample {

    public static void main(String[] args) {
        Graph<String, DefaultEdge> graph = new DirectedAcyclicGraph<>(DefaultEdge.class);

        graph.addVertex("root");
        graph.addVertex("a");
        graph.addVertex("b");
        graph.addVertex("c");
        graph.addVertex("d");

        graph.addEdge("root", "a");
        graph.addEdge("a", "b");
        graph.addEdge("b", "c");
        graph.addEdge("c", "d");
        graph.addEdge("b", "d");

        Iterator<String> iterator = new TopologicalOrderIterator<String, DefaultEdge>(graph);
        while (iterator.hasNext()) {
            String  s = iterator.next();
            System.out.println(s);
        }

    }
}
