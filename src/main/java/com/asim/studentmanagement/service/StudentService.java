package com.asim.studentmanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.asim.studentmanagement.dto.StudentDTO;
import com.asim.studentmanagement.model.Students;

public interface StudentService {
    StudentDTO createStudent(StudentDTO studentDTO);
    Page<StudentDTO> getStudents(int page, int size);
    StudentDTO getStudentById(Long id);
    StudentDTO updateStudent(Long id, StudentDTO studentDTO);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
    List<StudentDTO> getAllStudents();
    Optional<Students> findById(Long id);
}