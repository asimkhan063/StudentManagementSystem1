package com.asim.studentmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asim.studentmanagement.model.Students;

public interface StudentRepository extends JpaRepository<Students, Long> {

    boolean existsByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    Page<Students> findByActiveTrue(Pageable pageable);
    List<Students> findByActiveTrue();

    @Query("SELECT s FROM Students s LEFT JOIN FETCH s.enrollments WHERE s.active = true AND SIZE(s.enrollments) > 0")
    Page<Students> findAllWithEnrollments(Pageable pageable);

    @Query("SELECT s FROM Students s LEFT JOIN FETCH s.enrollments e LEFT JOIN FETCH e.course WHERE s.id = :id")
    Optional<Students> findStudentWithCoursesById(@Param("id") Long id);
}