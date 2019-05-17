import java.util.List;

public class Dependency {

    private String groupId = "";
    private String artifactId = "";
    private VersionAndScope versionAndScope;
    private Dependency child;
    private int layer = 1;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String actifactId) {
        this.artifactId = actifactId;
    }

    public Dependency getChild() {
        return child;
    }

    public void setChild(Dependency child) {
        this.child = child;
        child.layer = this.layer + 1;
    }

    public VersionAndScope getVersionAndScope() {
        return versionAndScope;
    }

    public void setVersionAndScope(VersionAndScope versionAndScope) {
        this.versionAndScope = versionAndScope;
    }

    public int getLayer() {
        return layer;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Dependency)) return false;
        else{
            Dependency o = (Dependency)obj;
            return this.groupId.equals(o.groupId) &&
                    this.artifactId.equals(o.artifactId);
        }
    }

    @Override
    public int hashCode() {
        return groupId.hashCode() * 31 + artifactId.hashCode();
    }

    @Override
    public String toString() {
        return groupId + ":" + artifactId;
    }

    public static void addModule(List<Dependency> modules, String groupId, String artifactId) {
        if (null == modules) {
            return;
        }
        Dependency dependency = new Dependency();
        dependency.setGroupId(groupId);
        dependency.setArtifactId(artifactId);
        modules.add(dependency);
    }
}
