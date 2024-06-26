package com.movie.service;

import com.movie.dto.MovieDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MovieService {

    MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException;

    MovieDto getMovie(Integer movieId);

    List<MovieDto> getAllMovies();

    String deleteMovie(Integer movieId);

    MovieDto updateMovie(MovieDto movieDto, MultipartFile file, Integer movieId) throws IOException;
}
