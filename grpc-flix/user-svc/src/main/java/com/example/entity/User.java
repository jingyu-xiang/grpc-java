package com.example.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString
public class User {
  @Id
  private String loginId;
  private String name;
  private String genre;
}
