package com.serverless.commons;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A utility class for classifying logs using OpenAI's GPT-3 API.
 */
public class Gpt3LogClassifier {
    /** The API key used to access the OpenAI API. */
    private static final String API_KEY = System.getenv("OPEN_AI_API_KEY");

    /** The timeout duration for OpenAI API requests. */
    public static final int OPEN_AI_TIMEOUT = Integer.parseInt(System.getenv("OPEN_AI_TIMEOUT"));

    /** The maximum number of tokens for OpenAI requests. */
    public static final int MAX_TOKENS = Integer.parseInt(System.getenv("OPEN_AI_MAX_TOKENS"));

    /**
     * Classifies log data using the OpenAI GPT-3 API.
     *
     * @param logData The log data to be classified.
     * @return A list of strings containing the classification response and the initial prompt used.
     */
    public static List<String> classifyLogs(String logData){
        // Creating an OpenAiService instance using API key and timeout duration
        OpenAiService service = new OpenAiService(API_KEY, Duration.ofSeconds(OPEN_AI_TIMEOUT));

        // Creating a prompt based on the log data
        String prompt = "Pretend to be an anomaly detector analyst. only provide a RFC8259 compliant JSON array of objects response for grouping a set of events on CRITICAL, HIGH, MEDIUM, LOW priority incidents following this format:\n" +
                "\n" +
                "[" +
                "{\"priority\": \"HIGH\",\n" +
                "\"short_description\": \"description of the problem goes here\""+
                "}" +
                "]\n The events to classify are: %events \n Do not escape the double quotes in the output: The JSON object is :".replace("%events", logData);

        // Creating user and system messages for OpenAI completion request
        ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), prompt);
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(userMessage);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo-0613")
                .messages(messages)
                .maxTokens(MAX_TOKENS)
                .build();

        // Generating a response message using the OpenAI service
        ChatMessage responseMessage = service.createChatCompletion(chatCompletionRequest)
                .getChoices().get(0).getMessage();

        // Extracting and returning the classification response and prompt
        return extractClassification(responseMessage.getContent(), prompt);
    }

    /**
     * Extracts the classification response and prompt and returns them as a list of strings.
     *
     * @param responseData The response data received from the GPT-3 API.
     * @param prompt The initial prompt used for the classification request.
     * @return A list containing the classification response and the initial prompt.
     */
    private static List<String> extractClassification(String responseData, String prompt) {
        List<String> arr = new ArrayList<>();
        arr.add(responseData);
        arr.add(prompt);
        return arr;
    }
}
