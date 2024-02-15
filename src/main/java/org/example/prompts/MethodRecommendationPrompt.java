package org.example.prompts;

public enum MethodRecommendationPrompt {

    CC_TOO_HIGH("The cyclomatic complexity of your java method is too high. To fix this you should"),
    MLOC_TOO_HIGH("Your java method has too many lines of code. To fix this you should"),
    LOID_TOO_LOW("The identifier of your java method is too short. You should fix this because"),
    NOP_TOO_HIGH("Your java methods has too many parameters. You should fix this because");

    private String prompt;

    MethodRecommendationPrompt(String prompt){
        this.prompt = prompt;
    }

    public String getPrompt(){
        return this.prompt;
    }




}
