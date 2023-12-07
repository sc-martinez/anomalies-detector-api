package com.serverless.commons;


import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
/**
 * A singleton class for managing DynamoDB connections and mappers.
 */
public class DynamoDBAdapter {
    private final AmazonDynamoDB client;
    private static DynamoDBAdapter db_adapter = null;
    private DynamoDBMapper mapper;

    /**
     * Private constructor to initialize the AmazonDynamoDB client.
     */
    private DynamoDBAdapter() {
        this.client = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .build();
    }

    /**
     * Retrieves an instance of DynamoDBAdapter. If not created, initializes a new instance.
     *
     * @return An instance of DynamoDBAdapter.
     */
    public static DynamoDBAdapter getInstance() {
        if (db_adapter == null)
            db_adapter = new DynamoDBAdapter();

        return db_adapter;
    }

    /**
     * Retrieves the AmazonDynamoDB client.
     *
     * @return The AmazonDynamoDB client used by the adapter.
     */
    public AmazonDynamoDB getDbClient() {
        return this.client;
    }

    /**
     * Creates and retrieves a DynamoDBMapper using the provided mapper configuration.
     *
     * @param mapperConfig The configuration for the DynamoDBMapper.
     * @return The DynamoDBMapper created using the provided configuration.
     */
    public DynamoDBMapper createDbMapper(DynamoDBMapperConfig mapperConfig) {
        if (this.client != null)
            mapper = new DynamoDBMapper(this.client, mapperConfig);

        return this.mapper;
    }
}
