package com.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entity.StudentEntity;
import com.repository.StudentRepository;

@Service
public class StudentService {
	
	@Autowired
	StudentRepository studentRepository;
	
	public List<StudentEntity> findStudentsByNamePrefix(String prefix) {
	    List<StudentEntity> students = studentRepository.findByFirstThreeLetter(prefix);
	    return students;
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
