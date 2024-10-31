package com.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "students")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StudentEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer studentId;
	String firstName;
	String lastName;
	String password;
	String email;
	String mobileNo;
	String collegeName;
	String batch;
	Integer discipline;
	Integer regularSessions;
	Integer communicationInSessions;
	Integer testPerformance;
}
