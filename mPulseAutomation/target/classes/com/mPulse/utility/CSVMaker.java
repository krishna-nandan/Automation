package com.mPulse.utility;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CSVMaker {

	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String FILE_HEADER = "firstName,lastName,mobilePhone,customSingleLine1,clientMemberId,customSingleLine2";

	public static void writeCsvFile(String fileName) {

		List<Audience> members = new ArrayList<Audience>();

		for(int i=1; i<=50000; i++) {
			Audience member = new Audience("first"+getRandomString(), "last"+getRandomString(),
					RandomeUtility.getRandomPhoneNumber(), "cli"+getRandomString(),
					getRandomString(), getRandomString());
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
			for (Audience member : members) {
				fileWriter.append(member.getFirstName());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(member.getLastName());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(member.getMobilePhone());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(member.getCustomSingleLine());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(member.getClientMemberId());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(member.getSingleLine());
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

	public static String getRandomString() {
		return "k"+RandomStringUtils.randomAlphanumeric(6).toLowerCase()
				+RandomStringUtils.randomAlphanumeric(4).toLowerCase();
	}

	public static void main(String arg[]) {
		writeCsvFile(UtilityMethods.getCsvFilePath("bulkPhoneNumberCSVForTest1"));
		writeCsvFile(UtilityMethods.getCsvFilePath("bulkPhoneNumberCSVForTest2"));
		writeCsvFile(UtilityMethods.getCsvFilePath("bulkPhoneNumberCSVForTest3"));
		writeCsvFile(UtilityMethods.getCsvFilePath("bulkPhoneNumberCSVForTest4"));
		writeCsvFile(UtilityMethods.getCsvFilePath("bulkPhoneNumberCSVForTest5"));
	}
}

