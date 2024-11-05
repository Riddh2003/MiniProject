package com.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.entity.StudentEntity;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Integer> {
	@Query(value = "SELECT * FROM students s WHERE LOWER(s.first_name) LIKE LOWER(CONCAT(:prefix, '%'))",nativeQuery = true)
	List<StudentEntity> findByFirstThreeLetter(@Param("prefix") String prefix);
}
