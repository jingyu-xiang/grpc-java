package com.example.service;

import com.example.model.movie.MovieDto;
import com.example.model.movie.MovieSearchRequest;
import com.example.model.movie.MovieSearchResponse;
import com.example.model.movie.MovieServiceGrpc.MovieServiceImplBase;
import com.example.repository.MovieRepository;
import io.grpc.stub.StreamObserver;
import java.util.List;
import java.util.stream.Collectors;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class MovieService extends MovieServiceImplBase {
  @Autowired
  private MovieRepository movieRepository;

  @Override
  public void getMovies(
      MovieSearchRequest request,
      StreamObserver<MovieSearchResponse> responseObserver
  ) {
    final List<MovieDto> movieDtoList = movieRepository.getMovieByGenreOrderByYearDesc(
            request.getGenre().toString()
        )
        .stream()
        .map(movie -> MovieDto.newBuilder()
            .setTitle(movie.getTitle())
            .setYear(movie.getYear())
            .setRating(movie.getRating())
            .build())
        .collect(Collectors.toList());

    final MovieSearchResponse movieSearchResponse = MovieSearchResponse.newBuilder()
        .addAllMovies(movieDtoList)
        .build();

    responseObserver.onNext(movieSearchResponse);
    responseObserver.onCompleted();
  }
}
