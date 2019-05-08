# POM管理方案

## 管理依赖的插件
* Idea: Maven Helper

## Maven-shade-plugin
对依赖包重命名，达到依赖隔离的目的。
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-shade-plugin</artifactId>
    <version>3.2.1</version>
    <executions>
        <execution>
            <phase>package</phase>
            <goals><goal>shade</goal></goals>
            <configuration>
                <minimizeJar>true</minimizeJar>
                <relocations>
                    <relocation>
                        <pattern>org.apache.util</pattern>
                        <!--{artifactId} is your project's specify id-->
                        <shadedPattern>transwarp.{artifactId}.apache.util</shadedPattern>
                        <excludes>
                            <!--if you want to exclude some package-->
                        </excludes>
                    </relocation>
                    <relocation>
                        ...
                    </relocation>
                </relocations>
            </configuration>
        </execution>
    </executions>
</plugin>
```

* **优点**：简单，快速解决冲突问题; 允许引入不同版本的依赖
* **缺点**：插件会将依赖都打包进来，jar包变得庞大?

## Maven-assembly-plugin


* **优点**：有针对性地管理依赖，jar干净无冗余
* **缺点**：需要一一排除冲突包，耗费时间；有可能除掉别的包所依赖的版本

## Maven继承
利用maven自带的继承属性，为所有项目建立一个总的pom，再建立一个公共依赖库，在公共依赖库中管理依赖包的版本。  
其中大致结构可以如下所示，其中parent是全局的父pom将dependencies、module1、module2聚合在一起，dependencies作为管理依赖的pom，module1和module2都继承于它
```
parent(pom)
  |
  | --- dependencies(pom)
  | --- module1
  |        | --- module1-denpendencies(pom)
  |        | --- module1.1
  | --- module2
  | --- ...
```
每个项目也可以自建一个公共依赖库，但必须遵循父依赖库。  
只作为依赖管理的pom，packaging要设为pom
如果不同依赖之间存在包冲突，需要exclude掉，保持整套项目没有冲突
* **优点**：一劳永逸，统一管理
* **缺点**：大工程