package com.cst438;

import com.cst438.controller.StudentController;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JunitTestStudent {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    public void setUp() {
        // Clear the database before each test
        studentRepository.deleteAll();
    }

    @Test
    public void testAddStudent() throws Exception {
        Student student = new Student();
        student.setName("Felix Flores");
        student.setEmail("felix@example.com");
        student.setStatusCode(0);
        student.setStatus("Active");

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders
                        .post("/api/students")
                        .content(asJsonString(student))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn().getResponse();

        Student savedStudent = fromJsonString(response.getContentAsString(), Student.class);
        assertNotNull(savedStudent.getStudent_id());

        Optional<Student> retrievedStudent = studentRepository.findById(savedStudent.getStudent_id());
        assertTrue(retrievedStudent.isPresent());
        assertEquals(student.getName(), retrievedStudent.get().getName());
        assertEquals(student.getEmail(), retrievedStudent.get().getEmail());
        assertEquals(student.getStatusCode(), retrievedStudent.get().getStatusCode());
        assertEquals(student.getStatus(), retrievedStudent.get().getStatus());
    }

    @Test
    public void testUpdateStudent() throws Exception {
        Student student = new Student();
        student.setName("James");
        student.setEmail("james@example.com");
        student.setStatusCode(0);
        student.setStatus("Active");

        Student savedStudent = studentRepository.save(student);

        // Update student information
        savedStudent.setName("Updated James");
        savedStudent.setEmail("james1@example.com");

        MockHttpServletResponse response = mvc.perform(
                MockMvcRequestBuilders
                        .put("/api/students/{id}", savedStudent.getStudent_id())
                        .content(asJsonString(savedStudent))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        Student updatedStudent = fromJsonString(response.getContentAsString(), Student.class);
        assertEquals(savedStudent.getStudent_id(), updatedStudent.getStudent_id());
        assertEquals("Updated James", updatedStudent.getName());
        assertEquals("james1@example.com", updatedStudent.getEmail());
    }

    @Test
    public void testDeleteStudent() throws Exception {
        Student student = new Student();
        student.setName("Lyn");
        student.setEmail("lyn@example.com");
        student.setStatusCode(0);
        student.setStatus("Active");

        Student savedStudent = studentRepository.save(student);

        mvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/students/{id}", savedStudent.getStudent_id()))
                .andExpect(status().isNoContent());

        Optional<Student> deletedStudent = studentRepository.findById(savedStudent.getStudent_id());
        assertFalse(deletedStudent.isPresent());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T fromJsonString(String str, Class<T> valueType) {
        try {
            return new ObjectMapper().readValue(str, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
