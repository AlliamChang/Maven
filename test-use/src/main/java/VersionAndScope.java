public class VersionAndScope {

    private String version = "";
    private String scope = "";


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof VersionAndScope)) return false;
        else{
            VersionAndScope o = (VersionAndScope) obj;
            return this.version.equals(o.version) &&
                    this.scope.equals(o.scope);
        }
    }

    @Override
    public int hashCode() {
        return version.hashCode() * 31 + scope.hashCode();
    }

    @Override
    public String toString() {
        return version + "["+ scope +"]";
    }
}
