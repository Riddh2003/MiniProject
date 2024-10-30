package com.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.entity.AdminEntity;
import com.repository.AdminRepository;

@Service
public class AdminService {
	
	@Autowired
	AdminRepository adminRepository;
	
	@Autowired
	BCryptPasswordEncoder encoder;
	
	public AdminEntity authenticateAdmin(String email,String password) {
		Optional<AdminEntity> op = adminRepository.findByEmail(email);
		AdminEntity admin = op.get();
		if(!op.isEmpty() && encoder.matches(password, admin.getPassword())) {
			return admin;
		}
		return null;
	}
}
