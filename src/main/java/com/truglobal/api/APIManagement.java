/**
 * 
 */
package com.truglobal.api;

import com.truglobal.beans.APIBean;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static io.restassured.RestAssured.given;

import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Kumara Swamy
 *
 */
public class APIManagement {
	public APIBean bean;

	public APIManagement() {
		bean = new APIBean();
	}

	public APIBean getAPIBean() {
		return bean;
	}

	public void execute() {
		ResponseSpecification responseSpecification;
		RequestSpecification requestSpecification = RestAssured.given();
		Response response = null;

		requestSpecification.baseUri(bean.getBaseURI());

		if (bean.getRequestHeaders() != null && !bean.getRequestHeaders().isEmpty()) {
			requestSpecification.headers(bean.getRequestHeaders());
		}

		if (bean.getResponseHeaders() != null && !bean.getResponseHeaders().isEmpty()) {
			requestSpecification.headers(bean.getRequestHeaders());
		}

		if (bean.getQueryParameters() != null && !bean.getQueryParameters().isEmpty()) {
			requestSpecification.queryParams(bean.getQueryParameters());
		}

		if (bean.getRequestBody() != null && !bean.getRequestBody().equals("")) {
			requestSpecification.body(bean.getRequestBody());
		}

		if (bean.getRequestMethod().equalsIgnoreCase("get")) {
			response = requestSpecification.get();
		} else if (bean.getRequestMethod().equalsIgnoreCase("post")) {
			response = requestSpecification.post();
		} else if (bean.getRequestMethod().equalsIgnoreCase("put")) {
			response = requestSpecification.put();
		}

		bean.setResponse(response);
	}

	public static void main(String[] args) {
		APIManagement api = new APIManagement();
		api.bean.setBaseURI("https://reqres.in/api/users?page=2");
		api.bean.setRequestMethod("get");
		api.execute();
		System.out.println(api.bean.getResponse().asPrettyString());

		api = new APIManagement();
		api.bean.setBaseURI("https://reqres.in/api/users");
		api.bean.setRequestMethod("post");
		api.bean.setRequestBody(
				"{\r\n" + "    \"name\": \"morpheus\",\r\n" + "    \"job\": \"leader\"\r\n" + "}\r\n" + "");
		api.bean.setRequestHeader("Content-Type", "application/json");
		api.execute();
		System.out.println(api.bean.getResponse().asPrettyString());
	}
}
