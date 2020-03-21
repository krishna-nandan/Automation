package com.mPulse.testCases;

import io.restassured.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class MemberApiTest {
	
	int audience_member_id;

	@Test
	public void createNewMember() {
		String myJson = "[{\n" + " \"email\": \"suzddhkfuvhs55565568@yopmail.com\",\n"
				+ " \"last_name\": \"CrowNew\",\n" + " \"first_name\": \"Scare\",\n" + " \"zip_code\": \"12345\",\n"
				+ " \"date_of_birth\": \"1981-05-01\",\n" + " \"gender\": \"male\",\n" + " \"language\": \"en\"\n"
				+ " }]";
		RestAssured.baseURI = "https://qa2-ms-api.mpulsemobile.com/REST/accounts/1440/members";

		Response r = expect().
				statusCode(200).
				given().
				contentType("application/json").
				header("Authorization", "Basic S3Jpc2huYVlhZGF2UUE6QXFASjZJTkNVd2o1eD1oIw==").
				body(myJson).log().all().
				when().
				post();
		
		String body = r.getBody().asString();
		System.out.println(body);
		Assert.assertEquals(r.header("Content-Type"), "application/json");
		Assert.assertEquals(r.statusCode(), 200);
		JsonPath jsonPathEvaluator = r.jsonPath();
		String lastName = jsonPathEvaluator.get("content[0].last_name");
		System.out.println("last name is :- "+lastName);
		this.audience_member_id = jsonPathEvaluator.getInt("content[0].audience_member_id");
	}

	@Test
	public void getMemberData() {
		
		RestAssured.baseURI = "https://qa2-ms-api.mpulsemobile.com/REST/accounts/1440/members/";
		Response r = given().
				contentType("application/json").
				header("Authorization", "Basic S3Jpc2huYVlhZGF2UUE6QXFASjZJTkNVd2o1eD1oIw==").
				when().get(""+audience_member_id+"");
		String body = r.getBody().asString();
		System.out.println(body);

		Assert.assertEquals(r.header("Content-Type"), "application/json");
		Assert.assertEquals(r.statusCode(), 200);
	}

}
