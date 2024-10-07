package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.helper.ExcelHelper;
import com.bezkoder.springjwt.message.ResponseMessage;
import com.bezkoder.springjwt.models.Student;
import com.bezkoder.springjwt.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/excel")
public class ExcelController {

  @Autowired
  ExcelService fileService;

  @PostMapping("/upload")
  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
    String message = "";

    if (ExcelHelper.hasExcelFormat(file)) {
      try {
        fileService.save(file);

        message = "Uploaded the file successfully: " + file.getOriginalFilename();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
      } catch (Exception e) {
        message = "Could not upload the file: " + file.getOriginalFilename() + "!";
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
      }
    }

    message = "Please upload an excel file!";
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
  }

  @GetMapping("/students")
  public ResponseEntity<List<Student>> getAllTutorials() {
    try {
      List<Student> tutorials = fileService.getAllTutorials();

      if (tutorials.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(tutorials, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/download")
  public ResponseEntity<Resource> getFile() {
    String filename = "students.xlsx";
    InputStreamResource file = new InputStreamResource(fileService.load());

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
        .body(file);
  }

  @GetMapping("/studentsExport")
  public Page<Student> getStudentsReport(
          @RequestParam(required = false) Integer studentId,
          @RequestParam(required = false) String className,
          @RequestParam(required = false) Integer startScore,
          @RequestParam(required = false) Integer endScore,
          @RequestParam(required = false) String startDate,
          @RequestParam(required = false) String endDate,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int size) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate startDateParsed = (startDate != null) ? LocalDate.parse(startDate, formatter) : null;
    LocalDate endDateParsed = (endDate != null) ? LocalDate.parse(endDate, formatter) : null;

    Pageable pageable = PageRequest.of(page, size);
    return fileService.getFilteredStudents(studentId, className, startScore, endScore, startDateParsed, endDateParsed, pageable);
  }


}
