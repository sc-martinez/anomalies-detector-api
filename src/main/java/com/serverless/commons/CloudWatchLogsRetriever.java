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

/**
 * A class for retrieving logs from AWS CloudWatch.
 */
public class CloudWatchLogsRetriever {
    /** The limit for the number of log events to retrieve. */
    public static final int LIMIT_EVENTS = Integer.parseInt(System.getenv("LIMIT_EVENTS"));

    private final CloudWatchLogsClient cloudWatchLogsClient;
    /** The name of the log group from which logs will be retrieved. */
    private final String logGroupName = System.getenv("LOG_GROUP_NAME");
    /** The number of minutes for log retrieval. */
    private final Integer retrievalMinutes = Integer.parseInt(System.getenv("RETRIEVAL_MINUTES"));

    /**
     * Constructs a CloudWatchLogsRetriever object initializing the CloudWatchLogsClient.
     */
    public CloudWatchLogsRetriever() {
        // Initializing the CloudWatchLogsClient using the builder pattern
        this.cloudWatchLogsClient = CloudWatchLogsClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    /**
     * Retrieves filtered log events for the last specified number of minutes.
     *
     * @return A list of FilteredLogEvent objects representing the filtered log events.
     */
    public List<FilteredLogEvent> getLogsLastMinutes() {
        // Calculate the start and end time for log retrieval
        Instant endTime = Instant.now();
        Instant startTime = endTime.minus(Duration.ofMinutes(retrievalMinutes));

        // Construct a request to filter log events
        FilterLogEventsRequest request = FilterLogEventsRequest.builder()
                .logGroupName(logGroupName)
                .startTime(startTime.toEpochMilli())
                .endTime(endTime.toEpochMilli())
                .limit(LIMIT_EVENTS)
                .build();

        // Retrieve log events based on the request
        FilterLogEventsResponse response = cloudWatchLogsClient.filterLogEvents(request);

        // Return the list of filtered log events
        return response.events();
    }
}
