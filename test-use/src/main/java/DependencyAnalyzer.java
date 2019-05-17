import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DependencyAnalyzer {

    /**
     * 利用maven dependency:list罗列出来的依赖，进行整理分析
     * @param dependenciesList
     * @param onlyConflict
     */
    public static void checkConflict(File dependenciesList, boolean onlyConflict) {
        if(!dependenciesList.exists() || dependenciesList.isDirectory()){
            return;
        }

        Map<Dependency, Map<VersionAndScope, List<String>>> list = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dependenciesList));
            String line = null;
            String module = null;
            while ((line = reader.readLine()) != null) {
                if (!line.contains("[INFO]")) {
                    continue;
                }
                line = line.replace("[INFO]", "").trim();
                if(line.contains("maven-dependency-plugin")){
                    module = line.split("@")[1].replaceAll("-","").trim();
                }else {
                    String[] info = line.split(":");
                    if (info.length >= 5) {
                        Dependency dependency = new Dependency();
                        dependency.setGroupId(info[0]);
                        dependency.setArtifactId(info[1]);
                        VersionAndScope vns = new VersionAndScope();
                        if(info.length == 6) {
                            vns.setVersion(info[4]);
                            vns.setScope(info[5]);
                        }else {
                            vns.setVersion(info[3]);
                            vns.setScope(info[4]);
                        }
                        if (!list.containsKey(dependency)) {
                            list.put(dependency, new HashMap<>());
                        }
                        Map<VersionAndScope, List<String>> conflict = list.get(dependency);
                        if (!conflict.containsKey(vns)) {
                            conflict.put(vns, new ArrayList<>());
                        }
                        conflict.get(vns).add(module);
                    }else {
                        if(info.length > 1) System.out.println(line);
                    }
                }
            }
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<Dependency, Map<VersionAndScope, List<String>>> dependency : list.entrySet()) {
                if (onlyConflict && dependency.getValue().size() == 1) {
                    continue;
                }
                int i = 0;
                for (Map.Entry<VersionAndScope, List<String>> vns : dependency.getValue().entrySet()) {
                    for (int j = 0; j < vns.getValue().size(); j++) {
                        String l = "";
                        if (i == 0 && j == 0) {
                            l = dependency.getKey().toString();
                        }
                        l += ",";
                        if (j == 0) {
                            l += vns.getKey().toString();
                        }
                        l += "," + vns.getValue().get(j) + "\n";
                        sb.append(l);
                    }
                    i++;
                }
            }
            FileWriter writer = new FileWriter("result.csv");
            writer.write(sb.toString());
            writer.flush();
            writer.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 利用enforcer罗列出来的依赖冲突，进行整理分析
     * @param dependencyConflict enforcer输出到文件中的冲突列表
     * @param layer 希望展示的层次深度, -1表示全部展开
     * @param modules 在间接引用中不希望出现的module
     * @param checkTranswarp 是否排除间接引用中属于transwarp的module
     */
    public static void checkConflictFromEnforcer(File dependencyConflict, String outputFilename, int layer, List<Dependency> modules, boolean checkTranswarp) {
        if (!dependencyConflict.exists() || dependencyConflict.isDirectory()) {
            return;
        }
        if (null == modules) {
            modules = new ArrayList<>();
        }

        Map<Dependency, Map<VersionAndScope,List<Dependency>>> list = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dependencyConflict));
            String line = null;
            int flag = -1;
            int flagLayer = 0;
            Dependency now = null, key = null;
            while ((line = reader.readLine()) != null) {
                if(flag == 1 && !line.trim().startsWith("+-")){
                    flagLayer = 0;
                    flag = 0;
                    if (now != null && !modules.contains(now.getChild())) {
                        Map<VersionAndScope, List<Dependency>> vns = list.get(key);
                        Dependency child = now;
                        while (child.getChild() != null){child = child.getChild();}
                        if (!vns.containsKey(child.getVersionAndScope())) {
                            vns.put(child.getVersionAndScope(), new ArrayList<>());
                        }
                        vns.get(child.getVersionAndScope()).add(now);
                        now = null;
                    }
                }
                if (line.trim().startsWith("Dependency convergence error for")) {
                    flag = 1;
                    String[] info = line.replace("Dependency convergence error for", "").replace("paths to dependency are:", "").trim().split(":");
                    key = new Dependency();
                    key.setGroupId(info[0]);
                    key.setArtifactId(info[1]);
                    list.put(key, new HashMap<>());
                } else if ("and".equals(line.trim())) {
                    flag = 1;
                } else if (line.trim().startsWith("+-") && flag == 1) {
                    if (flagLayer == 0) {
                        flagLayer ++;
                        continue;
                    } else if(flagLayer > 1 && !checkTranswarp && (line.contains("transwarp") || line.contains("inceptor"))){
                        flag = 0;
                        flagLayer = 0;
                        now = null;
                        continue;
                    }
                    String[] info = line.trim().replace("+-", "").split(":");
                    Dependency d = new Dependency();
                    d.setGroupId(info[0]);
                    d.setArtifactId(info[1]);
                    VersionAndScope vns = new VersionAndScope();
                    vns.setVersion(info[2]);
                    d.setVersionAndScope(vns);
                    if (now != null) {
                        Dependency child = now;
                        while (child.getChild() != null) {child = child.getChild();}
                        child.setChild(d);
                    }else {
                        now = d;
                    }
                    flagLayer ++;
                } else {
                    System.out.println(line);
                }
            }

            FileWriter writer = new FileWriter(outputFilename + ".csv");
            StringBuilder sb = new StringBuilder();
            sb.append("冲突的依赖,版本号,第一层引用者,第二层引用者,第三层引用者,第四层引用者,第五层引用者\n");
            for (Map.Entry<Dependency, Map<VersionAndScope, List<Dependency>>> dependencies : list.entrySet()) {
                int i = 0;
                for (Map.Entry<VersionAndScope,List<Dependency>> vns : dependencies.getValue().entrySet()) {
                    int k = 0;
                    for (Dependency dependency : vns.getValue()) {
                        String l = "";
                        if (i == 0 && k == 0) {
                            l = dependencies.getKey().toString();
                        }
                        l += ",";
                        if (k == 0) {
                            l += vns.getKey().toString();
                        }
                        l += "," + dependency.toString() + ":" + (dependency.getVersionAndScope() == null? "":dependency.getVersionAndScope().toString());
                        Dependency child = dependency;
                        int flagLayer2 = 1;
                        while (child.getChild() != null) {
                            child = child.getChild();
                            if (dependencies.getKey().equals(child)) {
                                if(layer > 0) {
                                    for (int j = 0; j < (layer - flagLayer2); j++) {
                                        l += ",";
                                    }
                                }
                                break;
                            }
                            if (flagLayer2 < layer || layer < 0) {
                                l += "," + child.toString() + ":" + (child.getVersionAndScope() == null? "":child.getVersionAndScope().toString());
                            }
                            flagLayer2 ++;
                        }
                        l += "\n";
                        sb.append(l);
                        k++;
                    }
                    i ++;
                }
            }
            writer.write(sb.toString());
            writer.flush();
            writer.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
