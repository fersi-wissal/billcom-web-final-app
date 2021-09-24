package com.billcom.app.service;

import static java.nio.file.Files.copy;

import static java.nio.file.Files.copy;

import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.billcom.app.dto.UserDto;
import com.billcom.app.dto.UserSkillDto;
import com.billcom.app.entity.Role;
import com.billcom.app.entity.Skill;
import com.billcom.app.entity.Task;
import com.billcom.app.entity.UserApp;
import com.billcom.app.exception.NotFoundException;
import com.billcom.app.model.PasswordUpdateModel;
import com.billcom.app.repository.RoleRepository;
import com.billcom.app.repository.SkillRepository;
import com.billcom.app.repository.UserRepository;
import com.billcom.app.security.SecurityUtils;

import net.bytebuddy.utility.RandomString;

@Service
public class UserService {

	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private SkillRepository skillRepository;

	private JavaMailSender mailSender;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private SecurityUtils securityUtils;
	public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/userFile";
	public static final String USERDIRECTORY = System.getProperty("user.dir")
			+ "/src/main/resources/static/assets/userPhoto";

	@Autowired
	public UserService(UserRepository userRepository, RoleRepository roleRepository,SkillRepository skillRepository,
			BCryptPasswordEncoder bCryptPasswordEncoder, SecurityUtils securityUtils, JavaMailSender mailSender) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.securityUtils = securityUtils;
		this.mailSender = mailSender;
		this.skillRepository =  skillRepository;
	}

	public UserApp getCurrentUser() {
		return securityUtils.getLoggedUser();
	}

	/** Create a new User */

	public void addUser(UserDto userDto, @RequestParam("file") MultipartFile multipartFile) {

		UserApp userLogged = getCurrentUser();
		userLogged.getRoles().stream().forEach(role -> {
			if (securityUtils.checkifUserLoggedIsManager(userLogged)
					& !userRepository.findByEmail(userDto.getEmail()).isPresent()) {

				String hashPassword = bCryptPasswordEncoder.encode(userDto.getPassword());

				Set<Role> roleLis = userDto.getRoles().stream().filter(r -> roleRepository.existsByName(r))
						.map(r -> roleRepository.findByName(r)).collect(Collectors.toSet());

				UserApp user = userDto.fromDtoToUser();

				user.setPassword(hashPassword);
				user.setRoles(roleLis);

				String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());

				Path fileStorage = get(USERDIRECTORY, filename).toAbsolutePath().normalize();

				try {
					copy(multipartFile.getInputStream(), fileStorage, REPLACE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
				}

				user.setPhotoName(filename);

				userRepository.save(user);
				sendEmailRegisterUser(userDto.getEmail(), userDto.getPassword(), userDto.getFirstName(),
						userDto.getLastName());
			}

		});

	}

	public void addUserWithoutPhoto(UserDto userDto) {

		UserApp userLogged = getCurrentUser();
		userLogged.getRoles().stream().forEach(role -> {
			if (securityUtils.checkifUserLoggedIsManager(userLogged)
					& !userRepository.findByEmail(userDto.getEmail()).isPresent()) {

				String hashPassword = bCryptPasswordEncoder.encode(userDto.getPassword());

				Set<Role> roleLis = userDto.getRoles().stream().filter(r -> roleRepository.existsByName(r))
						.map(r -> roleRepository.findByName(r)).collect(Collectors.toSet());

				UserApp user = userDto.fromDtoToUser();

				user.setPassword(hashPassword);
				user.setRoles(roleLis);

				

				userRepository.save(user);
				sendEmailRegisterUser(userDto.getEmail(), userDto.getPassword(), userDto.getFirstName(),
						userDto.getLastName());
			}

		});

	}

	public void create(UserDto userDto) {

		String hashPassword = bCryptPasswordEncoder.encode(userDto.getPassword());
		Set<Role> roleList = new HashSet<>();
		userDto.getRoles().stream().forEach(r -> {
			if (roleRepository.existsByName(r)) {
				Role newRole = roleRepository.findByName(r);
				roleList.add(newRole);
			}
		}

		);
		UserApp user = new UserApp(userDto.getFirstName(), userDto.getLastName(), userDto.getMobile(),
				userDto.getEmail(), hashPassword, roleList, userDto.getAdresse());
		userRepository.save(user);
	}

	/** Update an Existing User */
	@Transactional

	public void updateUser(Long id, UserDto userDto) {

		userRepository.findById(id).map(user -> {

			return userRepository.save(userDto.updateFromDto(user));
		}).orElseThrow(() -> new NotFoundException("User Not Exists"));

	}

	/**
	 * @param id
	 * @return an existing user
	 */

	public UserApp getUser(Long id) {
		UserApp userLogged = getCurrentUser();
		if (checkifUserLoggedIsManager(userLogged)) {
			return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User NOt FOUND"));

		}
		return null;

	}

	/** @return list Users */
	public List<UserApp> getUsers() {
		UserApp userLogged = getCurrentUser();
		if (securityUtils.checkifUserLoggedIsManager(userLogged)) {
			return userRepository.findAll().stream().collect(Collectors.mapping(
					user -> new UserApp(user.getId(), user.getFirstName(), user.getLastName(), user.getMobile(),
							user.getEmail(), user.isActive(), getRoleList(user), user.getAdresse(), user.getSkills()),
					Collectors.toList())

			);

		}

		return new ArrayList<>();
	}

	/** get RoleList for Existing User */

	public Set<Role> getRoleList(UserApp user) {
		Set<Role> roleList = new HashSet<>();
		user.getRoles().stream().forEach(r -> roleList.add(r));
		return roleList;
	}

	/** Get List Of User With Role Member */

/*	public List<UserApp> getListUserWithRoleMember() {
		return userRepository.findAll().stream().filter(UserApp::isActive).collect(Collectors.toList()).stream()
				.filter(user -> user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("member")))
				.collect(Collectors.toList());

	}*/
	
	public List<UserApp> getListUserWithRoleMember() {
		return userRepository.findAll().stream().filter(UserApp::isActive).collect(Collectors.toList()).stream()
				.filter(user -> user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("member")))
				.collect(Collectors.mapping(
						user -> new UserApp(user.getId(),  user.getFirstName(),user.getLastName(), user.isActive()),
						Collectors.toList()));

	}
	public List<UserApp> getListMemberWithSkill(String name) {
		return userRepository.findAll().stream().filter(UserApp::isActive).collect(Collectors.toList()).stream()
				.filter(user -> user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("member")))
				.filter(user -> user.getSkills().stream().anyMatch(skill -> skill.getRoleLabel().equalsIgnoreCase(name)))
				.collect(Collectors.mapping(
						user -> new UserApp(user.getId(),  user.getFirstName(),user.getLastName(),user.isActive() ),
						Collectors.toList()));

	}
	

	/** Get List Of User With Role Leader Api Stream */

	public List<UserApp> getListUserWithRoleLeader() {
		return userRepository.findAll().stream().filter(UserApp::isActive).collect(Collectors.toList()).stream()
				.filter(user -> user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("leader")))
				.collect(Collectors.toList());
	}
	
	
	
	
	
	

	/** Get List Of User With Role Project Leader Api Stream */

	public List<UserApp> getListUserWithRoleProjectLeader() {
		return userRepository.findAll().stream().filter(UserApp::isActive).collect(Collectors.toList()).stream().filter(
				user -> user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("project Leader")))
				.collect(Collectors.toList());
	}
	
	
	
	
	

	/** send e-mail Reset Password to User */

	public void sendSimpleEmail(String email) {

		UserApp user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("email not found"));
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("apptestbillcom@gmail.com");
		message.setTo(email);
		String token = RandomString.make(30);
		message.setText(token);
		message.setSubject("Reset Password");
		user.setResetToken(token);
		userRepository.save(user);
		mailSender.send(message);

	}

	public void sendEmailRegisterUser(String email, String password, String firstName, String lastName) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("apptestbillcom@gmail.com");
		message.setTo(email);
		message.setText(
				"Bonjour " + firstName + " " + " " + lastName + "," + "\n" + "Bienvenue chez Billcom Consulting,"
						+ "Vous êtes inscrit à l'application billcom Virtual, Voici vos coordonnées : " + "\n"
						+ "Votre email est  :   " + email + "\n" + "Votre mot de passe est : " + password + "\n"
						+ "Vous devez changer votre mot de passe avant de vous connectez la première fois." + "\n"
						+ "Voici le lien pour se connecter à l'application : " + "http://localhost:4200/login" + "\n"
						+ "Trés Bien Cordialement");
		message.setSubject("[Accés à la plateforme Billcom Consulting]");
		mailSender.send(message);

	}

	public void deleteAll() {
		userRepository.deleteAll();
	}

	/** update password User */

	public UserApp updatePassword(String code, String passwordUpdated) {

		UserApp user = userRepository.findUserByResetToken(code)
				.orElseThrow(() -> new NotFoundException("Reset Token is Empty"));
		user.setPassword(bCryptPasswordEncoder.encode(passwordUpdated));
		user.setResetToken(null);
		userRepository.save(user);
		return user;
	}

	public void updatePasswordAttemptFirstLogin(PasswordUpdateModel updatePasswordModel) {
		UserApp user = userRepository.findUserByEmail(updatePasswordModel.getEmail())
				.orElseThrow(() -> new NotFoundException("please inter your valid email that you have receive"));

		if (bCryptPasswordEncoder.matches(updatePasswordModel.getPassword(), user.getPassword())) {

			user.setPassword(bCryptPasswordEncoder.encode(updatePasswordModel.getNewPassword()));
			user.setPendingFirstLogin(true);

		}
		userRepository.save(user);

	}

	/***** Check code Valid *********/
	public void checkCode(String code) {

		userRepository.findUserByResetToken(code).orElseThrow(() -> new NotFoundException("Reset Token is Empty"));

	}

	/** update User Status */

	public void updateUserStatus(long id, boolean setActive) {
		UserApp user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found"));
		user.setActive(setActive);
		userRepository.save(user);

	}

	public byte[] getPhoto(Long id) throws Exception {
		UserApp user = userRepository.findById(id).get();

		return Files.readAllBytes(Paths.get(USERDIRECTORY + "/" + user.getPhotoName()));

	}

	public void uploadPhoto(MultipartFile file, Long id) throws Exception {

		UserApp user = userRepository.findById(id).get();
		user.setPhotoName(file.getOriginalFilename() + RandomString.make(10));
		Files.write(Paths.get(USERDIRECTORY + "/" + user.getPhotoName()), file.getBytes());

		userRepository.save(user);

	}

	public boolean checkifUserLoggedIsManager(UserApp user) {
		if ((user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("manager")))) {
			return true;

		} else {
			throw new NotFoundException("User Does not have role manager");
		}
	}

	public boolean checkifFirstAttemptLoggin(String email) {
		UserApp user = userRepository.findUserByEmail(email)
				.orElseThrow(() -> new NotFoundException("User Not Exists"));

		return user.isPendingFirstLogin();

	}

	public void adSkillsToUser(long id, Skill skill) {

		userRepository.findById(id).map(user -> {
			if (!(user.getSkills().stream().anyMatch(s -> s.getRoleLabel().equalsIgnoreCase(skill.getRoleLabel()))))
				user.getSkills().add(skill);
			return userRepository.save(user);

		}).orElseThrow(() -> new NotFoundException("User Not Found"));
	}

	public List<String> uploadFiles(long id, List<MultipartFile> multipartFiles) throws IOException {
		UserApp user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
		List<String> filenames = user.getFiles();
		for (MultipartFile file : multipartFiles) {

			String filename = StringUtils.cleanPath(file.getOriginalFilename());

			Path fileStorage = get(uploadDirectory, filename).toAbsolutePath().normalize();

			copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);
			filenames.add(filename);

		}

		user.setFiles(filenames);
		userRepository.save(user);
		return filenames;
	}

	public ResponseEntity<Resource> downloadFiles(String filename) throws IOException {
		Path filePath = get(uploadDirectory).toAbsolutePath().normalize().resolve(filename);
		if (!Files.exists(filePath)) {
			throw new FileNotFoundException(filename + "this file ins not found");
		}

		Resource resource = new UrlResource(filePath.toUri());
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("File-Name", filename);
		httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
				.headers(httpHeaders).body(resource);
	}

}
