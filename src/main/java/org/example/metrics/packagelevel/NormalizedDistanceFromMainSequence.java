package org.example.metrics.packagelevel;

import java.io.File;
import java.io.IOException;

public class NormalizedDistanceFromMainSequence {

    public static double calculateDistanceFromMainSequence(String packagePath) {
        double y = Abstractness.getAbstractness(packagePath);
        double x = Instability.getInstability(packagePath);

        double optimalY = -x + 1;

        double distance = optimalY - y;

        //main sequence forumla is y=-x + 1
        //if distance = 0 --> package is on main sequence --> optimal balance
        return distance;
    }
}
