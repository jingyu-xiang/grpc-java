package com.example.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovieVo {

  private String title;
  private int year;
  private double rating;

}
