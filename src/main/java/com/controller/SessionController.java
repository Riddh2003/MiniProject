package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bean.LoginBean;
import com.entity.AdminEntity;
import com.entity.StudentEntity;
import com.repository.AdminRepository;
import com.repository.StudentRepository;
import com.service.AdminService;
import com.utility.JwtUtility;


@RestController
@RequestMapping("/api/public/session")
public class SessionController {
	
	@Autowired
	BCryptPasswordEncoder encoder;
	
	@Autowired
	StudentRepository studentRepository;
	
	@Autowired
	AdminRepository adminRepository;
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	JwtUtility jwtUtility;
	
	@PostMapping("/student")
	public ResponseEntity<?> addStudentDetails(@RequestBody StudentEntity studentEntity){
		studentEntity.setPassword(encoder.encode(studentEntity.getPassword()));
		studentRepository.save(studentEntity);
		return ResponseEntity.ok("Success");
	}
	
	@PostMapping("/admin")
	public ResponseEntity<?> addAdmin(@RequestBody AdminEntity adminEntity){
		adminEntity.setPassword(encoder.encode(adminEntity.getPassword()));
		adminRepository.save(adminEntity);
		return ResponseEntity.ok("Success");
	}
	
	@PostMapping("/adminlogin")
	public ResponseEntity<?> adminLogin(@RequestBody LoginBean loginBean){
		if(loginBean.getEmail().isEmpty() || loginBean.getPassword().isEmpty()){
			return ResponseEntity.badRequest().body("Please, fill the details.");
		}else {
			AdminEntity admin = adminService.authenticateAdmin(loginBean.getEmail(), loginBean.getPassword());
			if(admin != null) {
				String token = jwtUtility.generateToken(loginBean.getEmail());
				return ResponseEntity.ok().header("Authorization", "Bearer "+token).body("Login successfully as Admin.");
			}
			else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email and password.");
			}
		}
	}
}
/*	
 * 	session controller -> public
 * 		1) Student login
 * 		2) student registration
 * 	
 * 	student controller -> private
 * 		1) student details using id
 * 		2) searching functionality using name first three letters
 * 
 * */