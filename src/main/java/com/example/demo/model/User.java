package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 private String fullName;

 @Column(unique = true)
 private String email;

 private String password;

 private String role;

 public User() {}

 public User(Long id, String fullName, String email,
 String password, String role) {
  this.id = id;
  this.fullName = fullName;
  this.email = email;
  this.password = password;
  this.role = role;
 }

 // getters and setters
}
