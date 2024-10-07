package com.bezkoder.springjwt.helper;

import com.bezkoder.springjwt.models.Student;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {
  public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
  static String[] HEADERs = { "StudentId", "FirstName", "LastName", "DOB", "Class", "Score" };
  static String SHEET = "students";

  public static boolean hasExcelFormat(MultipartFile file) {

    if (!TYPE.equals(file.getContentType())) {
      return false;
    }

    return true;
  }

  public static ByteArrayInputStream studentsToExcel(List<Student> students) {

    try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
      Sheet sheet = workbook.createSheet(SHEET);

      // Header
      Row headerRow = sheet.createRow(0);

      for (int col = 0; col < HEADERs.length; col++) {
        Cell cell = headerRow.createCell(col);
        cell.setCellValue(HEADERs[col]);
      }

      int rowIdx = 1;
      for (Student student : students) {
        Row row = sheet.createRow(rowIdx++);

        row.createCell(0).setCellValue(student.getStudentId());
        row.createCell(1).setCellValue(student.getFirstName());
        row.createCell(2).setCellValue(student.getLastName());
        row.createCell(3).setCellValue(student.getDob());
        row.createCell(4).setCellValue(student.getClassName());
        row.createCell(5).setCellValue(student.getScore());

      }

      workbook.write(out);
      return new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
    }
  }

  public static List<Student> excelTostudents(InputStream is) {
    try {
      Workbook workbook = new XSSFWorkbook(is);

      Sheet sheet = workbook.getSheetAt(0);
      Iterator<Row> rows = sheet.iterator();

      List<Student> students = new ArrayList<Student>();

      int rowNumber = 0;
      while (rows.hasNext()) {
        Row currentRow = rows.next();

        // skip header
        if (rowNumber == 0) {
          rowNumber++;
          continue;
        }

        Iterator<Cell> cellsInRow = currentRow.iterator();

        Student student = new Student();

        int cellIdx = 0;
        while (cellsInRow.hasNext()) {
          Cell currentCell = cellsInRow.next();

          switch (cellIdx) {
          case 0:
            student.setStudentId((long) currentCell.getNumericCellValue());
            break;

          case 1:
            student.setFirstName(currentCell.getStringCellValue());
            break;

          case 2:
            student.setLastName(currentCell.getStringCellValue());
            break;

          case 3:
            student.setDob(currentCell.getLocalDateTimeCellValue().toLocalDate());
            break;

          case 4:
              student.setClassName(currentCell.getStringCellValue());
              break;

          case 5:
              student.setScore((int) currentCell.getNumericCellValue());
              break;

          default:
            break;
          }

          cellIdx++;
        }

        students.add(student);
      }

      workbook.close();

      return students;
    } catch (IOException e) {
      throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
    }
  }
}
