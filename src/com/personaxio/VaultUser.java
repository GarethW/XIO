package com.personaxio;

import java.sql.Timestamp;
import java.util.Date;

public class VaultUser {

	private String userID;
	private String eMail;
	private String password;
	private String foreName;
	private String lastName;
	private Date dob;
	private String address1;
	private String address2;
	private String town;
	private String county;
	private String postCode;
	private String country;
	private String mobileNumber;
	private String creditCardNumber;
	private String ccProvider;
	private String ccExpiresMonth;
	private String ccExpiresYear;
	private String ccvNumber;
	private boolean consent;
	private Timestamp registrationDate;
	
	public VaultUser()
	{
		
	}

	public String getUserID() {
		return userID;
	}

	public String geteMail() {
		return eMail;
	}

	public String getPassword() {
		return password;
	}

	public String getForeName() {
		return foreName;
	}

	public String getLastName() {
		return lastName;
	}

	public Date getDob() {
		return dob;
	}

	public String getAddress1() {
		return address1;
	}

	public String getAddress2() {
		return address2;
	}

	public String getTown() {
		return town;
	}

	public String getCounty() {
		return county;
	}

	public String getPostCode() {
		return postCode;
	}

	public String getCountry() {
		return country;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public String getCcProvider() {
		return ccProvider;
	}

	public String getCCVNumber() {
		return ccvNumber;
	}

	public boolean isConsent() {
		return consent;
	}

	public Timestamp getRegistrationDate() {
		return registrationDate;
	}
	// Setters
	
	public void setUserID(String userID) {
		this.userID = userID;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setForeName(String foreName) {
		this.foreName = foreName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setDob(java.util.Date date) {
		this.dob = date;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public void setCcProvider(String ccProvider) {
		this.ccProvider = ccProvider;
	}



	public void setCCVNumber(String secNumber) {
		this.ccvNumber = secNumber;
	}

	public void setConsent(boolean consent) {
		this.consent = consent;
	}

	public String getCcExpiresMonth() {
		return ccExpiresMonth;
	}

	public String getCcExpiresYear() {
		return ccExpiresYear;
	}

	public void setCcExpiresMonth(String ccExpiresMonth) {
		this.ccExpiresMonth = ccExpiresMonth;
	}

	public void setCcExpiresYear(String ccExpiresYear) {
		this.ccExpiresYear = ccExpiresYear;
	}
	
	public void setRegistrationDate(Timestamp value)
	{
		this.registrationDate= value;
	}
}
