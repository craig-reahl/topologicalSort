package csparksfly.gradle;

public class Dependency {
    public Module getFrom() {
        return from;
    }

    public Module getTo() {
        return to;
    }

    private final Module from;
    private final Module to;

    public Dependency(Module from, Module to) {
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

    private static String parseModuleFrom(String rawString){
        String candidate = rawString
                .replace("from:","")
                .replace("project :","")
                .trim();
        if (candidate.isEmpty())
            candidate = "rootProject";
        return candidate;
    }

    public static Dependency FromLine(String rawLine){

        String[] fromAndTo = rawLine.split("requested:");
        String fromString = parseModuleFrom(fromAndTo[0]);
        String dependencyString = parseModuleFrom(fromAndTo[1]);

        return new Dependency(new Module(fromString), new Module(dependencyString));
    }

    @Override
    public String toString() {
        return from.getName() + "    ->    "+to.getName();
    }
}
