package com.billcom.app.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/*
 * Class User 
 * 
 */
@Entity

public class UserApp {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private long id;
	private String firstName;
	private String lastName;

	@Column(unique = true)
	private String mobile;

	@Column(unique = true)
	private String email;

	private String password;
	private boolean active;
	private String adresse;
	private boolean pendingFirstLogin;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Role> roles;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private Set<TeamMember> teamMember;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	private Set<TeamLeader> teamLead;

	@Column(name = "reset_token")
	private String resetToken;
	private String photoName;
	private int passportNumber;
	private LocalDate visaValidateDate;
	private LocalDate passeportValidityDate;

	@ElementCollection
	private List<String> files;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<Skill> skills;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<Notification> notifcation;
	
	public UserApp() {
		this.active = true;
		this.pendingFirstLogin = false;
	}

	public boolean isPendingFirstLogin() {
		return pendingFirstLogin;
	}

	public void setPendingFirstLogin(boolean pendingFirstLogin) {
		this.pendingFirstLogin = pendingFirstLogin;
	}

	public UserApp(long id, String firstName, String lastName, String mobile, String email, String password,
			boolean active, Set<Role> roles, String resetToken) {
		this();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobile = mobile;
		this.email = email;
		this.password = password;
		this.active = active;
		this.roles = roles;
		this.resetToken = resetToken;
	}

	public UserApp(String firstName, String lastName, String mobile, String email, String hashPassword,
			Set<Role> roleList, String adresse) {
		this();
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobile = mobile;
		this.email = email;
		this.password = hashPassword;
		this.roles = roleList;
		this.adresse = adresse;
	}

	
	public UserApp(long id, String firstName, String lastName, int passportNumber) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.passportNumber = passportNumber;
	}

	public UserApp(long id, String firstName, String lastName,Set<Skill> skills) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.skills = skills;
	}

	public UserApp(long id, String firstName, String lastName, boolean active) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.active = active;
	}

	public UserApp(long id, String firstName, String lastName, String mobile, String email, boolean active,
			Set<Role> roleList, String adresse) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobile = mobile;
		this.email = email;
		this.active = active;
		this.roles = roleList;
		this.adresse = adresse;
	}

	public UserApp(long id, String firstName, String lastName, String mobile, String email, boolean active,
			Set<Role> roleList, String adresse,Set<Skill> skills) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobile = mobile;
		this.email = email;
		this.active = active;
		this.roles = roleList;
		this.adresse = adresse;
		this.skills = skills;
	}
	public UserApp(String firstName, String lastName, String mobile, String email, String adresse) {
		this();
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobile = mobile;
		this.email = email;
		this.adresse = adresse;
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}

	public UserApp createUser() {
		return new UserApp(id, firstName, lastName, mobile, email, active, roles, adresse);

	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public int getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(int passportNumber) {
		this.passportNumber = passportNumber;
	}

	public LocalDate getVisaValidateDate() {
		return visaValidateDate;
	}

	public void setVisaValidateDate(LocalDate visaValidateDate) {
		this.visaValidateDate = visaValidateDate;
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

	public LocalDate getPasseportValidityDate() {
		return passeportValidityDate;
	}

	public void setPasseportValidityDate(LocalDate passeportValidityDate) {
		this.passeportValidityDate = passeportValidityDate;
	}

	public Set<Skill> getSkills() {
		return skills;
	}

	public void setSkills(Set<Skill> skills) {
		this.skills = skills;
	}

	
	public Set<Notification> getNotifcation() {
		return notifcation;
	}

	public void setNotifcation(Set<Notification> notifcation) {
		this.notifcation = notifcation;
	}

	@Override
	public String toString() {
		return "UserApp [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", mobile=" + mobile
				+ ", email=" + email + ", password=" + password + ", active=" + active + ", adresse=" + adresse
				+ ", pendingFirstLogin=" + pendingFirstLogin + ", roles=" + roles + ", teamMember=" + teamMember
				+ ", teamLead=" + teamLead + ", resetToken=" + resetToken + ", photoName=" + photoName
				+ ", passportNumber=" + passportNumber + ", visaValidateDate=" + visaValidateDate
				+ ", passeportValidityDate=" + passeportValidityDate + ", files=" + files + ", skills=" + skills + "]";
	}

}
