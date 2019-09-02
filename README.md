# TopologicalSort

Someone needed to be able to list gradle dependencies in this way.

By adding a gradle task that prints out the dependencies in a convenient way,
this code can be used to sort the dependencies [https://en.wikipedia.org/wiki/Topological_sorting](topologically) 
and present them.

## Create a gradle task for your project

Reference from [https://stackoverflow.com/questions/36536561/is-there-a-way-to-list-all-gradle-dependencies-programatically](https://stackoverflow.com/questions/36536561/is-there-a-way-to-list-all-gradle-dependencies-programatically)
```
    task printDeps {
    doLast {
      configurations.compile.incoming.getResolutionResult().getAllDependencies().each { depResult  ->
      println "from:" + depResult.getFrom() + " requested:" + depResult.getRequested() + ""
      }
     }
    }
```

run the gradle task and ssave the output:
```
 gradle printDeps >  /tmp/deps.txt
```

then you need to write something like this to parse it [for example](https://github.com/craig-reahl/topologicalSort/blob/master/src/main/java/csparksfly/gradle/DependencyTopologicalSort.java):

```
    List<Dependency> dependencies = Dependency.FromFile("src/test/resources/deps.txt");

        Graph<MavenModule, DefaultEdge> graph = Dependency.Create(dependencies);

        List<MavenModule> sortedTopological = new LinkedList<>();
        Iterator<MavenModule> iter = new TopologicalOrderIterator<>(graph);
        while (iter.hasNext()) {
            System.out.println(s);
        }
```
