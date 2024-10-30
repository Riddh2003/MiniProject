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
@Table(name = "admin")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminEntity {	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer adminId;
	String firstName;
	String lastName;
	String email;
	String password;
	String mobileNo;
}