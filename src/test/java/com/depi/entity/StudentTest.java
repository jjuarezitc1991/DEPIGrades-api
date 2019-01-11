package com.depi.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.depi.exception.ResourceNotFoundException;
import com.depi.service.StudentService;

@SpringBootTest
public class StudentTest extends AbstractTestNGSpringContextTests {
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(StudentTest.class);
	
	private Long studentGeneratedId;
	
	@Autowired
	private StudentService studentService;
	
	@Test(priority = 1)
	public void createStudentTest() {
		Student student = new Student();
		student.setStudentNumber("A937874");
		student.setName("Nelly");
		student.setLastName("Garcia");
		
		this.studentService.saveStudent(student);
		this.studentGeneratedId = student.getId();
		
		assertThat(student.getId()).isNotNull();
	}
	
	@Test(priority = 2)
	public void listStudentsTest() {
		List<Student> students = this.studentService.getAllStudents();
		assertThat(students.size()).isGreaterThan(2);
	}
	
	@Test(priority = 3,
			expectedExceptions = { ResourceNotFoundException.class } )
	public void deleteStudentTest() {
		this.studentService.delete(this.studentGeneratedId);
		this.studentService.getStudentById(this.studentGeneratedId);
	}
}