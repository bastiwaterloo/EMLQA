package org.example.metrics.packagelevel;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Abstractness {
    public static boolean containsAbstractClass(String filePath) {
        try {
            FileInputStream file = new FileInputStream(filePath);
            CompilationUnit cu = StaticJavaParser.parse(file);

            AbstractClassVisitor visitor = new AbstractClassVisitor();
            visitor.visit(cu, null);

            return visitor.containsAbstractClass();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false; // Rückgabe im Fehlerfall
        }
    }

    public static double getAbstractness(String projectPath) {
        // Beispiel: Ein Pfad zur Java-Datei
        File[] files = new File(projectPath).listFiles();
        double abstractClasses = 0;
        double totalClasses = 0;
        for(File file : files){
            String filePath = file.getAbsolutePath();
            boolean hasAbstractClass = containsAbstractClass(filePath);
            if (hasAbstractClass) {
                abstractClasses++;
            }
            totalClasses++;
        }
        return abstractClasses / totalClasses;
    }

    private static class AbstractClassVisitor extends com.github.javaparser.ast.visitor.VoidVisitorAdapter<Void> {
        private boolean containsAbstract = false;

        public boolean containsAbstractClass() {
            return containsAbstract;
        }

        @Override
        public void visit(ClassOrInterfaceDeclaration cid, Void arg) {
            if (cid.isAbstract()) {
                containsAbstract = true;
            }
        }
    }
}
