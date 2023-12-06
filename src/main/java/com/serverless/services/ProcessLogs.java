package com.serverless.services;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.commons.CloudWatchLogsRetriever;
import com.serverless.commons.Gpt3LogClassifier;
import com.serverless.domain.Classification;
import com.serverless.http.ApiGatewayResponse;
import com.serverless.http.Response;
import org.apache.log4j.Logger;
import software.amazon.awssdk.services.cloudwatchlogs.model.FilteredLogEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

public class ProcessLogs implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Powered-By", "AWS Lambda & Serverless");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Credentials", "true");

        try {
            CloudWatchLogsRetriever retriever = new CloudWatchLogsRetriever();

            List<String> logs =  retriever.getLogsLastMinutes().stream()
                                .map(FilteredLogEvent::toString)
                                .collect(Collectors.toList());

            List<String> result = Gpt3LogClassifier.classifyLogs(logs.toString());

            Classification csl = new Classification();
            csl.setResult(result.get(0));
            csl.save(csl);

            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(result)
                    .setHeaders(headers)
                    .build();
        } catch (Exception ex) {
            Response responseBody = new Response("Error in getting elements ......" + ex, input);
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(responseBody)
                    .setHeaders(headers)
                    .build();
        }
    }
}