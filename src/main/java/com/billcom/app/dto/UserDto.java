package com.billcom.app.dto;

import java.time.LocalDate;
import java.util.Set;



import com.billcom.app.entity.UserApp;



public class UserDto  {


		private String firstName;
	    private String lastName;
		private String mobile;
		private String email;
	    private String password;
	    private boolean active;
	    private String adresse;
	    private Set<String> roles;
	    private int passportNumber;
	    private LocalDate passeportValidityDate;
	    private LocalDate visaValidateDate;
	 
		public UserDto() {
		}
		public UserDto(String firstName, String lastName, String mobile, String email, String password, boolean active,
				String adresse) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.mobile = mobile;
			this.email = email;
			this.password = password;
			this.active = active;
			this.adresse = adresse;
		}
		
	
		public UserDto(String firstName, String lastName, String mobile, String email, String password, boolean active,
				String adresse, Set<String> roles) {
			super();
			this.firstName = firstName;
			this.lastName = lastName;
			this.mobile = mobile;
			this.email = email;
			this.password = password;
			this.active = active;
			this.adresse = adresse;
			this.roles = roles;
		}

		public String getAdresse() {
			return adresse;
		}
		public void setAdresse(String adresse) {
			this.adresse = adresse;
		}
	
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
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
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public boolean isActive() {
			return active;
		}
		public void setActive(boolean active) {
			this.active = active;
		}
		public Set<String> getRoles() {
			return roles;
		}
		public void setRoles(Set<String> roles) {
			this.roles = roles;
		}
		public String getMobile() {
			return mobile;
		}
	 
	public UserApp fromDtoToUser() {
		return new UserApp(firstName, lastName, mobile,email,adresse);
	}
	public UserApp updateFromDto(UserApp user) {

		user.setFirstName(firstName);
		user.setPassportNumber(passportNumber);
		user.setPasseportValidityDate(passeportValidityDate);
		user.setVisaValidateDate(visaValidateDate);
		user.setMobile(mobile);
		user.setLastName(lastName);
		user.setAdresse(adresse);
		
		return user;
	}
	public int getPassportNumber() {
		return passportNumber;
	}
	public void setPassportNumber(int passportNumber) {
		this.passportNumber = passportNumber;
	}
	public LocalDate getPasseportValidityDate() {
		return passeportValidityDate;
	}
	public void setPasseportValidityDate(LocalDate passeportValidityDate) {
		this.passeportValidityDate = passeportValidityDate;
	}
	public LocalDate getVisaValidateDate() {
		return visaValidateDate;
	}
	public void setVisaValidateDate(LocalDate visaValidateDate) {
		this.visaValidateDate = visaValidateDate;
	}
	@Override
	public String toString() {
		return "UserDto [firstName=" + firstName + ", lastName=" + lastName + ", mobile=" + mobile + ", email=" + email
				+ ", password=" + password + ", active=" + active + ", adresse=" + adresse + ", roles=" + roles
				+ ", passportNumber=" + passportNumber + ", passeportValidityDate=" + passeportValidityDate
				+ ", visaValidateDate=" + visaValidateDate + "]";
	}
	
	
	
	
	

}

