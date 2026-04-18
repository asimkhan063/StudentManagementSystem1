package com.asim.studentmanagement.dto;



import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/*
 * Copyright (c) 2026 Mahesh Shet
 * Licensed under the MIT License.
 */

public class EnrollmentDTO {

	@NotNull(message = "Student is required")
    private Long studentId;

    @NotEmpty(message = "Select at least one course")
    private List<Long> courseIds = new ArrayList<>();

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public List<Long> getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(List<Long> courseIds) {
        this.courseIds = courseIds;
    }

}
/**
package com.asim.studentmanagement.dto;

import java.util.List;

import lombok.Data;

@Data
public class EnrollmentDTO {
    private Long studentId;
    private List<Long> courseIds;
}*/