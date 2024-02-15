package org.example;

import java.util.HashMap;

public class TQICalculator {
    public static HashMap<String, Double> calculateTQI(double dsc, double hierarchies, double abstraction, double encapsulation, double coupling, double cohesion, double composition, double inheritance, double polymorphism, double messaging, double complexity){
        HashMap<String, Double> tqiValues = new HashMap<>();
        tqiValues.put("reusability", calcReusability(coupling, cohesion, messaging, dsc));
        tqiValues.put("flexibility", calcFlexibility(encapsulation, coupling, composition, polymorphism));
        tqiValues.put("understandability", calcUnderstandability(abstraction, encapsulation, coupling, cohesion, polymorphism, complexity, dsc));
        tqiValues.put("functionality", calcFunctionality(cohesion, polymorphism, messaging, dsc, hierarchies));
        tqiValues.put("extendbaility", calcExtendability(abstraction, coupling, inheritance, polymorphism));
        tqiValues.put("effectiveness", calcEffectiveness(abstraction, encapsulation, composition, inheritance, polymorphism));

        return tqiValues;
    }

    public static double calcReusability(double coupling, double cohesion, double messaging, double dsc) {
        return (-0.25 * coupling) + (0.25 * cohesion) + (0.5 * messaging) + (0.5 * dsc);
    }

    public static double calcFlexibility(double encapuslation, double coupling, double composition, double polymorphism) {
        return (0.25 * encapuslation) - (0.25 * coupling) + (0.5 * composition) + (0.5 * polymorphism);
    }

    public static double calcUnderstandability(double abstraction, double encapsulation, double coupling, double cohesion, double polymorphsim, double complexity, double dsc) {
        return (-0.33 * abstraction) + (0.33 * encapsulation) - (0.33 * coupling) + (0.33 * cohesion) - (0.33 * polymorphsim) - (0.33 * complexity) - (0.33 * dsc);
    }

    public static double calcFunctionality(double cohesion, double polymporhpism, double messaging, double dsc, double hierarchies) {
        return (0.12 * cohesion) + (0.22 * polymporhpism) + (0.22 * messaging) + (0.22 * dsc) + (0.22 * hierarchies);
    }

    public static double calcExtendability(double abstraction, double coupling, double inheritance, double polymorphism) {
        return (0.5 * abstraction) - (0.5 * coupling) + (0.5 * inheritance) + (0.5 * polymorphism);
    }

    public static double calcEffectiveness(double abstraction, double encapsulation, double composition, double inheritance, double polymorphism) {
        return (0.2 * abstraction) + (0.2 * encapsulation) + (0.2 * composition) + (0.2 * inheritance) + (0.2 * polymorphism);
    }
}
