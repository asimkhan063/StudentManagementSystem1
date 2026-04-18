/**package com.asim.studentmanagement.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.asim.studentmanagement.dto.EnrollmentDTO;
import com.asim.studentmanagement.dto.EnrollmentSummaryDTO;

/*
 * Copyright (c) 2026 Mahesh Shet
 * Licensed under the MIT License.


public interface EnrollmentService {

	void enrollStudentToCourses(EnrollmentDTO enrollmentDTO);

	Page<EnrollmentSummaryDTO> getEnrolledStudents(int page, int size);

	EnrollmentSummaryDTO findEnrolledStudentCourseDetails(Long studentId);

	List<EnrollmentSummaryDTO> getRecentlyEnrolledStudents();

}*/
package com.asim.studentmanagement.service;

import org.springframework.data.domain.Page;

import com.asim.studentmanagement.dto.EnrollmentDTO;
import com.asim.studentmanagement.dto.EnrollmentSummaryDTO;

public interface EnrollmentService {
    void enrollStudentToCourses(EnrollmentDTO enrollmentDTO);
    Page<EnrollmentSummaryDTO> getEnrolledStudents(int page, int size);
    EnrollmentSummaryDTO findEnrolledStudentCourseDetails(Long studentId);
    java.util.List<EnrollmentSummaryDTO> getRecentlyEnrolledStudents();
}