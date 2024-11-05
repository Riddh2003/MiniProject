package com.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/private/admin")
public class AdminController {
	
	@Autowired
	StudentService studentService;
	
	@GetMapping("/search")
	public ResponseEntity<?> searchStudent(@RequestParam("prefix") String prefix){
		if(prefix == null || prefix.length()<1) {
			return ResponseEntity.ok("Student Not Found...");
		}
		return ResponseEntity.ok(studentService.findStudentsByNamePrefix(prefix));
	}
	
	@GetMapping
	public ResponseEntity<?> getStudentById(@RequestParam("studentId") Integer studentId){
		return ResponseEntity.ok(studentService.findById(studentId));
	}
	
	@GetMapping("/logout")
	public ResponseEntity<?> logoutAdmin(HttpServletRequest request){
		String authHeader = request.getHeader("Authorization");
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			Set<String> blackListedToken = new HashSet<>();
			blackListedToken.add(token);
			return ResponseEntity.ok("Logout successfully as admiin...");
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No token found in request...");
		}
	}
}