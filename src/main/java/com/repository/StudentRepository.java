package com.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.entity.StudentEntity;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Integer> {
	
	@Query("SELECT s.firstName, s.lastName, s.email, s.mobileNo FROM StudentEntity s WHERE s.firstName LIKE CONCAT(:prefix,'%')")
	List<Object[]> findByFirstThreeLetter(@Param("prefix") String prefix);
}
