package com.bezkoder.springjwt.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "students")
public class Student {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "studentId")
  private Long studentId;

  @Column(name = "firstName")
  private String firstName;

  @Column(name = "lastName")
  private String lastName;

  @Column(name = "DOB", nullable = false)
  private LocalDate dob;  // Use LocalDate for date representation

  @Column(name = "class")
  private String className;  // Renamed to avoid confusion with the keyword 'class'

  @Column(name = "score")
  private int score;


}
