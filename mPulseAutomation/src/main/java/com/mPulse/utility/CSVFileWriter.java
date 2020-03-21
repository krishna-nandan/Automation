package com.mPulse.utility;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mPulse.factories.ReporterFactory;
import com.mPulse.utility.Member;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class CSVFileWriter {
	
	private static final String COMMA_DELIMITER = ",";
	private static final String PIPE_DELIMITER = "|";
	private static final String NEW_LINE_SEPARATOR = "\n";
	public static int count=6;
	private static final String CUSTOM_FILE_HEADER = "mobilePhone,customNumber,singleLine,date,time,picklist,multipicklist,para";
	private static final String FILE_HEADER = "firstName,lastName,email,mobilePhone,appMemberId,clientMemberId,customNumber";
	private static final String PIPE_FILE_HEADER = "firstName|lastName|email|mobilePhone|appMemberId|clientMemberId|customNumber";
	
	public static void writeCsvFile(String fileName) {

		// Create new members objects
//		Member member1 = new Member("krishna", "nandan", "krishnanandan@yopmail.com", 19918443544l, "appone", "clientone", 789);
//		Member member2 = new Member("vibhu", "bhasin", "vibhu.bhasin1@yopmail.com", 19918443545l, "apptwo1", "clienttwo1", 689);
//		Member member3 = new Member("vibhu", "bhasin", "vibhu.bhasin2@yopmail.com", 19918443546l, "apptwo2", "clienttwo2", 689);
//		Member member4 = new Member("vibhu", "bhasin", "vibhu.bhasin3@yopmail.com", 19918443547l, "apptwo3", "clienttwo3", 689);
//		Member member5 = new Member("vibhu", "bhasin", "vibhu.bhasin4@yopmail.com", 19918443548l, "apptwo4", "clienttwo4", 689);
//		Member member6 = new Member("vibhu", "bhasin", "vibhu.bhasin5@yopmail.com", 19918443549l, "apptwo5", "clienttwo5", 689);
//
//		// Create a new list of member objects
//		List<Member> members = new ArrayList<Member>();
//		members.add(member1);
//		members.add(member2);
//		members.add(member3);
//		members.add(member4);
//		members.add(member5);
//		members.add(member6);

		List<Member> members = new ArrayList<Member>();
		
		for(int i=1; i<=count; i++) {
			String email = RandomeUtility.getRandomEmails(5);
			Member member = new Member("krishna", "nandan", email, "19918443544", "appone", "clientone", "789");
			members.add(member);
		}
		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(fileName);

			// Write the CSV file header
			fileWriter.append(FILE_HEADER.toString());

			// Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);

			// Write a new member object list to the CSV file
			for (Member member : members) {
				fileWriter.append(member.getFirstName());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(member.getLastName());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(member.getEmail());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(member.getMobilePhone());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(member.getAppMemberId());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(member.getClientMemberId());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(member.getCustomNumber());
				fileWriter.append(NEW_LINE_SEPARATOR);
			}

			System.out.println("Members CSV file was created successfully");

		} catch (Exception e) {
			System.out.println("Error in writing CSV File");
			e.printStackTrace();
		} finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}

		}
	}
	
	public static void writePipeCsvFile(String fileName) {

		// Create new members objects
		Member member1 = new Member("krishna", "nandan", "krishnanandan@yopmail.com", "19918443544", "appone", "clientone", "786");
		Member member2 = new Member("vibhu", "bhasin", "vibhu.bhasin1@yopmail.com", "19918443544", "apptwo1", "clienttwo1", "786");
		Member member3 = new Member("vibhu", "bhasin", "vibhu.bhasin2@yopmail.com", "19918443544", "apptwo2", "clienttwo2", "786");
		Member member4 = new Member("vibhu", "bhasin", "vibhu.bhasin3@yopmail.com", "19918443544", "apptwo3", "clienttwo3", "786");
		Member member5 = new Member("vibhu", "bhasin", "vibhu.bhasin4@yopmail.com", "19918443544", "apptwo4", "clienttwo4", "786");
		Member member6 = new Member("vibhu", "bhasin", "vibhu.bhasin5@yopmail.com", "19918443544", "apptwo5", "clienttwo5", "786");

		// Create a new list of member objects
		List<Member> members = new ArrayList<Member>();
		members.add(member1);
		members.add(member2);
		members.add(member3);
		members.add(member4);
		members.add(member5);
		members.add(member6);

		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(fileName);

			// Write the CSV file header
			fileWriter.append(PIPE_FILE_HEADER.toString());

			// Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);

			// Write a new member object list to the CSV file
			for (Member member : members) {
				fileWriter.append(member.getFirstName());
				fileWriter.append(PIPE_DELIMITER);
				fileWriter.append(member.getLastName());
				fileWriter.append(PIPE_DELIMITER);
				fileWriter.append(member.getEmail());
				fileWriter.append(PIPE_DELIMITER);
				fileWriter.append(member.getMobilePhone());
				fileWriter.append(PIPE_DELIMITER);
				fileWriter.append(member.getAppMemberId());
				fileWriter.append(PIPE_DELIMITER);
				fileWriter.append(member.getClientMemberId());
				fileWriter.append(PIPE_DELIMITER);
				fileWriter.append(member.getCustomNumber());
				fileWriter.append(NEW_LINE_SEPARATOR);
			}

			System.out.println("Members pipe separated CSV file was created successfully");

		} catch (Exception e) {
			System.out.println("Error in writing pipe separated CSV File");
			e.printStackTrace();
		} finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}

		}
	}
	
	public static synchronized String writeCsvFileForSingleMember(String fileName, Member newMember) {

		List<Member> members = new ArrayList<Member>();
		members.add(newMember);

		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(fileName);

			// Write the CSV file header
			fileWriter.append(FILE_HEADER.toString());

			// Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);

			// Write a new member object list to the CSV file
			for (Member member : members) {
				fileWriter.append(member.getFirstName());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(member.getLastName());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(member.getEmail());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(member.getMobilePhone());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(member.getAppMemberId());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(member.getClientMemberId());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(member.getCustomNumber());
				fileWriter.append(NEW_LINE_SEPARATOR);
			}

			System.out.println("Members CSV file was created successfully");

		} catch (Exception e) {
			System.out.println("Error in writing CSV File");
			e.printStackTrace();
		} finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}

		}
		return fileName;
	}
	
	public static synchronized String writePipeDelimiterCsvFileForSingleMember(String fileName, Member newMember) {

		List<Member> members = new ArrayList<Member>();
		members.add(newMember);

		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(fileName);

			// Write the CSV file header
			fileWriter.append(PIPE_FILE_HEADER.toString());

			// Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);

			// Write a new member object list to the CSV file
			for (Member member : members) {
				fileWriter.append(member.getFirstName());
				fileWriter.append(PIPE_DELIMITER);
				fileWriter.append(member.getLastName());
				fileWriter.append(PIPE_DELIMITER);
				fileWriter.append(member.getEmail());
				fileWriter.append(PIPE_DELIMITER);
				fileWriter.append(member.getMobilePhone());
				fileWriter.append(PIPE_DELIMITER);
				fileWriter.append(member.getAppMemberId());
				fileWriter.append(PIPE_DELIMITER);
				fileWriter.append(member.getClientMemberId());
				fileWriter.append(PIPE_DELIMITER);
				fileWriter.append(member.getCustomNumber());
				fileWriter.append(NEW_LINE_SEPARATOR);
			}

			System.out.println("Members pipe separated CSV file was created successfully");
			ExtentTest testReporter = ReporterFactory.getTest();
			testReporter.log(LogStatus.INFO, "Members pipe separated CSV file was created successfully");

		} catch (Exception e) {
			System.out.println("Error in writing pipe separated CSV File");
			e.printStackTrace();
		} finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}

		}
		return fileName;
	}
	
	public static synchronized String writeCsvFileForAllCustomFieldMember(String fileName, String phoneNumber) {

		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(fileName);

			// Write the CSV file header
			fileWriter.append(CUSTOM_FILE_HEADER.toString());

			// Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);
			fileWriter.append(phoneNumber);
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append("678");
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append("this is new single line field");
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append("09/25/2019");
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append("3:30 PM");
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append("one");
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append("hey | bye");
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append("hey | bye");
			fileWriter.append(NEW_LINE_SEPARATOR);

			System.out.println("Members CSV file was created successfully");

		} catch (Exception e) {
			System.out.println("Error in writing CSV File");
			e.printStackTrace();
		} finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}

		}
		return fileName;
	}
}
