package csparksfly.gradle;

public class MavenModule implements Comparable<MavenModule> {

    private final String fullModuleSpec;
    private final String groupId;
    private final String artifactId;
    private final String version;

    public MavenModule(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId=artifactId;
        this.version=version;
        this.fullModuleSpec = groupId+":"+artifactId+":"+version;
    }

    public static MavenModule FromFullSpec(String fullModuleSpec){
        String[] parts = fullModuleSpec.split(":");
        String groupId = "";
        String artifactId;
        String version = "";
        if(parts.length == 3) {
            groupId = parts[0];
            artifactId = parts[1];
            version = parts[2];
        }else if(parts.length == 2){
            artifactId=parts[0];
            version=parts[1];
        }else{
            artifactId=parts[0];
        }
        return new MavenModule(groupId, artifactId, version);
    }

    public String getFullModuleSpec() {
        return fullModuleSpec;
    }

    @Override
    public String toString() {
        return getFullModuleSpec();
    }

    @Override
    public int compareTo(MavenModule o) {
        return fullModuleSpec.compareTo(o.fullModuleSpec);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MavenModule && fullModuleSpec.equals(((MavenModule) obj).getFullModuleSpec());
    }
}
