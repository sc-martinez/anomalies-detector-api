package com.serverless.commons;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.FilterLogEventsRequest;
import software.amazon.awssdk.services.cloudwatchlogs.model.FilterLogEventsResponse;
import software.amazon.awssdk.services.cloudwatchlogs.model.FilteredLogEvent;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class CloudWatchLogsRetriever {
    public static final int limitEvents = Integer.parseInt(System.getenv("LIMIT_EVENTS"));
    private final CloudWatchLogsClient cloudWatchLogsClient;
    private final String logGroupName = System.getenv("LOG_GROUP_NAME");

    private final Integer retrievalMinutes = Integer.parseInt(System.getenv("RETRIEVAL_MINUTES"));

    public CloudWatchLogsRetriever() {
        this.cloudWatchLogsClient = CloudWatchLogsClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    public List<FilteredLogEvent> getLogsLastMinutes() {
        Instant endTime = Instant.now();
        Instant startTime = endTime.minus(Duration.ofMinutes(retrievalMinutes));

        FilterLogEventsRequest request = FilterLogEventsRequest.builder()
                .logGroupName(logGroupName)
                .startTime(startTime.toEpochMilli())
                .endTime(endTime.toEpochMilli())
                .limit(limitEvents)
                .build();

        FilterLogEventsResponse response = cloudWatchLogsClient.filterLogEvents(request);

        return response.events();
    }

}