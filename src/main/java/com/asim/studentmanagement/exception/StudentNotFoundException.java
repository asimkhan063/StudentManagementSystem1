
// StudentNotFoundException.java
package com.asim.studentmanagement.exception;

public class StudentNotFoundException extends RuntimeException {
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public StudentNotFoundException(String message) {
        super(message);
    }
    public StudentNotFoundException(Long id) {
        super("Student not found with id: " + id);
    }
}
