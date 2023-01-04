package com.example.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateGenreParam {

  private String loginId;

  private String genre;
}
