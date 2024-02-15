package org.example;

import org.example.connection.OpenAIRequest;
import org.example.prompts.MethodRecommendationPrompt;
import org.example.prompts.PackageRecommendationPromt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RecommendationCreator {
    public static String createRecommendation(String prompt){
        return OpenAIRequest.getRecommendation(prompt);
    }
}
