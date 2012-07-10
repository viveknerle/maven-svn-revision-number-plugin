<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>${groupId}</groupId>
    <artifactId>${projectId}</artifactId>
    <version>1.0</version>
    <packaging>pom</packaging>

    <modules>
    <#list 1..children as i>
        <module>${projectId}-${i}</module>
    </#list>
    </modules>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    </properties>

    <build>
        <plugins>
            <plugin>
                <groupId>com.google.code.maven-svn-revision-number-plugin</groupId>
                <artifactId>svn-revision-number-maven-plugin</artifactId>
                <version>${pluginVersion}</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>revision</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
