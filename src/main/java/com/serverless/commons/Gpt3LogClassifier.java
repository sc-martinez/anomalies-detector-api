package com.serverless.commons;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Gpt3LogClassifier {
    private static final String API_KEY = System.getenv("OPEN_AI_API_KEY");
    public static final int OPEN_AI_TIMEOUT = Integer.parseInt(System.getenv("OPEN_AI_TIMEOUT"));
    public static final int MAX_TOKENS = Integer.parseInt(System.getenv("OPEN_AI_MAX_TOKENS"));

    public static List<String> classifyLogs(String logData){
        OpenAiService service = new OpenAiService(API_KEY, Duration.ofSeconds(OPEN_AI_TIMEOUT));

        String prompt = "Pretend to be an anomaly detector analyst. only provide a RFC8259 compliant JSON array of objects response for grouping a set of events on CRITICAL, HIGH, MEDIUM, LOW priority incidents following this format:\n" +
                "\n" +
                "[" +
                "{\"priority\": \"HIGH\",\n" +
                "\"short_description\": \"description of the problem goes here\""+
                "}" +
                "]\n The events to classify are: %events \n Do not escape the double quotes in the output: The JSON object is :".replace("%events", logData);


        ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), prompt);
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(userMessage);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo-0613")
                .messages(messages)
                .maxTokens(MAX_TOKENS)
                .build();

        ChatMessage responseMessage = service.createChatCompletion(chatCompletionRequest)
                .getChoices().get(0).getMessage();
        return extractClassification(responseMessage.getContent(), prompt);
    }

    private static List<String> extractClassification(String responseData, String prompt) {
        List<String> arr = new ArrayList<>();
        arr.add(responseData);
        arr.add(prompt);
        return arr;
    }
}
