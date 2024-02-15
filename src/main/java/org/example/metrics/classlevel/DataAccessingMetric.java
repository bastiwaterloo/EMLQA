package org.example.metrics.classlevel;

import com.github.javaparser.JavaParser;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithAccessModifiers;

import java.io.File;
import java.io.FileInputStream;

public class DataAccessingMetric {
    public static double getDAM(String filepath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filepath);
            CompilationUnit cu = StaticJavaParser.parse(fileInputStream);

            FieldVisitor fieldVisitor = new FieldVisitor();
            fieldVisitor.visit(cu, null);

            System.out.println("Public Fields Count: " + fieldVisitor.getPublicCount());
            System.out.println("Private Fields Count: " + fieldVisitor.getPrivateCount());
            if ((fieldVisitor.getPrivateCount() + fieldVisitor.getPublicCount()) != 0){
                return fieldVisitor.getPrivateCount() / (fieldVisitor.getPrivateCount() + fieldVisitor.getPublicCount());
            }
            return 0;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static double getAvgDAM(String projectPath){
        File dir = new File(projectPath);
        double totalDAM = 0;
        int numOfClasses = dir.listFiles().length;
        for(File file : dir.listFiles()){
            totalDAM += getDAM(file.getAbsolutePath());
        }
        return totalDAM / numOfClasses;
    }

    private static class FieldVisitor extends com.github.javaparser.ast.visitor.VoidVisitorAdapter<Void> {
        private int publicCount = 0;
        private int privateCount = 0;

        @Override
        public void visit(FieldDeclaration n, Void arg) {
            super.visit(n, arg);

            if (n instanceof NodeWithAccessModifiers) {
                NodeWithAccessModifiers<?> nodeWithAccessModifiers = (NodeWithAccessModifiers<?>) n;
                if (nodeWithAccessModifiers.isPublic()) {
                    publicCount++;
                } else if (nodeWithAccessModifiers.isPrivate() || nodeWithAccessModifiers.isProtected()) {
                    privateCount++;
                }
            }
        }

        public int getPublicCount() {
            return publicCount;
        }

        public int getPrivateCount() {
            return privateCount;
        }
    }
}
