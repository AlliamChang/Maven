import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        List<Dependency> modules = new ArrayList<>();
        Dependency.addModule(modules, "io.transwarp", "hyperdrive");
        Dependency.addModule(modules, "io.transwarp", "shivakvdrive");
        Dependency.addModule(modules, "io.transwarp", "shapedrive");
        Dependency.addModule(modules, "io.transwarp", "esdrive");
        Dependency.addModule(modules, "io.transwarp", "jdbcdrive");
        Dependency.addModule(modules, "io.transwarp", "mockdb");
        Dependency.addModule(modules, "io.transwarp", "stellardbdrive");
        Dependency.addModule(modules, "io.transwarp", "graphsearch-hive");
        Dependency.addModule(modules, "io.transwarp", "inceptor-base");
        Dependency.addModule(modules, "io.transwarp", "inceptor-batch-operator");
        Dependency.addModule(modules, "io.transwarp", "inceptor_2.10");
        Dependency.addModule(modules, "org.apache.hive", "inceptor-metastore");
        Dependency.addModule(modules, "org.apache.hive", "hive-exec");
        Dependency.addModule(modules, "org.apache.hive", "hive-ant");
        Dependency.addModule(modules, "org.apache.hive", "hive-cli");
        Dependency.addModule(modules, "org.apache.hive", "hive-common");
        Dependency.addModule(modules, "org.apache.hive", "hive-hwi");
        Dependency.addModule(modules, "org.apache.hive", "hive-contrib");
        Dependency.addModule(modules, "org.apache.hive", "hive-serde");
        Dependency.addModule(modules, "org.apache.hive", "hive-service");
        Dependency.addModule(modules, "org.apache.hive", "hive-shims");
        DependencyAnalyzer.checkConflictFromEnforcer(new File("conflict"), "dependency-conflict", -1, modules, true);
    }
}
