package com.cst438.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cst438.domain.FinalGradeDTO;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;

@Service
@ConditionalOnProperty(prefix = "gradebook", name = "service", havingValue = "rest")
@RestController
public class GradebookServiceREST implements GradebookService {
    @Autowired
    EnrollmentRepository enrollmentRepository;

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${gradebook.url}")
    private String gradebook_url;

    @Override
    public void enrollStudent(String student_email, String student_name, int course_id) {
        System.out.println("Start Message " + student_email + " " + course_id);

        EnrollmentDTO enrollmentDTO = new EnrollmentDTO(0, student_email, student_name, course_id);

        EnrollmentDTO response = restTemplate.postForObject(gradebook_url, enrollmentDTO, EnrollmentDTO.class);

        if (response != null) {
            System.out.println("Enrollment successful for student: " + student_email + " in course: " + course_id);
        } else {
            System.out.println("Enrollment failed for student: " + student_email + " in course: " + course_id);
        }
    }

    @PutMapping("/course/{course_id}")
    @Transactional
    public void updateCourseGrades(@RequestBody FinalGradeDTO[] grades, @PathVariable("course_id") int course_id) {
        System.out.println("Grades received " + grades.length);

        for (FinalGradeDTO grade : grades) {
            Enrollment enrollment = enrollmentRepository.findByStudentEmailAndCourseId(grade.studentEmail(), course_id);
            if (enrollment != null) {
                enrollment.setCourseGrade(grade.grade());
                enrollmentRepository.save(enrollment);
            } else {
                System.out.println("Enrollment record not found for student: " + grade.studentEmail() + " in course: " + course_id);
            }
        }
    }
}
