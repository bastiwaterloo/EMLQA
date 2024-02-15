package org.example.prompts;

public enum ClassRecommendationPrompt {

    LOC_TOO_HIGH("Your java class has too many lines of code. To fix this you should"),
    AVGCC_TOO_HIGH("The average cyclomatic complexity in your java class is too high. To fix this you should"),
    NOM_TOO_HIGH("The number of methods in your java class is too high. To fix this you should"),
    NOC_TOO_HIGH("Your java class has too many children. To fix this you should"),
    NMO_TOO_HIGH("The number of overridden methods in your java class is too high. To fix this you should"),
    WMC_TOO_HIGH("The weighted methods per class in your java class is too high. To fix this you should"),
    DIT_TOO_HIGH("The depth of inheritance tree for your java class is too high. To fix this you should"),
    AVGLOID_TOO_HIGH("The average length of identifieres within your java class is too short. You should fix this because"),
    LCOM_TOO_HIGH("The lack of cohesion in object methods in your class is too high. To fix this you should");

    private String prompt;

    ClassRecommendationPrompt(String prompt){
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }
}
