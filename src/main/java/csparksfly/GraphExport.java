package csparksfly;

import csparksfly.gradle.Dependency;
import csparksfly.gradle.MavenModule;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.io.ComponentNameProvider;
import org.jgrapht.io.DOTExporter;
import org.jgrapht.io.ExportException;
import org.jgrapht.io.GraphExporter;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GraphExport {

    public static void Dot(Graph<MavenModule, DefaultEdge> graph, String filename) throws ExportException, IOException {


        // adhering to the DOT language restrictions
        ComponentNameProvider<MavenModule> vertexIdProvider = new ComponentNameProvider<MavenModule>()
        {
            public String getName(MavenModule module)
            {
                //return name.replace('.', '_').replace(":","_");
                return ""+module.hashCode();
            }
        };
        ComponentNameProvider<MavenModule> vertexLabelProvider = new ComponentNameProvider<MavenModule>()
        {
            public String getName(MavenModule module)
            {
                return module.getFullModuleSpec();
            }
        };
        GraphExporter<MavenModule, DefaultEdge> exporter =
                new DOTExporter<>(vertexIdProvider, vertexLabelProvider, null);
        Writer writer = new StringWriter();
        exporter.exportGraph(graph, writer);

        Path path = Paths.get(filename);
        byte[] strToBytes = writer.toString().getBytes();

        Files.write(path, strToBytes);

        System.out.println(writer.toString());
    }

    public static void main(String[] args) throws Exception{

        List<Dependency> dependencies = Dependency.FromFile("src/test/resources/deps2.txt");

        Graph<MavenModule, DefaultEdge> graph = Dependency.Create(dependencies);

        GraphExport.Dot(graph, "src/test/resources/sample.dot" );
        //dot -Tpng src/test/resources/sample.dot  -o src/test/resources/sample.png

    }
}
