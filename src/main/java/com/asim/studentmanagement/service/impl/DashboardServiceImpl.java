package com.asim.studentmanagement.service.impl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.asim.studentmanagement.dto.DashboardStatsDTO;
import com.asim.studentmanagement.model.Enrollment;
import com.asim.studentmanagement.model.Students;
import com.asim.studentmanagement.repository.CourseRepository;
import com.asim.studentmanagement.repository.EnrollmentRepository;
import com.asim.studentmanagement.repository.StudentRepository;
import com.asim.studentmanagement.service.DashboardService;

@SuppressWarnings("unused")
@Service
public class DashboardServiceImpl implements DashboardService {

	private static final Logger log = LoggerFactory.getLogger(DashboardServiceImpl.class);

	private final EnrollmentRepository enrollmentRepository;
	private final StudentRepository studentRepository;
	private final CourseRepository courseRepository;

	public DashboardServiceImpl(EnrollmentRepository enrollmentRepository,
			StudentRepository studentRepository,
			CourseRepository courseRepository) {
		this.enrollmentRepository = enrollmentRepository;
		this.studentRepository = studentRepository;
		this.courseRepository = courseRepository;
	}

	@Override
	public DashboardStatsDTO getDashboardStats() {
		log.info("Generating dashboard stats");

		// Basic counts
		long totalStudents = studentRepository.count();
		long totalCourses = courseRepository.count();
		String topPerformingCourse = getTopPerformingCourse();

		// Current month enrollments
		long studentsEnrolledThisMonth = getStudentsEnrolledThisMonth();

		DashboardStatsDTO stats = new DashboardStatsDTO();
		stats.setTotalStudents(totalStudents);
		stats.setTotalCourses(totalCourses);
		stats.setTopPerformingCourse(topPerformingCourse);
		stats.setStudentsEnrolledThisMonth(studentsEnrolledThisMonth);

		log.info("Dashboard stats generated: Students={}, Courses={}, TopCourse={}, Monthly={}",
				totalStudents, totalCourses, topPerformingCourse, studentsEnrolledThisMonth);

		return stats;
	}

	/** Get top performing course by enrollment count */
	private String getTopPerformingCourse() {
		try {
			List<Enrollment> allEnrollments = enrollmentRepository.findAll();

			if (allEnrollments.isEmpty()) {
				return "No Enrollments";
			}

			return allEnrollments.stream()
					.collect(Collectors.groupingBy(
						enr -> enr.getCourse().getCourseName(),
						Collectors.counting()
					))
					.entrySet().stream()
					.max(Map.Entry.comparingByValue())
					.map(Map.Entry::getKey)
					.orElse("N/A");

		} catch (Exception e) {
			log.error("Error getting top course", e);
			return "N/A";
		}
	}

	/** Get students enrolled this month */
	private long getStudentsEnrolledThisMonth() {
		try {
			YearMonth currentMonth = YearMonth.now();
			LocalDateTime startDate = currentMonth.atDay(1).atStartOfDay();
			LocalDateTime endDate = currentMonth.atEndOfMonth().atTime(LocalTime.MAX);

			// ✅ FIXED: Simple count instead of custom method
			return enrollmentRepository.findAll().stream()
		            .filter(enr -> {
		                LocalDateTime enrollDate = enr.getEnrolledDate(); // ✅ Direct mil jayega
		                return enrollDate != null &&
		                       !enrollDate.isBefore(startDate) &&
		                       !enrollDate.isAfter(endDate);
		            })
					.map(Enrollment::getStudent)
					.map(Students::getId)
					.distinct()
					.count();

		} catch (Exception e) {
			log.error("Error counting monthly enrollments", e);
			return 0;
		}
	}
}