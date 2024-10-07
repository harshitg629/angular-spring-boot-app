package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface StudentsRepository extends JpaRepository<Student, Long> {
    @Query("SELECT s FROM Student s WHERE " +
            "(?1 IS NULL OR s.studentId = ?1) AND " +
            "(?2 IS NULL OR s.className = ?2) AND " +
            "(?3 IS NULL OR s.score BETWEEN ?3 AND ?4) AND " +
            "(?5 IS NULL OR s.dob BETWEEN ?5 AND ?6)")
    Page<Student> findStudentsByFilters(Integer studentId, String className, Integer startScore, Integer endScore, LocalDate startDate, LocalDate endDate, Pageable pageable);

}
