# Maven依赖管理
多模块开发的时候，会存在依赖版本冲突的问题，这是因为每个模块都拥有属于自己的maven依赖管理文件。
根据资料的查阅，找到了一些依赖管理的方法，为了记录下来并作为参考，创建了该项目。
## 管理方案
[maven依赖管理方法](maven.md)  
[maven依赖管理.ppt](Maven依赖管理.odp)
## 项目参考
* [SpringBoot依赖管理](https://github.com/spring-projects/spring-boot)
* 本项目建立了一个比较简单的基于继承的依赖管理项目

## 依赖排查小工具
为了更方便明了地排查依赖冲突问题，本项目还编写了一个小程序，可以将冲突的依赖以表格形式展示。  
小程序主体在test-use包下  
使用说明：
1. 将所有需要排查的module都引入到test-use的[pom](test-use/pom.xml)里
2. 使用`maven clean package > filename`命令打包，利用enforcer插件的特性，将冲突全部列出来，同时输出到文件中
3. 将冲突列表文件加入到[排查入口](test-use/src/main/java/Test.java)代码中，修改方法`DependencyAnalyzer.checkConflictFromEnforcer()`中的参数
4. 运行代码，最终会将整理结果输出到自己定义的**outputFilename**文件中，csv格式存储
