package org.example.prompts;

public enum PackageRecommendationPromt {
    AVG_MUM_METHODS_OVERRIDDEN_TOO_HIGH  ("NMO stands for the number of methods overridden within a class in the context of object-oriented programming (OOP). NMO measures the number of methods that have been overridden in a class. The NMO value in your Java package is too high. To improve this, you should consider the following options:"),
    RMA_TOO_HIGH ("RMA stands for the abstraction of a package in the context of OOP. RMA measures the ratio of abstract classes to concrete classes in a Java package. The RMA value in your Java package is too high. To improve this, you should"),
    NUM_OF_CLASSES_TOO_HIGH ("<> stands for <> in the context of oop. In your java package the <> is too high. To fix this, you should"),
    DN_TOO_HIGH("DN stands for the normalized distance from the main sequence in the context of OOP. DN measures the vertical distance of a package from the main sequence. The main sequence is the graphical relationship between abstraction and instability. The DN value in your Java package is too high. To improve this, you should"),
    AVG_WMC_TOO_HIGH ("WMC stands for weighted methods per class in the context of OOP. WMC counts the classes and multiplies them by their cyclomatic complexity as a weight. The WMC value in your Java package is too high. To improve this, you should"),
    RMI_TOO_HIGH ("RMI stands for the instability within a package in the context of OOP. It measures the ratio of efferent couplings to all couplings. The RMI value in your Java package is too high. To improve this, you should");

    private String prompt;

    PackageRecommendationPromt(String prompt){
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }
}
