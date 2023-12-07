package com.serverless.http;

import java.util.Map;

/**
 * Represents a generic response containing a message and input data.
 */
public class Response {
	private final String message;
	private final Map<String, Object> input;

	/**
	 * Constructs a Response object.
	 *
	 * @param message The message associated with the response.
	 * @param input   The input data associated with the response.
	 */
	public Response(String message, Map<String, Object> input) {
		this.message = message;
		this.input = input;
	}

	/**
	 * Retrieves the message associated with the response.
	 *
	 * @return The message of the response.
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Retrieves the input data associated with the response.
	 *
	 * @return The input data of the response.
	 */
	public Map<String, Object> getInput() {
		return this.input;
	}
}
