package com.example.repository;

import com.example.entity.Movie;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

  List<Movie> getMovieByGenreOrderByYearDesc(String genre);

}
