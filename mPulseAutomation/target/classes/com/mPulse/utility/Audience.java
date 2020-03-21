package com.mPulse.utility;


public class Audience {

	private String firstName;
	private String lastName;
	private String customSingleLine;
	private String mobilePhone;
	private String singleLine;
	private String clientMemberId;
	private String customNumber;

	public Audience(String firstName, String lastName, String mobilePhone,
			String clientMemberId, String customSingleLine, String singleLine) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.customSingleLine = customSingleLine;
		this.mobilePhone = mobilePhone;
		this.singleLine = singleLine;
		this.clientMemberId = clientMemberId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCustomSingleLine() {
		return customSingleLine;
	}

	public void setCustomSingleLine(String customSingleLine) {
		this.customSingleLine = customSingleLine;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getSingleLine() {
		return singleLine;
	}

	public void setAppMemberId(String singleLine) {
		this.singleLine = singleLine;
	}

	public String getClientMemberId() {
		return clientMemberId;
	}

	public void setClientMemberId(String clientMemberId) {
		this.clientMemberId = clientMemberId;
	}

	public String getCustomNumber() {
		return customNumber;
	}

	public void setCustomNumber(String customNumber) {
		this.customNumber = customNumber;
	}
}

