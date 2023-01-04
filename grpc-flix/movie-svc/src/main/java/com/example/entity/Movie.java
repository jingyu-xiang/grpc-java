package com.example.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@Entity @Table(name="ms_movie")
@ToString @Getter @Setter @RequiredArgsConstructor
public class Movie {
    @Id
    private Integer id;
    private String title;
    @Column(name="release_year")
    private Integer year;
    private Double rating;
    private String genre;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Movie movie = (Movie) o;
    return id != null && Objects.equals(id, movie.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
