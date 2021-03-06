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
import com.depi.service.ThesisService;

@SpringBootTest
public class StudentTest extends AbstractTestNGSpringContextTests {
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(StudentTest.class);
	
	private Long studentGeneratedId;
	private Long thesisGeneratedId;
	private final String SUBSTITUTION_NAME = "Diana";
	private final String THESIS_TITLE = "Estudio de NFC y grupo de estudio";
	
	@Autowired
	private StudentService studentService;
	@Autowired
	private ThesisService thesisService;
	
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
		assertThat(students.size()).isGreaterThan(1);
	}
	
	@Test(priority = 3)
	public void updateStudentTest() {
		Student student = this.studentService.getStudentById(studentGeneratedId);
		student.setName(this.SUBSTITUTION_NAME);
		this.studentService.saveStudent(student);
		
		Student updatedStudent = this.studentService.getStudentById(studentGeneratedId);
		assertThat(updatedStudent.getName()).isEqualTo(this.SUBSTITUTION_NAME);
	}
	
	@Test(priority = 4)
	public void assignThesisToStudentTest() {
		Thesis thesis = new Thesis();
		thesis.setTitle(this.THESIS_TITLE);
		thesisGeneratedId = thesisService.saveThesis(thesis).getId();
		
		studentService.assignThesis(this.studentGeneratedId, this.thesisGeneratedId);
		
		assertThat(studentService.getStudentById(studentGeneratedId).getThesis().getTitle()).isEqualTo(this.THESIS_TITLE);
		assertThat(thesisService.getThesisById(thesisGeneratedId).getStudent().getId()).isEqualTo(this.studentGeneratedId);
	}
	
	@Test(priority = 5,
			expectedExceptions = { ResourceNotFoundException.class } )
	public void deleteStudentTest() {
		this.studentService.delete(this.studentGeneratedId);
		this.studentService.getStudentById(this.studentGeneratedId);
	}
}
