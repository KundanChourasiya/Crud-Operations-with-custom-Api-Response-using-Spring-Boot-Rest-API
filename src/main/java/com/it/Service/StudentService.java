package com.it.Service;

import com.it.Payload.StudentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService {

    public StudentDto createStudent(StudentDto dto);

    public List<StudentDto> getAllStudent();

    public Page<StudentDto> getAllStudentPage(Pageable pageable);

    public StudentDto getStudent(Long id);

    public StudentDto updateStudent(Long id, StudentDto dto);

    public StudentDto deleteStudent(Long id);

    public List<StudentDto> searchByName(String name);

}
