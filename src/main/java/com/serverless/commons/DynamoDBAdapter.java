package com.serverless.commons;


import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
public class DynamoDBAdapter {
    private final AmazonDynamoDB client;
    private static DynamoDBAdapter db_adapter = null;
    private DynamoDBMapper mapper;
    private DynamoDBAdapter() {
        this.client = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .build();
    }
    public static DynamoDBAdapter getInstance() {
        if (db_adapter == null)
            db_adapter = new DynamoDBAdapter();

        return db_adapter;
    }

    public AmazonDynamoDB getDbClient() {
        return this.client;
    }
    public DynamoDBMapper createDbMapper(DynamoDBMapperConfig mapperConfig) {
        if (this.client != null)
            mapper = new DynamoDBMapper(this.client, mapperConfig);

        return this.mapper;
    }
}