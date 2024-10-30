package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.service.StudentService;

@RestController
@RequestMapping("/api/private/admin")
public class AdminController {
	
	@Autowired
	StudentService studentService;
	
	@GetMapping("/search")
	public ResponseEntity<?> searchStudent(@RequestParam("prefix") String prefix){
		return ResponseEntity.ok(studentService.findStudentsByNamePrefix(prefix));
	}
	
	@GetMapping
	public ResponseEntity<?> getStudentById(@RequestParam("studentId") Integer studentId){
		return ResponseEntity.ok(studentService.findById(studentId));
	}
}