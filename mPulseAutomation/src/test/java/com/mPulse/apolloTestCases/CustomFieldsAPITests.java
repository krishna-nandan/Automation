package com.mPulse.apolloTestCases;

import com.mPulse.factories.mPulseDB;
import com.mPulse.utility.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CustomFieldsAPITests extends ApolloBaseTest {

    //Custom Field Types
    private String customFieldTypeNumber="1";
    private String customFieldTypePickList="4";
    private String customFieldTypeMultiPickList="5";

    //Custom Fields ErrorMessages
    private String invalidCustomFieldName = "Value should be between 5 and 50 characters.";
    private String picklistWithoutValuesErrorMessage = "values are required if field_type is picklist and multipicklist";


    @Test(groups = { "ApolloSanity", "CustomFieldsApi" })
    public void verifySingleCustomFieldGetRequest() throws Exception {

        RestAssured.baseURI = ConfigReader.getConfig("apollo_host");
        Response res = given().header("Authorization", ConfigReader.getConfig("apollo_api_key")).header("X-Ms-Source", ConfigReader.getConfig("apollo_source"))
                .header("Content-Type", ConfigReader.getConfig("apollo_content")).
                        when().log().all().get(Resources.customFieldSingleGet()).
                        then().assertThat().statusCode(200).and().contentType(ContentType.JSON).and().
                        extract().response();
        String body = res.getBody().asString();
        JsonPath x = new JsonPath(body);
        Assert.assertEquals(x.getString("multipicklist6._url"), "/api/accounts/1608/custom_fields/7475");
        Map<String, String> data = mPulseDB.getResultMap("select name, description, field_type from custom_field where id = " + Resources.customfieldid);
        Assert.assertEquals(data.get("name"), "multi7");
        Assert.assertEquals(data.get("description"), "multi7");
        Assert.assertEquals(data.get("field_type"), "5");
    }

    @Test(groups = { "ApolloSanity", "CustomFieldsApi" })
    public void verifyRespondForGetNotExistingSingleCustomField() {

        RestAssured.baseURI = ConfigReader.getConfig("apollo_host");
        Response res = given().header("Authorization", ConfigReader.getConfig("apollo_api_key")).header("X-Ms-Source", ConfigReader.getConfig("apollo_source"))
                .header("Content-Type", ConfigReader.getConfig("apollo_content")).
                        when().log().all().get("/api/accounts/1608/custom_fields/8989").
                        then().assertThat().statusCode(200).and().contentType(ContentType.JSON).
                        extract().response();
        Assert.assertEquals(res.asString(), "{}\n");
    }

    @Test(groups = { "ApolloSanity", "CustomFieldsApi" })
    public void verifyGetCustomFieldWithWrongAuth() {

        RestAssured.baseURI = ConfigReader.getConfig("apollo_host");
        given().header("Authorization", ConfigReader.getConfig("apollo_wrong_api_key")).header("X-Ms-Source", ConfigReader.getConfig("apollo_source"))
                .header("Content-Type", ConfigReader.getConfig("apollo_content")).
                when().log().all().get(Resources.customFieldSingleGet()).
                then().assertThat().statusCode(401).and().contentType(ContentType.JSON);
    }

    @Test(groups = { "ApolloSanity", "CustomFieldsApi" })
    public void verifyGetAllCustomFields() {

        RestAssured.baseURI = ConfigReader.getConfig("apollo_host");
        Response res = given().header("Authorization", ConfigReader.getConfig("apollo_api_key")).header("X-Ms-Source", ConfigReader.getConfig("apollo_source"))
                .header("Content-Type", ConfigReader.getConfig("apollo_content")).
                        when().log().all().get(Resources.customFieldAllGet()).
                        then().assertThat().statusCode(200).and().contentType(ContentType.JSON).
                        body("date1._url", equalTo("/api/accounts/1608/custom_fields/6597")).
                        extract().response();
    }

    @Test(groups = { "ApolloSanity", "CustomFieldsApi" })
    public void verifyGetAllCustomFieldsWithWrongAuth() {

        RestAssured.baseURI = ConfigReader.getConfig("apollo_host");
        given().header("Authorization", ConfigReader.getConfig("apollo_wrong_api_key")).header("X-Ms-Source", ConfigReader.getConfig("apollo_source"))
                .header("Content-Type", ConfigReader.getConfig("apollo_content")).
                when().log().all().get(Resources.customFieldAllGet()).
                then().assertThat().statusCode(401).and().contentType(ContentType.JSON);
    }

    @Test(groups = { "ApolloSanity", "CustomFieldsApi" })
    public void verifyCreateCustomFieldNumberType() throws Exception {
        String customFieldName = "name" + RandomeUtility.getRandomString();

        JsonPath jsonRespond = Payload.sendPostRequest(customFieldName, this.customFieldTypeNumber,
                "\"description\"", 201);
        String fieldId = jsonRespond.get("id").toString();
        Map<String, String> data = mPulseDB.getResultMap("select name, description, field_type from custom_field where id = " + fieldId);
        Assert.assertEquals(data.get("name"), customFieldName);
        Assert.assertEquals(data.get("description"), "description");
        Assert.assertEquals(data.get("field_type"), this.customFieldTypeNumber);
        ApolloUtilityMethods.softDeleteCustomFieldFromDataBase(fieldId);

    }
    @Test(groups = { "ApolloSanity", "CustomFieldsApi" })
    public void verifyCreateCustomFieldSingleLineType() throws Exception{
        String customFieldName = "name" + RandomeUtility.getRandomString();
        String customFieldTypeSingleLine = "2";

        JsonPath jsonPathEvaluator = Payload.sendPostRequest(customFieldName, customFieldTypeSingleLine,
                "\"description\"", 201);
        String fieldId = jsonPathEvaluator.get("id").toString();
        Map<String, String> data = mPulseDB.getResultMap("select name, description, field_type from custom_field where id = " + fieldId);
        Assert.assertEquals(data.get("name"), customFieldName);
        Assert.assertEquals(data.get("description"), "description");
        Assert.assertEquals(data.get("field_type"), customFieldTypeSingleLine);
        ApolloUtilityMethods.softDeleteCustomFieldFromDataBase(fieldId);
    }

    @Test(groups = { "ApolloSanity", "CustomFieldsApi" })
    public void verifyCreateCustomFieldParagraphType() throws Exception{
        String customFieldName = "name" + RandomeUtility.getRandomString();
        String customFieldTypeParagraph = "3";

        JsonPath jsonPathResponse = Payload.sendPostRequest(customFieldName, customFieldTypeParagraph,
                "\"description\"", 201);
        String fieldId = jsonPathResponse.get("id").toString();
        Map<String, String> data = mPulseDB.getResultMap("select name, description, field_type from custom_field where id = " + fieldId);
        Assert.assertEquals(data.get("name"), customFieldName);
        Assert.assertEquals(data.get("description"), "description");
        Assert.assertEquals(data.get("field_type"), customFieldTypeParagraph);
        ApolloUtilityMethods.softDeleteCustomFieldFromDataBase(fieldId);
    }

    @Test(groups = { "ApolloSanity", "CustomFieldsApi" })
    public void verifyCreateCustomFieldPickListType() throws Exception{
        String customFieldName = "name" + RandomeUtility.getRandomString();

        JsonPath res = Payload.sendPostRequestWithValues(customFieldName, this.customFieldTypePickList,
                "\"description\"", 201, "car", "bike");
        String fieldId = res.get("id").toString();
        Map<String, String> data = mPulseDB.getResultMap("select name, description, field_type from custom_field where id = " + fieldId);
        Assert.assertEquals(data.get("name"), customFieldName);
        Assert.assertEquals(data.get("description"), "description");
        Assert.assertEquals(data.get("field_type"), this.customFieldTypePickList);
        ApolloUtilityMethods.softDeleteCustomFieldFromDataBase(fieldId);
    }

    @Test(groups = { "ApolloSanity", "CustomFieldsApi" })
    public void verifyCreateCustomFieldMultiPickListType() throws Exception{
        String customFieldName = "name" + RandomeUtility.getRandomString();

        JsonPath res = Payload.sendPostRequestWithValues(customFieldName, this.customFieldTypeMultiPickList,
                "\"description\"", 201, "car", "bike");
        String fieldId = res.get("id").toString();
        Map<String, String> data = mPulseDB.getResultMap("select name, description, field_type from custom_field where id = " + fieldId);
        Assert.assertEquals(data.get("name"), customFieldName);
        Assert.assertEquals(data.get("description"), "description");
        Assert.assertEquals(data.get("field_type"), this.customFieldTypeMultiPickList);
        ApolloUtilityMethods.softDeleteCustomFieldFromDataBase(fieldId);
    }

    @Test(groups = { "ApolloSanity", "CustomFieldsApi" })
    public void verifyCreateCustomFieldPickListTypeWithoutValues() {
        String customFieldName = "name" + RandomeUtility.getRandomString();

        JsonPath jsonPathEvaluator = Payload.sendPostRequest(customFieldName, this.customFieldTypePickList,
                "\"description\"", 400);
        String errorMessage = jsonPathEvaluator.get("errors[0]").toString();
        Assert.assertEquals(errorMessage, this.picklistWithoutValuesErrorMessage);
    }

    @Test(groups = { "ApolloSanity", "CustomFieldsApi" })
    public void verifyCreateCustomFieldMultiPickListTypeWithoutValues() {
        String customFieldName = "name" + RandomeUtility.getRandomString();

        JsonPath jsonPathEvaluator = Payload.sendPostRequest(customFieldName, this.customFieldTypeMultiPickList,
                "\"description\"", 400);
        String errorMessage = jsonPathEvaluator.get("errors[0]").toString();
        Assert.assertEquals(errorMessage, this.picklistWithoutValuesErrorMessage);
    }

    @Test(groups = { "ApolloSanity", "CustomFieldsApi" })
    public void verifyCreateCustomFieldDateType() throws Exception {
        String customFieldName = "name" + RandomeUtility.getRandomString();
        String customFieldTypeDate = "6";

        JsonPath jsonPathEvaluator = Payload.sendPostRequest(customFieldName, customFieldTypeDate,
                "\"description\"", 201);
        String fieldId = jsonPathEvaluator.get("id").toString();
        Map<String, String> data = mPulseDB.getResultMap("select name, description, field_type from custom_field where id = " + fieldId);
        Assert.assertEquals(data.get("name"), customFieldName);
        Assert.assertEquals(data.get("description"), "description");
        Assert.assertEquals(data.get("field_type"), customFieldTypeDate);
        ApolloUtilityMethods.softDeleteCustomFieldFromDataBase(fieldId);
    }

    @Test(groups = { "ApolloSanity", "CustomFieldsApi" })
    public void verifyCreateCustomFieldTimeType() throws Exception {
        String customFieldName = "name" + RandomeUtility.getRandomString();
        String customFieldTypeTime = "7";

        JsonPath jsonPathEvaluator = Payload.sendPostRequest(customFieldName, customFieldTypeTime,
                "\"description\"", 201);
        String fieldId = jsonPathEvaluator.get("id").toString();
        Map<String, String> data = mPulseDB.getResultMap("select name, description, field_type from custom_field where id = " + fieldId);
        Assert.assertEquals(data.get("name"), customFieldName);
        Assert.assertEquals(data.get("description"), "description");
        Assert.assertEquals(data.get("field_type"), customFieldTypeTime);
        ApolloUtilityMethods.softDeleteCustomFieldFromDataBase(fieldId);
    }

    @Test(groups = { "ApolloSanity", "CustomFieldsApi" })
    public void verifyCreateCustomFieldWithInvalidFieldType() {
        String customFieldName = "name" + RandomeUtility.getRandomString();
        String invalidFieldType = "70";
        String invalidCustomFieldTypeErrorMessage = "[Not a valid choice. Available choices - 1, 2, 3, 4, 5, 6, 7 " +
                "corresponding to labels - number, singleline, paragraph, picklist, multipicklist, date, time respectively]";

        JsonPath jsonPathEvaluator = Payload.sendPostRequest(customFieldName, invalidFieldType,
                "\"description\"", 400);
        String line = jsonPathEvaluator.get("errors.field_type").toString();
        Assert.assertEquals(line, invalidCustomFieldTypeErrorMessage);
    }

    @Test(groups = { "ApolloSanity", "CustomFieldsApi" })
    public void verifyCreateCustomFieldWithEmptyName() {

        JsonPath jsonPathEvaluator = Payload.sendPostRequest("", this.customFieldTypeNumber,
                "\"description\"", 400);
        String line = jsonPathEvaluator.get("errors.name[0]").toString();
        Assert.assertEquals(line, this.invalidCustomFieldName);
    }

    @Test(groups = { "ApolloSanity", "CustomFieldsApi" })
    public void verifyCreateCustomFieldWithNameLessThenFiveLetters() {

        JsonPath jsonPathEvaluator = Payload.sendPostRequest("abc", this.customFieldTypeNumber,
                "\"description\"", 400);
        String line = jsonPathEvaluator.get("errors.name[0]").toString();
        Assert.assertEquals(line, this.invalidCustomFieldName);
    }

    @Test(groups = { "ApolloSanity", "CustomFieldsApi" })
    public void verifyCreateCustomFieldWithNameMoreThenFiftyLetters() {
        String fieldNameMoreThenFiftyLetters = "Fifty letters name and Fifty letters name and " +
                "Fifty letters name and Fifty letters name and";

        JsonPath jsonPathEvaluator = Payload.sendPostRequest(fieldNameMoreThenFiftyLetters, this.customFieldTypeNumber,
                "\"description\"", 400);
        String line = jsonPathEvaluator.get("errors.name[0]").toString();
        Assert.assertEquals(line, this.invalidCustomFieldName);
    }

    @Test(groups = { "ApolloSanity", "CustomFieldsApi" })
    public void verifyCreateCustomFieldWitExistingName() {
        String existingCustomFieldName = "Multi";
        String existingFieldNameErrorMessage = "Custom Field name should be unique.";

        JsonPath jsonPathEvaluator = Payload.sendPostRequest(existingCustomFieldName, this.customFieldTypeNumber,
                "\"description\"", 400);
        String line = jsonPathEvaluator.get("errors[0]").toString();
        Assert.assertEquals(line, existingFieldNameErrorMessage);
    }

    @Test(groups = { "ApolloSanity", "CustomFieldsApi" })
    public void verifyCreateCustomFieldWitEmptyDescription() throws Exception {
        String customFieldName = "name" + RandomeUtility.getRandomString();

        JsonPath jsonPathEvaluator = Payload.sendPostRequest(customFieldName, this.customFieldTypeNumber,
                "\"\"", 201);
        String fieldId = jsonPathEvaluator.get("id").toString();
        Map<String, String> data = mPulseDB.getResultMap("select name, description, field_type from custom_field where id = " + fieldId);
        Assert.assertEquals(data.get("name"), customFieldName);
        Assert.assertEquals(data.get("description"), "");
        Assert.assertEquals(data.get("field_type"), this.customFieldTypeNumber);
        ApolloUtilityMethods.softDeleteCustomFieldFromDataBase(fieldId);
    }

    @Test(groups = { "ApolloSanity", "CustomFieldsApi" })
    public void verifyCreateCustomFieldWitSpecialCharactersName() {
        String invalidCharactersErrorMessage = "Value contains invalid characters ('_', '-' and ' ' are the only allowed special characters).";

        JsonPath jsonPathEvaluator = Payload.sendPostRequest("Name@#$-.", this.customFieldTypeNumber,
                "\"description\"", 400);
        String line = jsonPathEvaluator.get("errors.name[0]").toString();
        Assert.assertEquals(line, invalidCharactersErrorMessage);
    }

    @Test(groups = { "ApolloSanity", "CustomFieldsApi" })
    public void verifyCreateCustomFieldWitSpecialCharactersDescription() throws Exception {
        String customFieldName = "name" + RandomeUtility.getRandomString();

        JsonPath jsonPathEvaluator = Payload.sendPostRequest(customFieldName, this.customFieldTypeNumber,
                "\"Description#$%^&*.\"", 201);
        String fieldId = jsonPathEvaluator.get("id").toString();
        Map<String, String> data = mPulseDB.getResultMap("select name, description, field_type from custom_field where id = " + fieldId);
        Assert.assertEquals(data.get("name"), customFieldName);
        Assert.assertEquals(data.get("description"), "Description#$%^&*.");
        Assert.assertEquals(data.get("field_type"), this.customFieldTypeNumber);
        ApolloUtilityMethods.softDeleteCustomFieldFromDataBase(fieldId);
    }

    @Test(groups = { "ApolloSanity", "CustomFieldsApi" })
    public void verifyCreateCustomFieldsWithWrongAuth() {

        RestAssured.baseURI = ConfigReader.getConfig("apollo_host");
        given().header("Authorization", ConfigReader.getConfig("apollo_wrong_api_key")).header("X-Ms-Source",
                ConfigReader.getConfig("apollo_source"))
                .header("Content-Type", ConfigReader.getConfig("apollo_content")).
                when().log().all().post(Resources.customFieldPost()).
                then().assertThat().statusCode(401).and().contentType(ContentType.JSON);
    }

//    PUT API DOESN'T WORK -- WILL ADD THOSE LATER

//    @Test(groups = { "sanity", "CustomFieldsApi" })
//    public void verifyUpdateNameAndDescriptionCustomField() throws Exception {
//        String customFieldName = "name" + RandomeUtility.getRandomString();
//        String descriptionWithTimeStamp = "description" + RandomeUtility.getRandomString();
//
//        RestAssured.baseURI = ConfigReader.getConfig("apollo_host");
//        Response res = given().header("Authorization", ConfigReader.getConfig("apollo_api_key")).header("X-Ms-Source", ConfigReader.getConfig("apollo_source"))
//                .header("Content-Type", ConfigReader.getConfig("apollo_content")).
//                        body(Payload.customFieldPostDataWithValues(customFieldName, Resources.customFieldTypeMultiPickList, descriptionWithTimeStamp, "valueOne", "ValueTwo")).
//                        when().log().all().
//                        put(Resources.customFieldPut()).
//                        then().contentType(ContentType.JSON).extract().response();
//        System.out.println(res);
//        Map<String, String> data = DbConnection.getResultMap("select * from custom_field where id = " + Resources.customfieldid);
//        Assert.assertEquals(data.get("name"), customFieldName);
//        Assert.assertEquals(data.get("description"), descriptionWithTimeStamp);
//        Assert.assertEquals(data.get("field_type"), Resources.customFieldTypeMultiPickList);
//    }

}

