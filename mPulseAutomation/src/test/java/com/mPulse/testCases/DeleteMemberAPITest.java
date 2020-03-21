package com.mPulse.testCases;

import io.restassured.path.xml.XmlPath;
import com.mPulse.factories.ReporterFactory;
import com.mPulse.factories.mPulseDB;
import com.mPulse.utility.RandomeUtility;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class DeleteMemberAPITest extends BaseTest {

    Map<String, String> memberOne = new HashMap<String, String>();
    Map<String, String> memberTwo = new HashMap<String, String>();

    @Test(groups = { "sanity", "deleteAPI" })
    public void Verify_Member_Can_Be_Deleted_By_Phone_Number() throws Exception {
        String phoneNumberFirst = RandomeUtility.getRandomPhoneNumber();

        ExtentTest testReporter = ReporterFactory.getTest();
        testReporter.log(LogStatus.INFO, "API test case started");
        testReporter.assignCategory("Delete Member API");
        memberOne.put("mobilephone", phoneNumberFirst);

        testReporter.log(LogStatus.INFO, "Delete member using PhoneNumber");
        String memberId = com.mPulse.utility.AudienceApi.createNewMember(memberOne);
        com.mPulse.utility.AudienceApi.deleteMemberByPhoneNumber(phoneNumberFirst);
        testReporter.log(LogStatus.INFO, "Status Code 200");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberId, "t");

    }
    @Test(groups = { "sanity", "deleteAPI" })
    public void Verify_Member_Can_Be_Deleted_By_Member_Id() throws Exception{
        String phoneNumberFirst = RandomeUtility.getRandomPhoneNumber();

        ExtentTest testReporter = ReporterFactory.getTest();
        testReporter.log(LogStatus.INFO, "API test case started");
        testReporter.assignCategory("Delete Member API");
        memberOne.put("mobilephone", phoneNumberFirst);

        testReporter.log(LogStatus.INFO, "Delete member using member ID");
        String memberId = com.mPulse.utility.AudienceApi.createNewMember(memberOne);
        System.out.println("Memeber ID" + memberId);
        com.mPulse.utility.AudienceApi.deleteMemberById(memberId);
        testReporter.log(LogStatus.INFO, "Status Code 200");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberId, "t");

    }
    @Test(groups = { "sanity", "deleteAPI" })
    public void Verify_Member_Can_Be_Deleted_By_Email() throws Exception {
        String emailFirst = RandomeUtility.getRandomEmails(5);

        ExtentTest testReporter = ReporterFactory.getTest();
        testReporter.log(LogStatus.INFO, "API test case started");
        testReporter.assignCategory("Delete Member API");
        memberOne.put("email", emailFirst);

        testReporter.log(LogStatus.INFO, "Delete member using email");
        String memberId = com.mPulse.utility.AudienceApi.createNewMember(memberOne);
        com.mPulse.utility.AudienceApi.deleteMemberByEmail(emailFirst);
        testReporter.log(LogStatus.INFO, "Status Code 200");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberId, "t");

    }
    @Test(groups = { "sanity", "deleteAPI" })
    public void Verify_Member_Can_Be_Deleted_By_Client_Member_Id() throws Exception {
        String clientMemberIdFirst = RandomeUtility.getRandomClientMemberID();
        String emailFirst = RandomeUtility.getRandomEmails(5);

        ExtentTest testReporter = ReporterFactory.getTest();
        testReporter.log(LogStatus.INFO, "API test case started");
        testReporter.assignCategory("Delete Member API");
        memberOne.put("email", emailFirst);
        memberOne.put("clientmemberid", clientMemberIdFirst);

        testReporter.log(LogStatus.INFO, "Delete member using client member ID");
        String memberId = com.mPulse.utility.AudienceApi.createNewMember(memberOne);

        String xmlBody = "<body>\n" +
                "	<member>\n" +
                "		<clientmemberid>"+clientMemberIdFirst+"</clientmemberid>\n" +
                "	</member>\n" +
                "</body>";
        String body = com.mPulse.utility.AudienceApi.deleteMemberGeneral(xmlBody, 200);
        testReporter.log(LogStatus.INFO, "Status Code 200");
        XmlPath xmlPath = new XmlPath(body);
        Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberId, "t");
    }

    @Test(groups = { "sanity", "deleteAPI" })
    public void Verify_Member_Can_Be_Deleted_By_App_Member_Id() throws Exception{
        String emailFirst = RandomeUtility.getRandomEmails(5);
        String appMemberIdFirst = RandomeUtility.getRandomAppMemberID();

        ExtentTest testReporter = ReporterFactory.getTest();
        testReporter.log(LogStatus.INFO, "API test case started");
        testReporter.assignCategory("Delete Member API");
        memberOne.put("email", emailFirst);
        memberOne.put("appmemberid", appMemberIdFirst);

        testReporter.log(LogStatus.INFO, "Delete member using app member ID");
        String memberId = com.mPulse.utility.AudienceApi.createNewMember(memberOne);

        String xmlBody = "<body>\n" +
                "	<member>\n" +
                "		<appmemberid>"+appMemberIdFirst+"</appmemberid>\n" +
                "	</member>\n" +
                "</body>";
        String body = com.mPulse.utility.AudienceApi.deleteMemberGeneral(xmlBody, 200);
        testReporter.log(LogStatus.INFO, "Status Code 200");
        XmlPath xmlPath = new XmlPath(body);
        Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberId, "t");

    }
    @Test(groups = { "sanity", "deleteAPI" })
    public void Verify_Delete_Not_Existing_Member() throws Exception{
        String emailFirst = RandomeUtility.getRandomEmails(5);

        ExtentTest testReporter = ReporterFactory.getTest();
        testReporter.log(LogStatus.INFO, "API test case started");
        testReporter.assignCategory("Delete Member API");
        memberOne.put("email", emailFirst);

        testReporter.log(LogStatus.INFO, "Create a new member and delete right after creation");
        String memberId = com.mPulse.utility.AudienceApi.createNewMember(memberOne);
        com.mPulse.utility.AudienceApi.deleteMemberByEmail(emailFirst);

        String xmlBody = "<body>\n" +
                "	<member>\n" +
                "		<email>"+emailFirst+"</email>\n" +
                "	</member>\n" +
                "</body>";
        String body = com.mPulse.utility.AudienceApi.deleteMemberGeneral(xmlBody, 400);
        testReporter.log(LogStatus.INFO, "Status Code 400");
        XmlPath xmlPath = new XmlPath(body);
        Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "1");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberId, "t");

    }
    @Test(groups = { "sanity", "deleteAPI" })
    public void Verify_Two_Members_Can_Be_Deleted_By_Email() throws Exception{
        String emailFirst = RandomeUtility.getRandomEmails(5);
        String emailSecond = RandomeUtility.getRandomEmails(5);

        ExtentTest testReporter = ReporterFactory.getTest();
        testReporter.log(LogStatus.INFO, "API test case started");
        testReporter.assignCategory("Delete Member API");
        memberOne.put("email", emailFirst);
        memberTwo.put("email", emailSecond);

        testReporter.log(LogStatus.INFO, "Delete two members using email");
        String memberIdOne = com.mPulse.utility.AudienceApi.createNewMember(memberOne);
        String memberIdTwo = com.mPulse.utility.AudienceApi.createNewMember(memberTwo);

        String xmlBody = "<body>\n" + "	<member>\n" +
                "		<email>"+emailFirst+"</email>\n" +
                "	</member>\n" + "<member>\n" +
                "		<email>"+emailSecond+"</email>\n" +
                "	</member>\n" + "</body>";
        String body = com.mPulse.utility.AudienceApi.deleteMemberGeneral(xmlBody, 200);
        testReporter.log(LogStatus.INFO, "Status Code 200");
        XmlPath xmlPath = new XmlPath(body);
        Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "2");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdOne, "t");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdTwo, "t");
    }
    @Test(groups = { "sanity", "deleteAPI" })
    public void Verify_Two_Members_Can_Be_Deleted_By_Phone_Number() throws Exception{
        String phoneNumberFirst = RandomeUtility.getRandomPhoneNumber();
        String phoneNumberSecond = RandomeUtility.getRandomPhoneNumber();

        ExtentTest testReporter = ReporterFactory.getTest();
        testReporter.log(LogStatus.INFO, "API test case started");
        testReporter.assignCategory("Delete Member API");
        memberOne.put("mobilephone", phoneNumberFirst);
        memberTwo.put("mobilephone", phoneNumberSecond);

        testReporter.log(LogStatus.INFO, "Delete two members using phone numbers");
        String memberIdOne = com.mPulse.utility.AudienceApi.createNewMember(memberOne);
        String MemberIdTwo = com.mPulse.utility.AudienceApi.createNewMember(memberTwo);

        String xmlBody = "<body>\n" + "	<member>\n" +
                "		<mobilephone>"+phoneNumberFirst+"</mobilephone>\n" +
                "	</member>\n" + "<member>\n" +
                "		<mobilephone>"+phoneNumberSecond+"</mobilephone>\n" +
                "	</member>\n" + "</body>";
        String body = com.mPulse.utility.AudienceApi.deleteMemberGeneral(xmlBody, 200);
        testReporter.log(LogStatus.INFO, "Status Code 200");
        XmlPath xmlPath = new XmlPath(body);
        Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "2");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdOne, "t");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(MemberIdTwo, "t");
    }

    @Test(groups = { "sanity", "deleteAPI" })
    public void Verify_Two_Members_Can_Be_Deleted_By_Member_Id() throws Exception{
        String phoneNumberFirst = RandomeUtility.getRandomPhoneNumber();
        String phoneNumberSecond = RandomeUtility.getRandomPhoneNumber();

        ExtentTest testReporter = ReporterFactory.getTest();
        testReporter.log(LogStatus.INFO, "API test case started");
        testReporter.assignCategory("Delete Member API");
        memberOne.put("mobilephone", phoneNumberFirst);
        memberTwo.put("mobilephone", phoneNumberSecond);

        testReporter.log(LogStatus.INFO, "Delete two members using member ID");
        String memberIdFirst = com.mPulse.utility.AudienceApi.createNewMember(memberOne);
        String memberIdSecond = com.mPulse.utility.AudienceApi.createNewMember(memberTwo);

        String xmlBody = "<body>\n" + "	<member>\n" +
                "		<memberid>"+memberIdFirst+"</memberid>\n" +
                "	</member>\n" + "<member>\n" +
                "		<memberid>"+memberIdSecond+"</memberid>\n" +
                "	</member>\n" + "</body>";
        String body = com.mPulse.utility.AudienceApi.deleteMemberGeneral(xmlBody, 200);
        testReporter.log(LogStatus.INFO, "Status Code 200");
        XmlPath xmlPath = new XmlPath(body);
        Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "2");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdFirst, "t");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdSecond, "t");
    }

    @Test(groups = { "sanity", "deleteAPI" })
    public void Verify_Two_Members_Can_Be_Deleted_By_App_Member_ID() throws Exception{
        String phoneNumberFirst = RandomeUtility.getRandomPhoneNumber();
        String phoneNumberSecond = RandomeUtility.getRandomPhoneNumber();
        String appMemberIdFirst = RandomeUtility.getRandomAppMemberID();
        String appMemberIdSecond = RandomeUtility.getRandomAppMemberID();

        ExtentTest testReporter = ReporterFactory.getTest();
        testReporter.log(LogStatus.INFO, "API test case started");
        testReporter.assignCategory("Delete Member API");
        memberOne.put("mobilephone", phoneNumberFirst);
        memberOne.put("appmemberid", appMemberIdFirst);
        memberTwo.put("mobilephone", phoneNumberSecond);
        memberTwo.put("appmemberid", appMemberIdSecond);

        testReporter.log(LogStatus.INFO, "Delete two members using App member ID");
        String memberIdFirst = com.mPulse.utility.AudienceApi.createNewMember(memberOne);
        String memberIdSecond = com.mPulse.utility.AudienceApi.createNewMember(memberTwo);

        String xmlBody = "<body>\n" + "	<member>\n" +
                "		<appmemberid>"+appMemberIdFirst+"</appmemberid>\n" +
                "	</member>\n" + "<member>\n" +
                "		<appmemberid>"+appMemberIdSecond+"</appmemberid>\n" +
                "	</member>\n" + "</body>";
        String body = com.mPulse.utility.AudienceApi.deleteMemberGeneral(xmlBody, 200);
        testReporter.log(LogStatus.INFO, "Status Code 200");
        XmlPath xmlPath = new XmlPath(body);
        Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "2");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdFirst, "t");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdSecond, "t");
    }

    @Test(groups = { "sanity", "deleteAPI" })
    public void Verify_Two_Members_Can_Be_Deleted_By_Client_Member_ID() throws Exception{
        String phoneNumberFirst = RandomeUtility.getRandomPhoneNumber();
        String phoneNumberSecond = RandomeUtility.getRandomPhoneNumber();
        String clientMemberIdFirst = RandomeUtility.getRandomClientMemberID();
        String clientMemberIdSecond = RandomeUtility.getRandomClientMemberID();

        ExtentTest testReporter = ReporterFactory.getTest();
        testReporter.log(LogStatus.INFO, "API test case started");
        testReporter.assignCategory("Delete Member API");
        memberOne.put("mobilephone", phoneNumberFirst);
        memberOne.put("clientmemberid", clientMemberIdFirst);
        memberTwo.put("mobilephone", phoneNumberSecond);
        memberTwo.put("clientmemberid", clientMemberIdSecond);

        testReporter.log(LogStatus.INFO, "Delete two members using client member ID");
        String memberIdFirst = com.mPulse.utility.AudienceApi.createNewMember(memberOne);
        String memberIdSecond = com.mPulse.utility.AudienceApi.createNewMember(memberTwo);

        String xmlBody = "<body>\n" + "	<member>\n" +
                "		<clientmemberid>"+clientMemberIdFirst+"</clientmemberid>\n" +
                "	</member>\n" + "<member>\n" +
                "		<clientmemberid>"+clientMemberIdSecond+"</clientmemberid>\n" +
                "	</member>\n" + "</body>";
        String body = com.mPulse.utility.AudienceApi.deleteMemberGeneral(xmlBody, 200);
        testReporter.log(LogStatus.INFO, "Status Code 200");
        XmlPath xmlPath = new XmlPath(body);
        Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "2");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdFirst, "t");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdSecond, "t");
    }
    @Test(groups = { "sanity", "deleteAPI" })
    public void Verify_Two_Members_Can_Be_Deleted_By_Member_ID_and_Phone_Number() throws Exception{
        String phoneNumberFirst = RandomeUtility.getRandomPhoneNumber();
        String phoneNumberSecond = RandomeUtility.getRandomPhoneNumber();

        ExtentTest testReporter = ReporterFactory.getTest();
        testReporter.log(LogStatus.INFO, "API test case started");
        testReporter.assignCategory("Delete Member API");
        memberOne.put("mobilephone", phoneNumberFirst);
        memberTwo.put("mobilephone", phoneNumberSecond);

        testReporter.log(LogStatus.INFO, "Delete two members. Delete one member by member ID and another member by phonenumber");
        String memberIdFirst = com.mPulse.utility.AudienceApi.createNewMember(memberOne);
        String memberIdSecond = com.mPulse.utility.AudienceApi.createNewMember(memberTwo);

        String xmlBody = "<body>\n" + "	<member>\n" +
                "		<memberid>"+memberIdFirst+"</memberid>\n" +
                "	</member>\n" + "<member>\n" +
                "		<mobilephone>"+phoneNumberSecond+"</mobilephone>\n" +
                "	</member>\n" + "</body>";
        String body = com.mPulse.utility.AudienceApi.deleteMemberGeneral(xmlBody, 200);
        testReporter.log(LogStatus.INFO, "Status Code 200");
        XmlPath xmlPath = new XmlPath(body);
        Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "2");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdFirst, "t");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdSecond, "t");
    }
    @Test(groups = { "sanity", "deleteAPI" })
    public void Verify_Two_Members_Can_Be_Deleted_By_Member_ID_and_Email() throws Exception{
        String phoneNumberFirst = RandomeUtility.getRandomPhoneNumber();
        String emailSecond = RandomeUtility.getRandomEmails(5);

        ExtentTest testReporter = ReporterFactory.getTest();
        testReporter.log(LogStatus.INFO, "API test case started");
        testReporter.assignCategory("Delete Member API");
        memberOne.put("mobilephone", phoneNumberFirst);
        memberTwo.put("email", emailSecond);

        testReporter.log(LogStatus.INFO, "Delete two members. Delete one member by member ID and another member by email");
        String memberIdFirst = com.mPulse.utility.AudienceApi.createNewMember(memberOne);
        String memberIdSecond = com.mPulse.utility.AudienceApi.createNewMember(memberTwo);

        String xmlBody = "<body>\n" + "	<member>\n" +
                "		<memberid>"+memberIdFirst+"</memberid>\n" +
                "	</member>\n" + "<member>\n" +
                "		<email>"+emailSecond+"</email>\n" +
                "	</member>\n" + "</body>";
        String body = com.mPulse.utility.AudienceApi.deleteMemberGeneral(xmlBody, 200);
        testReporter.log(LogStatus.INFO, "Status Code 200");
        XmlPath xmlPath = new XmlPath(body);
        Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "2");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdFirst, "t");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdSecond, "t");
    }

    @Test(groups = { "sanity", "deleteAPI" })
    public void Verify_Two_Members_Can_Be_Deleted_By_Member_ID_and_App_Member_Id() throws Exception{
        String phoneNumberFirst = RandomeUtility.getRandomPhoneNumber();
        String phoneNumberSecond = RandomeUtility.getRandomPhoneNumber();
        String appMemberIdSecond = RandomeUtility.getRandomAppMemberID();


        ExtentTest testReporter = ReporterFactory.getTest();
        testReporter.log(LogStatus.INFO, "API test case started");
        testReporter.assignCategory("Delete Member API");
        memberOne.put("mobilephone", phoneNumberFirst);
        memberTwo.put("mobilephone", phoneNumberSecond);
        memberTwo.put("appmemberid", appMemberIdSecond);

        testReporter.log(LogStatus.INFO, "Delete two members. Delete one member by member ID and another member by App Member ID");
        String memberIdFirst = com.mPulse.utility.AudienceApi.createNewMember(memberOne);
        String memberIdSecond = com.mPulse.utility.AudienceApi.createNewMember(memberTwo);

        String xmlBody = "<body>\n" + "	<member>\n" +
                "		<memberid>"+memberIdFirst+"</memberid>\n" +
                "	</member>\n" + "<member>\n" +
                "		<appmemberid>"+appMemberIdSecond+"</appmemberid>\n" +
                "	</member>\n" + "</body>";
        String body = com.mPulse.utility.AudienceApi.deleteMemberGeneral(xmlBody, 200);
        testReporter.log(LogStatus.INFO, "Status Code 200");
        XmlPath xmlPath = new XmlPath(body);
        Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "2");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdFirst, "t");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdSecond, "t");
    }
    @Test(groups = { "sanity", "deleteAPI" })
    public void Verify_Two_Members_Can_Be_Deleted_By_Member_ID_and_Client_Member_Id()throws Exception {
        String phoneNumberFirst = RandomeUtility.getRandomPhoneNumber();
        String phoneNumberSecond = RandomeUtility.getRandomPhoneNumber();
        String clientMemberIdSecond = RandomeUtility.getRandomClientMemberID();

        ExtentTest testReporter = ReporterFactory.getTest();
        testReporter.log(LogStatus.INFO, "API test case started");
        testReporter.assignCategory("Delete Member API");
        memberOne.put("mobilephone", phoneNumberFirst);
        memberTwo.put("mobilephone", phoneNumberSecond);
        memberTwo.put("clientmemberid", clientMemberIdSecond);

        testReporter.log(LogStatus.INFO, "Delete two members. Delete one member by member ID and another member by Client Member ID");
        String memberIdFirst = com.mPulse.utility.AudienceApi.createNewMember(memberOne);
        String memberIdSecond = com.mPulse.utility.AudienceApi.createNewMember(memberTwo);
        String xmlBody = "<body>\n" + "	<member>\n" +
                "		<memberid>"+memberIdFirst+"</memberid>\n" +
                "	</member>\n" + "<member>\n" +
                "		<clientmemberid>"+clientMemberIdSecond+"</clientmemberid>\n" +
                "	</member>\n" + "</body>";
        String body = com.mPulse.utility.AudienceApi.deleteMemberGeneral(xmlBody, 200);
        testReporter.log(LogStatus.INFO, "Status Code 200");
        XmlPath xmlPath = new XmlPath(body);
        Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "2");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdFirst, "t");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdSecond, "t");
    }

    @Test(groups = { "sanity", "deleteAPI" })
    public void Verify_Two_Members_Can_Be_Deleted_By_Mobile_Phone_And_Email() throws Exception{
        String phoneNumberFirst = RandomeUtility.getRandomPhoneNumber();
        String emailSecond = RandomeUtility.getRandomEmails(5);

        ExtentTest testReporter = ReporterFactory.getTest();
        testReporter.log(LogStatus.INFO, "API test case started");
        testReporter.assignCategory("Delete Member API");
        memberOne.put("mobilephone", phoneNumberFirst);
        memberTwo.put("email", emailSecond);

        testReporter.log(LogStatus.INFO, "Delete two members. Delete one member by phone number and another member by email");
        String memberIdFirst = com.mPulse.utility.AudienceApi.createNewMember(memberOne);
        String memberIdSecond = com.mPulse.utility.AudienceApi.createNewMember(memberTwo);
        String xmlBody = "<body>\n" + "	<member>\n" +
                "		<mobilephone>"+phoneNumberFirst+"</mobilephone>\n" +
                "	</member>\n" + "<member>\n" +
                "		<email>"+emailSecond+"</email>\n" +
                "	</member>\n" + "</body>";
        String body = com.mPulse.utility.AudienceApi.deleteMemberGeneral(xmlBody, 200);
        testReporter.log(LogStatus.INFO, "Status Code 200");
        XmlPath xmlPath = new XmlPath(body);
        Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "2");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdFirst, "t");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdSecond, "t");
    }

    @Test(groups = { "sanity", "deleteAPI" })
    public void Verify_Two_Members_Can_Be_Deleted_By_Mobile_Phone_And_App_Member_Id() throws Exception{
        String phoneNumberFirst = RandomeUtility.getRandomPhoneNumber();
        String emailSecond = RandomeUtility.getRandomEmails(5);
        String appMemberIdSecond = RandomeUtility.getRandomAppMemberID();

        ExtentTest testReporter = ReporterFactory.getTest();
        testReporter.log(LogStatus.INFO, "API test case started");
        testReporter.assignCategory("Delete Member API");
        memberOne.put("mobilephone", phoneNumberFirst);
        memberTwo.put("email", emailSecond);
        memberTwo.put("appmemberid", appMemberIdSecond);

        testReporter.log(LogStatus.INFO, "Delete two members. Delete one member by phone number and another member by app member ID");
        String memberIdFirst = com.mPulse.utility.AudienceApi.createNewMember(memberOne);
        String memberIdSecond = com.mPulse.utility.AudienceApi.createNewMember(memberTwo);
        String xmlBody = "<body>\n" + "	<member>\n" +
                "		<mobilephone>"+phoneNumberFirst+"</mobilephone>\n" +
                "	</member>\n" + "<member>\n" +
                "		<appmemberid>"+appMemberIdSecond+"</appmemberid>\n" +
                "	</member>\n" + "</body>";
        String body = com.mPulse.utility.AudienceApi.deleteMemberGeneral(xmlBody, 200);
        testReporter.log(LogStatus.INFO, "Status Code 200");
        XmlPath xmlPath = new XmlPath(body);
        Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "2");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdFirst, "t");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdSecond, "t");
    }

    @Test(groups = { "sanity", "deleteAPI" })
    public void Verify_Two_Members_Can_Be_Deleted_By_Mobile_Phone_And_Client_Member_Id() throws Exception{
        String phoneNumberFirst = RandomeUtility.getRandomPhoneNumber();
        String clientMemberIdSecond = RandomeUtility.getRandomClientMemberID();
        String emailSecond = RandomeUtility.getRandomEmails(5);

        ExtentTest testReporter = ReporterFactory.getTest();
        testReporter.log(LogStatus.INFO, "API test case started");
        testReporter.assignCategory("Delete Member API");
        memberOne.put("mobilephone", phoneNumberFirst);
        memberTwo.put("email", emailSecond);
        memberTwo.put("clientmemberid", clientMemberIdSecond);

        testReporter.log(LogStatus.INFO, "Delete two members. Delete one member by phone number and another member by client member ID");
        String memberIdFirst = com.mPulse.utility.AudienceApi.createNewMember(memberOne);
        String memberIdSecond = com.mPulse.utility.AudienceApi.createNewMember(memberTwo);
        String xmlBody = "<body>\n" + "	<member>\n" +
                "		<mobilephone>"+phoneNumberFirst+"</mobilephone>\n" +
                "	</member>\n" + "<member>\n" +
                "		<clientmemberid>"+clientMemberIdSecond+"</clientmemberid>\n" +
                "	</member>\n" + "</body>";
        String body = com.mPulse.utility.AudienceApi.deleteMemberGeneral(xmlBody, 200);
        testReporter.log(LogStatus.INFO, "Status Code 200");
        XmlPath xmlPath = new XmlPath(body);
        Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "2");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdFirst, "t");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdSecond, "t");
    }

    @Test(groups = { "sanity", "deleteAPI" })
    public void Verify_Two_Members_Can_Be_Deleted_By_Email_And_Client_Member_Id() throws Exception{
        String emailFirst = RandomeUtility.getRandomEmails(5);
        String emailSecond = RandomeUtility.getRandomEmails(5);
        String clientMemberIdSecond = RandomeUtility.getRandomClientMemberID();

        ExtentTest testReporter = ReporterFactory.getTest();
        testReporter.log(LogStatus.INFO, "API test case started");
        testReporter.assignCategory("Delete Member API");
        memberOne.put("email", emailFirst);
        memberTwo.put("email", emailSecond);
        memberTwo.put("clientmemberid", clientMemberIdSecond);

        testReporter.log(LogStatus.INFO, "Delete two members. Delete one member by email and another member by client member ID");
        String memberIdFirst = com.mPulse.utility.AudienceApi.createNewMember(memberOne);
        String memberIdSecond = com.mPulse.utility.AudienceApi.createNewMember(memberTwo);
        String xmlBody = "<body>\n" + "	<member>\n" +
                "		<email>"+emailFirst+"</email>\n" +
                "	</member>\n" + "<member>\n" +
                "		<clientmemberid>"+clientMemberIdSecond+"</clientmemberid>\n" +
                "	</member>\n" + "</body>";
        String body = com.mPulse.utility.AudienceApi.deleteMemberGeneral(xmlBody, 200);
        testReporter.log(LogStatus.INFO, "Status Code 200");
        XmlPath xmlPath = new XmlPath(body);
        Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "2");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdFirst, "t");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdSecond, "t");
    }

    @Test(groups = { "sanity", "deleteAPI" })
    public void Verify_Two_Members_Can_Be_Deleted_By_Email_And_App_Member_Id() throws Exception{
        String emailFirst = RandomeUtility.getRandomEmails(5);
        String emailSecond = RandomeUtility.getRandomEmails(5);
        String appMemberIdSecond = RandomeUtility.getRandomAppMemberID();

        ExtentTest testReporter = ReporterFactory.getTest();
        testReporter.log(LogStatus.INFO, "API test case started");
        testReporter.assignCategory("Delete Member API");
        memberOne.put("email", emailFirst);
        memberTwo.put("email", emailSecond);
        memberTwo.put("appmemberid", appMemberIdSecond);

        testReporter.log(LogStatus.INFO, "Delete two members. Delete one member by email and another member by app member ID");
        String memberIdFirst = com.mPulse.utility.AudienceApi.createNewMember(memberOne);
        String memberIdSecond = com.mPulse.utility.AudienceApi.createNewMember(memberTwo);
        String xmlBody = "<body>\n" + "	<member>\n" +
                "		<email>"+emailFirst+"</email>\n" +
                "	</member>\n" + "<member>\n" +
                "		<appmemberid>"+appMemberIdSecond+"</appmemberid>\n" +
                "	</member>\n" + "</body>";
        String body = com.mPulse.utility.AudienceApi.deleteMemberGeneral(xmlBody, 200);
        testReporter.log(LogStatus.INFO, "Status Code 200");
        XmlPath xmlPath = new XmlPath(body);
        Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "2");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdFirst, "t");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdSecond, "t");
    }

    @Test(groups = { "sanity", "deleteAPI" })
    public void Verify_Two_Members_Can_Be_Deleted_By_Client_Member_ID_And_App_Member_Id() throws Exception{
        String emailFirst = RandomeUtility.getRandomEmails(5);
        String emailSecond = RandomeUtility.getRandomEmails(5);
        String clientMemberIdFirst = RandomeUtility.getRandomClientMemberID();
        String appMemberIdSecond = RandomeUtility.getRandomAppMemberID();

        ExtentTest testReporter = ReporterFactory.getTest();
        testReporter.log(LogStatus.INFO, "API test case started");
        testReporter.assignCategory("Delete Member API");
        memberOne.put("email", emailFirst);
        memberOne.put("clientmemberid", clientMemberIdFirst);
        memberTwo.put("email", emailSecond);
        memberTwo.put("appmemberid", appMemberIdSecond);

        testReporter.log(LogStatus.INFO, "Delete two members. Delete one member by email and another member by app member ID");
        String memberIdFirst = com.mPulse.utility.AudienceApi.createNewMember(memberOne);
        String memberIdSecond = com.mPulse.utility.AudienceApi.createNewMember(memberTwo);
        String xmlBody = "<body>\n" + "	<member>\n" +
                "		<clientmemberid>"+clientMemberIdFirst+"</clientmemberid>\n" +
                "	</member>\n" + "<member>\n" +
                "		<appmemberid>"+appMemberIdSecond+"</appmemberid>\n" +
                "	</member>\n" + "</body>";
        String body = com.mPulse.utility.AudienceApi.deleteMemberGeneral(xmlBody, 200);
        testReporter.log(LogStatus.INFO, "Status Code 200");
        XmlPath xmlPath = new XmlPath(body);
        Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "2");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdFirst, "t");
        com.mPulse.utility.AudienceApi.verifyMemberDeletedInDataBase(memberIdSecond, "t");
    }

    @Test(groups = { "sanity", "deleteAPI" })
    public void Verify_Delete_Not_Existing_Two_Members() {
        String emailFirst = RandomeUtility.getRandomEmails(5);
        String emailSecond = RandomeUtility.getRandomEmails(5);

        ExtentTest testReporter = ReporterFactory.getTest();
        testReporter.log(LogStatus.INFO, "API test case started");
        testReporter.assignCategory("Delete Member API");
        memberOne.put("email", emailFirst);
        memberTwo.put("email", emailSecond);

        testReporter.log(LogStatus.INFO, "Create and Delete 2 Members");
        com.mPulse.utility.AudienceApi.createNewMember(memberOne);
        com.mPulse.utility.AudienceApi.createNewMember(memberTwo);
        com.mPulse.utility.AudienceApi.deleteMemberByEmail(emailFirst);
        com.mPulse.utility.AudienceApi.deleteMemberByEmail(emailSecond);

        String xmlBody = "<body>\n" + "	<member>\n" +
                "		<email>"+emailFirst+"</email>\n" +
                "	</member>\n" + "<member>\n" +
                "		<email>"+emailSecond+"</email>\n" +
                "	</member>\n" + "</body>";
        String body = com.mPulse.utility.AudienceApi.deleteMemberGeneral(xmlBody, 400);
        testReporter.log(LogStatus.INFO, "Status Code 400");
        XmlPath xmlPath = new XmlPath(body);
        Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "2");
        Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.reason.text()").toString(), "Member does not existMember does not exist");
    }

    @Test(groups = { "sanity", "deleteAPI" })
    public void Verify_Delete_If_One_Of_Two_Member_Not_Exists() {
        String emailFirst = RandomeUtility.getRandomEmails(5);
        String emailSecond = RandomeUtility.getRandomEmails(5);

        ExtentTest testReporter = ReporterFactory.getTest();
        testReporter.log(LogStatus.INFO, "API test case started");
        testReporter.assignCategory("Delete Member API");
        memberOne.put("email", emailFirst);
        memberTwo.put("email", emailSecond);

        testReporter.log(LogStatus.INFO, "Create 2 Members and Delete 1 Member");
        com.mPulse.utility.AudienceApi.createNewMember(memberOne);
        com.mPulse.utility.AudienceApi.createNewMember(memberTwo);
        com.mPulse.utility.AudienceApi.deleteMemberByEmail(emailFirst);

        String xmlBody = "<body>\n" + "	<member>\n <email>"+emailFirst+"</email>\n" +
                "	</member>\n" + "<member>\n <email>"+emailSecond+"</email>\n </member>\n" + "</body>";
        String body = com.mPulse.utility.AudienceApi.deleteMemberGeneral(xmlBody, 400);
        testReporter.log(LogStatus.INFO, "Status Code 400");
        XmlPath xmlPath = new XmlPath(body);
        Assert.assertEquals(xmlPath.get("body.results.success.text()").toString(), "1");
        Assert.assertEquals(xmlPath.get("body.results.failure.text()").toString(), "1");
        Assert.assertEquals(xmlPath.get("body.members.member.member_errors.member_error.detail.reason.text()").toString(), "Member does not exist");
    }

    @Test(groups = { "sanity", "deleteAPI" })
    public void VerifyDeletedMemberInfoInAudienceMemberDeletedDB() throws Exception {
        String phoneNumberFirst = RandomeUtility.getRandomPhoneNumber();
        String email = RandomeUtility.getRandomEmails(5);
        String clientMemberId = RandomeUtility.getRandomClientMemberID();
        String appMemberId = RandomeUtility.getRandomAppMemberID();

        ExtentTest testReporter = ReporterFactory.getTest();
        testReporter.log(LogStatus.INFO, "API test case started");
        testReporter.assignCategory("Delete Member API");
        
        Map<String, String> memberOne = new HashMap<String, String>();
        
        memberOne.put("mobilephone", phoneNumberFirst);
        memberOne.put("email", email);
        memberOne.put("appmemberid", appMemberId);
        memberOne.put("clientmemberid", clientMemberId);

        testReporter.log(LogStatus.INFO, "Delete member using PhoneNumber and verify all information is in DB");
        String memberId = com.mPulse.utility.AudienceApi.createNewMember(memberOne);
        com.mPulse.utility.AudienceApi.deleteMemberByPhoneNumber(phoneNumberFirst);
        String query = "select * from audience_member_deleted where audience_member_id="+ memberId;
        Map<String, String> memberData = mPulseDB.getResultMap(query);
        Assert.assertEquals(memberData.get("email"), email);
        Assert.assertEquals(memberData.get("mobile_phone"), phoneNumberFirst);
        Assert.assertEquals(memberData.get("app_member_id"), appMemberId);
        Assert.assertEquals(memberData.get("client_member_id"), clientMemberId);
    }

}

