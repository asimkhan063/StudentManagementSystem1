package com.asim.studentmanagement.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asim.studentmanagement.dto.StudentDTO;
import com.asim.studentmanagement.model.Students;
import com.asim.studentmanagement.repository.StudentRepository;
import com.asim.studentmanagement.service.StudentService;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {  // ✅ Interface match karta hai

    private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;
    private final ModelMapper mapper;

    public StudentServiceImpl(StudentRepository studentRepository, ModelMapper mapper) {
        this.studentRepository = studentRepository;
        this.mapper = mapper;
    }

    // ✅ 1. CREATE - Interface se match
    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        log.info("Creating student: {}", studentDTO.getEmail());
        Students student = mapper.map(studentDTO, Students.class);
        student.setActive(true); // Default active
        Students saved = studentRepository.save(student);
        return mapper.map(saved, StudentDTO.class);
    }

    // ✅ 2. LIST with Pagination
    @Override
    public Page<StudentDTO> getStudents(int page, int size) {
        log.info("Fetching students page: {}, size: {}", page, size);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return studentRepository.findByActiveTrue(pageRequest)
                .map(student -> mapper.map(student, StudentDTO.class));
    }

    // ✅ 3. GET BY ID
    @Override
    @Transactional(readOnly = true)
    public StudentDTO getStudentById(Long id) {
        log.info("Fetching student by ID: {}", id);
        Students student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        return mapper.map(student, StudentDTO.class);
    }

    // ✅ 4. UPDATE
    @Override
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        log.info("Updating student ID: {}", id);
        Students student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        mapper.map(studentDTO, student);
        Students updated = studentRepository.save(student);
        return mapper.map(updated, StudentDTO.class);
    }

    // ✅ 5. EMAIL VALIDATION
    @Override
    public boolean existsByEmailIgnoreCase(String email) {
        log.info("Checking email exists: {}", email);
        return studentRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id) {
        log.info("Checking email exists for update: {}, id: {}", email, id);
        return studentRepository.existsByEmailIgnoreCaseAndIdNot(email, id);
    }

    // ✅ 6. ALL STUDENTS (for dropdown)
    @Override
    public List<StudentDTO> getAllStudents() {
        log.info("Fetching all active students");
        return studentRepository.findByActiveTrue().stream()
                .map(student -> mapper.map(student, StudentDTO.class))
                .collect(Collectors.toList());
    }

    // ✅ 7. FIND BY ID (for EnrollmentService)
    @Override
    public java.util.Optional<Students> findById(Long id) {
        return studentRepository.findById(id);
    }
}
/**package com.asim.studentmanagement.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asim.studentmanagement.dto.StudentDTO;
import com.asim.studentmanagement.model.Students;
import com.asim.studentmanagement.repository.StudentRepository;
import com.asim.studentmanagement.service.StudentService;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

	private static final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

	private final StudentRepository studentRepository;
	private final ModelMapper mapper;

	public StudentServiceImpl(StudentRepository studentRepository, ModelMapper mapper) {
		this.studentRepository = studentRepository;
		this.mapper = mapper;
	}

	@Override
	public boolean existsByEmailIgnoreCase(String email) {
		log.info("email from create student");
		return studentRepository.existsByEmailIgnoreCase(email);
	}

	@Override
	public StudentDTO createStudent(StudentDTO studentDTO) {
		log.info("saving student data");
		Students students = mapper.map(studentDTO, Students.class);
		students.setActive(true); // Default active
		Students saved = studentRepository.save(students);
		return mapper.map(saved, StudentDTO.class);
	}

	@Override
	public Page<StudentDTO> getStudents(int page, int size) {
		log.info("list of student from: {}", page);
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Direction.DESC, "id"));
		return studentRepository.findByActiveTrue(pageRequest)
				.map(student -> mapper.map(student, StudentDTO.class));
	}

	@Override
	@Transactional(readOnly = true)
	public StudentDTO getStudentById(Long id) {
		Students student = studentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("No student found"));
		return mapper.map(student, StudentDTO.class);
	}

	@Override
	public boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id) {
		log.info("email from update student");
		return studentRepository.existsByEmailIgnoreCaseAndIdNot(email, id);
	}

	@Override
	public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
		Students student = studentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("No student found"));
		mapper.map(studentDTO, student);
		Students updated = studentRepository.save(student);
		return mapper.map(updated, StudentDTO.class);
	}

	@Override
	public List<StudentDTO> getAllStudents() {
		return studentRepository.findByActiveTrue().stream()
				.map(student -> mapper.map(student, StudentDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public void deleteStudent(Long id) {
		Students student = studentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Student not found"));
		student.setActive(false);
		studentRepository.save(student);
	}

	@Override
	public List<StudentDTO> searchStudents(String keyword) {
		return studentRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
				keyword, keyword, keyword).stream()
				.filter(Students::isActive)
				.map(student -> mapper.map(student, StudentDTO.class))
				.collect(Collectors.toList());
	}
}*/