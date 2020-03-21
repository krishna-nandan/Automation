package com.mPulse.utility;

public class Member {

	private String firstName;
	private String lastName;
	private String email;
	private String mobilePhone;
	private String appMemberId;
	private String clientMemberId;
	private String customNumber;

	public Member(String firstName, String lastName, String email, String mobilePhone, String appMemberId,
			String clientMemberId, String customNumber) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.mobilePhone = mobilePhone;
		this.appMemberId = appMemberId;
		this.clientMemberId = clientMemberId;
		this.customNumber = customNumber;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getAppMemberId() {
		return appMemberId;
	}

	public void setAppMemberId(String appMemberId) {
		this.appMemberId = appMemberId;
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
