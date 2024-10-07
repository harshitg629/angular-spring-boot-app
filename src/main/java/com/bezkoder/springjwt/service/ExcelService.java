package com.bezkoder.springjwt.service;


import com.bezkoder.springjwt.helper.ExcelHelper;
import com.bezkoder.springjwt.models.Student;
import com.bezkoder.springjwt.repository.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class ExcelService {

  @Autowired
  StudentsRepository repository;

  public void save(MultipartFile file) {
    try {
      List<Student> tutorials = ExcelHelper.excelTostudents(file.getInputStream());
      repository.saveAll(tutorials);
    } catch (IOException e) {
      throw new RuntimeException("fail to store excel data: " + e.getMessage());
    }
  }
  public Page<Student> getFilteredStudents(Integer studentId, String className, Integer startScore, Integer endScore, LocalDate startDate, LocalDate endDate, Pageable pageable) {
    return repository.findStudentsByFilters(studentId, className, startScore, endScore, startDate, endDate, pageable);
  }

  public ByteArrayInputStream load() {
    List<Student> tutorials = repository.findAll();

    ByteArrayInputStream in = ExcelHelper.studentsToExcel(tutorials);
    return in;
  }

  public List<Student> getAllTutorials() {
    return repository.findAll();
  }
}
