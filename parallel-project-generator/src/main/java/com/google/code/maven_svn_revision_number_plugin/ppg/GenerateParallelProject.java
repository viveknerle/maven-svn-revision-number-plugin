/*-
 * Copyright (c) 2012, Oleg Estekhin
 * All rights reserved.
 */

package com.google.code.maven_svn_revision_number_plugin.ppg;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public class GenerateParallelProject {

    public static void main( String[] arguments ) throws IOException, TemplateException {
        if ( arguments.length < 1 ) {
            System.out.printf( "Usage: java %s basedir <depth> <children> <plugin>%n", GenerateParallelProject.class.getName() );
            return;
        }
        File basedir = new File( arguments[ 0 ] );
        int depth = arguments.length > 1 ? Integer.parseInt( arguments[ 1 ] ) : 2;
        int children = arguments.length > 2 ? Integer.parseInt( arguments[ 2 ] ) : 3;
        String pluginVersion = arguments.length > 3 ? arguments[ 3 ] : "1.14-SNAPSHOT";

        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading( GenerateParallelProject.class, "" );

        createAggregatorProject( basedir, "com.google.code.maven-svn-revision-number-plugin.it", "parallel", depth, children, pluginVersion, configuration );
    }


    private static void createAggregatorProject( File basedir, String groupId, String projectId, int depth, int children, String pluginVersion, Configuration configuration ) throws IOException, TemplateException {
        System.out.printf( "creating aggregator project %s:%s%n", groupId, projectId );

        basedir.mkdirs();
        Writer writer = new OutputStreamWriter( new FileOutputStream( new File( basedir, "pom.xml" ) ), "UTF-8" );
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put( "groupId", groupId );
            data.put( "projectId", projectId );
            data.put( "children", children );
            data.put( "pluginVersion", pluginVersion );
            configuration.getTemplate( "AggregatorPom.ftl" ).process( data, writer );
        } finally {
            try {
                writer.close();
            } catch ( IOException ignored ) {
            }
        }

        for ( int i = 1; i <= children; i++ ) {
            String childId = projectId + '-' + i;
            if ( depth > 0 ) {
                createParentProject( new File( basedir, childId ), groupId, childId, projectId, depth - 1, children, configuration );
            } else {
                createLeafProject( new File( basedir, childId ), groupId, childId, projectId, configuration );
            }
        }
    }

    private static void createParentProject( File basedir, String groupId, String projectId, String parentId, int depth, int children, Configuration configuration ) throws IOException, TemplateException {
        System.out.printf( "creating parent project %s:%s%n", groupId, projectId );

        basedir.mkdirs();
        Writer writer = new OutputStreamWriter( new FileOutputStream( new File( basedir, "pom.xml" ) ), "UTF-8" );
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put( "groupId", groupId );
            data.put( "parentId", parentId );
            data.put( "projectId", projectId );
            data.put( "children", children );
            configuration.getTemplate( "ParentPom.ftl" ).process( data, writer );
        } finally {
            try {
                writer.close();
            } catch ( IOException ignored ) {
            }
        }

        for ( int i = 1; i <= children; i++ ) {
            String childId = projectId + '-' + i;
            if ( depth > 0 ) {
                createParentProject( new File( basedir, childId ), groupId, childId, projectId, depth - 1, children, configuration );
            } else {
                createLeafProject( new File( basedir, childId ), groupId, childId, projectId, configuration );
            }
        }
    }

    private static void createLeafProject( File basedir, String groupId, String projectId, String parentId, Configuration configuration ) throws IOException, TemplateException {
        System.out.printf( "creating leaf project %s:%s%n", groupId, projectId );

        basedir.mkdirs();
        Writer writer = new OutputStreamWriter( new FileOutputStream( new File( basedir, "pom.xml" ) ), "UTF-8" );
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put( "groupId", groupId );
            data.put( "parentId", parentId );
            data.put( "projectId", projectId );
            configuration.getTemplate( "LeafPom.ftl" ).process( data, writer );
        } finally {
            try {
                writer.close();
            } catch ( IOException ignored ) {
            }
        }

        String javaName = projectId.replace( '-', '_' );

        File srcMain = new File( basedir, "src" + File.separator + "main" + File.separator + "java" + File.separator + javaName );
        srcMain.mkdirs();
        createClass( new File( srcMain, javaName + ".java" ), javaName, configuration, "LeafMainClass.ftl" );

        File srcTest = new File( basedir, "src" + File.separator + "test" + File.separator + "java" + File.separator + javaName );
        srcTest.mkdirs();
        createClass( new File( srcTest, javaName + "Test.java" ), javaName, configuration, "LeafTestClass.ftl" );
    }

    private static void createClass( File file, String javaName, Configuration configuration, String templateName ) throws IOException, TemplateException {
        Writer writer = new OutputStreamWriter( new FileOutputStream( file ), "UTF-8" );
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put( "javaName", javaName );
            configuration.getTemplate( templateName ).process( data, writer );
        } finally {
            try {
                writer.close();
            } catch ( IOException ignored ) {
            }
        }
    }

}
