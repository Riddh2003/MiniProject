package com.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entity.StudentEntity;
import com.repository.StudentRepository;

@Service
public class StudentService {
	
	@Autowired
	StudentRepository studentRepository;
	
	public List<String> findStudentsByNamePrefix(String prefix) {
		List<Object[]> student = studentRepository.findByFirstThreeLetter(prefix);
		return student.stream()
				.map(result -> "First Name : "+result[0]+", Last Name : "+result[1]+", Email : "+result[2]+", Mobile No. : "+result[3])
				.collect(Collectors.toList());
	}
		
	public StudentEntity findById(Integer studentId) {
		Optional<StudentEntity> optional = studentRepository.findById(studentId);
		if(optional.isEmpty()) {
			return null;
		}
		StudentEntity student = optional.get();
		return student;
	}
}
