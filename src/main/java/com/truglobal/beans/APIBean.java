/**
 * 
 */
package com.truglobal.beans;

import java.util.HashMap;
import java.util.Map;

import io.restassured.response.Response;

/**
 * @author Kumara Swamy
 *
 */
public class APIBean {
	private String baseURI;
	private String requestBody;
	private String requestMethod;
	private int statusCode;
	private Map<String, Object> requestHeaders = new HashMap<>();
	private Map<String, Object> responseHeaders = new HashMap<>();
	private Map<String, Object> queryParameters = new HashMap<>();
	private Response response;

	/**
	 * @return the apiURL
	 */
	public String getBaseURI() {
		return baseURI;
	}

	/**
	 * @param baseURL the apiURL to set
	 */
	public void setBaseURI(String baseURL) {
		this.baseURI = baseURL;
	}

	/**
	 * @return the requestBody
	 */
	public String getRequestBody() {
		return requestBody;
	}

	/**
	 * @param requestBody the requestBody to set
	 */
	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	/**
	 * @return the requestMethod
	 */
	public String getRequestMethod() {
		return requestMethod;
	}

	/**
	 * @param requestMethod the requestMethod to set
	 */
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * @param statusCode the statusCode to set
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * @return the requestHeaders
	 */
	public Map<String, Object> getRequestHeaders() {
		return requestHeaders;
	}

	/**
	 * @param requestHeaders the requestHeaders to set
	 */
	public void setRequestHeader(String key, Object value) {
		this.requestHeaders.put(key, value);
	}

	/**
	 * @return the responseHeaders
	 */
	public Map<String, Object> getResponseHeaders() {
		return responseHeaders;
	}

	/**
	 * @param responseHeaders the responseHeaders to set
	 */
	public void setResponseHeaders(String key, Object value) {
		this.responseHeaders.put(key, value);
	}

	/**
	 * @return the queryParameters
	 */
	public Map<String, Object> getQueryParameters() {
		return queryParameters;
	}

	/**
	 * @param queryParameters the queryParameters to set
	 */
	public void setQueryParameters(String key, Object value) {
		this.queryParameters.put(key, value);
	}

	/**
	 * @return the response
	 */
	public Response getResponse() {
		return response;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(Response response) {
		this.response = response;
	}
}
