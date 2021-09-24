package com.billcom.app.controller;

import java.io.IOException;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.billcom.app.dto.UserDto;
import com.billcom.app.entity.Skill;
import com.billcom.app.entity.UserApp;
import com.billcom.app.model.PasswordUpdateModel;
import com.billcom.app.repository.UserRepository;
import com.billcom.app.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class UserController {

	private UserService userService;

	@Autowired
	public UserController(UserService userService, UserRepository userRepository) {
		this.userService = userService;
	}

	@PostMapping("login/updateFirstLogin")
	public void updatePasswordAttemptFirstLogin(@RequestBody PasswordUpdateModel updatePasswordModel) {
		userService.updatePasswordAttemptFirstLogin(updatePasswordModel);
	}

	@PostMapping("/user/createWithoutPhoto")
	public void createUser(@RequestBody UserDto user) {
		userService.addUserWithoutPhoto(user);
	}
	
	@PostMapping("/user/create")
	public void createUser(@RequestParam String userDto, @RequestParam("file") MultipartFile multipartFile)
			throws IOException {
		UserDto newUser = new ObjectMapper().readValue(userDto, UserDto.class);

		userService.addUser(newUser,multipartFile);
	}
	

	@PostMapping("/user/add")
	public void addUser(@RequestBody UserDto user) {
		userService.create(user);
	}
	@GetMapping("/user/details/{idUser}")
	public UserApp getUser(@PathVariable Long idUser) {
		return userService.getUser(idUser);
	}

	@GetMapping("/user/all")
	public List<UserApp> getUsers() {
		return userService.getUsers();

	}

	@GetMapping("user/ListMember")
	public List<UserApp> getistUserWithRoleMember() {
		return userService.getListUserWithRoleMember();

	}

	@GetMapping("user/ListLeader")
	public List<UserApp> getistUserWithRoleLeader() {
		return userService.getListUserWithRoleLeader();

	}

	@GetMapping("user/ListProjectLeader")
	public List<UserApp> getistUserWithRoleProjectLeader() {
		return userService.getListUserWithRoleProjectLeader();

	}

	@PutMapping("/user/update/{idUser}")
	public void updateUser(@PathVariable Long idUser, @RequestBody UserDto user) {
		userService.updateUser(idUser, user);
	}

	@PostMapping("login/reset/{code}")
	public UserApp updateResetPossword(@PathVariable String code, @RequestBody String password) {
		return userService.updatePassword(code, password);
	}

	@PostMapping("login/check")
	public void checkCode(@RequestBody String code) {
		userService.checkCode(code);
	}

	@DeleteMapping("user/delete")
	public void del() {
		userService.deleteAll();
	}

	@RequestMapping(value = "login/sendEmail", method = RequestMethod.POST)
	public void sendEmail(@RequestBody String email) {

		this.userService.sendSimpleEmail(email);

	}

	@PutMapping("user/updateStatus/{idUser}")
	public void updateStatus(@PathVariable Long idUser, @RequestBody boolean statut) {
		userService.updateUserStatus(idUser, statut);

	}

	@GetMapping(path = "photoUser/{idUser}", produces = MediaType.IMAGE_PNG_VALUE)
	public byte[] getPhoto(@PathVariable("idUser") Long idUser) throws Exception {
		return userService.getPhoto(idUser);

	}

	@PostMapping(path = "user/uploadPhoto/{idUser}")
	public void uploadPhoto(MultipartFile file, @PathVariable Long idUser) throws Exception {
		userService.uploadPhoto(file, idUser);

	}

	@GetMapping(path = "user/loggedUser")
	public UserApp getCurrentUser() {
		return userService.getCurrentUser();

	}

	@GetMapping(path = "login/checkAttemptFirst/{email}")
	public boolean checkAttemptFirst(@PathVariable String email) {

		return userService.checkifFirstAttemptLoggin(email);

	}

	@PutMapping("user/adSkillsToUser/{idUser}")
	public void adSkillsToUser(@PathVariable long idUser, @RequestBody Skill skillDto) {

		userService.adSkillsToUser(idUser, skillDto);

	}

	@RequestMapping(path = "user/uploadFiles/{idUser}", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

	public List<String> uploadFiles(@PathVariable long idUser,
			@RequestParam("files") List<MultipartFile> multipartFiles) throws IOException {
		return userService.uploadFiles(idUser, multipartFiles);
	}

	@GetMapping("user/download/{filename}")
	public ResponseEntity<Resource> downloadFiles(@PathVariable("filename") String filename) throws IOException {

		return userService.downloadFiles(filename);
	}

}
