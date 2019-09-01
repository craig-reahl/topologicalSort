package csparksfly.gradle;

import csparksfly.GraphExport;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.traverse.TopologicalOrderIterator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DependencyTopologicalSort {


    public static List<String> Show(List<Dependency> dependencies){

        Graph<String, DefaultEdge> graph = DependencyGraph.Create(dependencies);

        List<String> sorted = new LinkedList<>();
        Iterator<String> iterator = new TopologicalOrderIterator<>(graph);
        while (iterator.hasNext()) {
            String s = iterator.next();
            sorted.add(s);
        }

        return sorted;
    }

    public static void main(String[] args) throws Exception {

        List<Dependency> dependencies = DependencyBuilder.FromFile("src/test/resources/deps3.txt");

        List<String> sorted = DependencyTopologicalSort.Show(dependencies);
        for(String s : sorted) {
            System.out.println(s);
        }

        List<String> froms = dependencies.stream().map(d->d.getFrom().getName()).collect(Collectors.toList());
        Set<String> tos = dependencies.stream().map(d->d.getTo().getName()).filter(d->!froms.contains(d)).collect(Collectors.toSet());

        System.out.println("\n\nNo Deps:");
        for (String s : tos){
            System.out.println(s);
        }

    }

}
