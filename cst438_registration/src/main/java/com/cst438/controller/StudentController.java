package com.cst438.controller;

import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    // List all students
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = (List<Student>) studentRepository.findAll();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    // Add a new student
    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        // Check if the student's email is unique
        Student existingStudent = studentRepository.findByEmail(student.getEmail());
        if (existingStudent != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Student savedStudent = studentRepository.save(student);
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }

    // Update student information
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable int id,
            @RequestBody Student updatedStudent) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Student existingStudent = optionalStudent.get();

        // Check if the updated email is unique
        Student existingStudentByEmail = studentRepository.findByEmail(updatedStudent.getEmail());
        if (existingStudentByEmail != null && existingStudentByEmail.getStudent_id() != id) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Update student details
        existingStudent.setName(updatedStudent.getName());
        existingStudent.setEmail(updatedStudent.getEmail());
        existingStudent.setStatusCode(updatedStudent.getStatusCode());
        existingStudent.setStatus(updatedStudent.getStatus());

        Student savedStudent = studentRepository.save(existingStudent);
        return new ResponseEntity<>(savedStudent, HttpStatus.OK);
    }

    // Delete a student
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable int id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Student student = optionalStudent.get();
        studentRepository.delete(student);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
