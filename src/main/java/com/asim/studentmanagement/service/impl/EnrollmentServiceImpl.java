package com.asim.studentmanagement.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.asim.studentmanagement.dto.CourseDTO;
import com.asim.studentmanagement.dto.EnrollmentDTO;
import com.asim.studentmanagement.dto.EnrollmentSummaryDTO;
import com.asim.studentmanagement.model.Courses;
import com.asim.studentmanagement.model.Enrollment;
import com.asim.studentmanagement.model.Students;
import com.asim.studentmanagement.repository.CourseRepository;
import com.asim.studentmanagement.repository.EnrollmentRepository;
import com.asim.studentmanagement.repository.StudentRepository;
import com.asim.studentmanagement.service.EnrollmentService;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

	private static final Logger log = LoggerFactory.getLogger(EnrollmentServiceImpl.class);

	private final EnrollmentRepository enrollmentRepository;
	private final StudentRepository studentRepository;
	private final CourseRepository courseRepository;
	private final ModelMapper mapper;

	public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository,
			StudentRepository studentRepository,
			CourseRepository courseRepository,
			ModelMapper mapper) {
		this.enrollmentRepository = enrollmentRepository;
		this.studentRepository = studentRepository;
		this.courseRepository = courseRepository;
		this.mapper = mapper;
	}

	@Override
	public void enrollStudentToCourses(EnrollmentDTO enrollmentDTO) {
		log.info("Enrolling student to courses");

		Students student = studentRepository.findById(enrollmentDTO.getStudentId())
				.orElseThrow(() -> new RuntimeException("Student not found"));

		for(Long courseId : enrollmentDTO.getCourseIds()) {
			Courses course = courseRepository.findById(courseId)
					.orElseThrow(() -> new RuntimeException("Course not found"));

			if(enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), courseId)) {
				continue;
			}

			Enrollment enrollment = new Enrollment();
			enrollment.setStudent(student);
			enrollment.setCourse(course);

			student.getEnrollments().add(enrollment);
			course.getEnrollments().add(enrollment);

			enrollmentRepository.save(enrollment);
		}
	}

	/** ✅ Helper method - Duplicate code avoid karta hai */
	private EnrollmentSummaryDTO createSummaryDTO(Students student, boolean includeCourses) {
		EnrollmentSummaryDTO dto = new EnrollmentSummaryDTO();
		dto.setStudentId(student.getId());
		dto.setStudentName(student.getFirstName() + " " + student.getLastName());
		dto.setEmail(student.getEmail());

		int courseCount = (student.getEnrollments() != null) ? student.getEnrollments().size() : 0;
		dto.setCourseCount(courseCount);

		BigDecimal totalFee = BigDecimal.ZERO;
		if (student.getEnrollments() != null && !student.getEnrollments().isEmpty()) {
			totalFee = student.getEnrollments().stream()
					.map(enr -> enr.getCourse().getFee())
					.filter(fee -> fee != null)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
		}
		dto.setTotalFee(totalFee);

		if (includeCourses && courseCount > 0) {
			List<CourseDTO> courseList = student.getEnrollments().stream()
					.map(enr -> enr.getCourse())
					.map(course -> mapper.map(course, CourseDTO.class))
					.collect(Collectors.toList());
			dto.setCourseList(courseList);
		}

		return dto;
	}

	@Override
	public Page<EnrollmentSummaryDTO> getEnrolledStudents(int page, int size) {
		log.info("Getting enrolled students page: {}", page);

		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Students> enrolledStudentsPage = studentRepository.findAllWithEnrollments(pageRequest);

		return enrolledStudentsPage.map(student -> createSummaryDTO(student, false));
	}

	@Override
	public EnrollmentSummaryDTO findEnrolledStudentCourseDetails(Long studentId) {
		Students student = studentRepository.findById(studentId)
				.orElseThrow(() -> new RuntimeException("Student not found"));

		if (student.getEnrollments() == null || student.getEnrollments().isEmpty()) {
			throw new RuntimeException("No enrollments found for student");
		}

		return createSummaryDTO(student, true);
	}

	@Override
	public List<EnrollmentSummaryDTO> getRecentlyEnrolledStudents() {
		log.info("Getting recently enrolled students");
		PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id"));
		Page<Students> recentStudents = studentRepository.findAllWithEnrollments(pageRequest);

		return recentStudents.map(student -> createSummaryDTO(student, false))
				.getContent();
	}
}