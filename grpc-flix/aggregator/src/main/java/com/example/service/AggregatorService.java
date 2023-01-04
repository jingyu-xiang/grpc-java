package com.example.service;

import com.example.model.common.Genre;
import com.example.model.movie.MovieSearchRequest;
import com.example.model.movie.MovieSearchResponse;
import com.example.model.movie.MovieServiceGrpc.MovieServiceBlockingStub;
import com.example.model.user.UserGenreUpdateRequest;
import com.example.model.user.UserResponse;
import com.example.model.user.UserSearchRequest;
import com.example.model.user.UserServiceGrpc.UserServiceBlockingStub;
import com.example.vo.MovieVo;
import com.example.vo.UserGenreVo;
import java.util.List;
import java.util.stream.Collectors;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class AggregatorService {

  @GrpcClient("movie-service")
  private UserServiceBlockingStub userStub;

  @GrpcClient("user-service")
  private MovieServiceBlockingStub movieStub;

  public List<MovieVo> getUserMovies(String loginId) {
    final UserSearchRequest userSearchRequest = UserSearchRequest.newBuilder()
        .setLoginId(loginId)
        .build();
    final UserResponse userResponse = userStub.getUserGenre(userSearchRequest);

    final MovieSearchRequest movieSearchRequest = MovieSearchRequest.newBuilder()
        .setGenre(userResponse.getGenre())
        .build();
    final MovieSearchResponse movieSearchResponse = movieStub.getMovies(movieSearchRequest);

    return movieSearchResponse.getMoviesList()
        .stream()
        .map(movieDto -> new MovieVo(movieDto.getTitle(), movieDto.getYear(), movieDto.getRating()))
        .collect(Collectors.toList());
  }

  public void updateUserGenre(UserGenreVo userGenreVo) {
    final UserGenreUpdateRequest userGenreUpdateRequest = UserGenreUpdateRequest.newBuilder()
        .setLoginId(userGenreVo.getLoginId())
        .setGenre(Genre.valueOf(userGenreVo.getGenre().toUpperCase()))
        .build();

    final UserResponse userResponse = userStub.updateUserGenre(userGenreUpdateRequest);
    System.out.println(userResponse);
  }
}
