package com.movie.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.dto.MovieDto;
import com.movie.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/add-movie")
    public ResponseEntity<MovieDto> createMovie(@RequestPart MultipartFile file, @RequestPart String movieDto) throws IOException {

        MovieDto newMovieDto = convertToMovieDto(movieDto);

        return new ResponseEntity<>(movieService.addMovie(newMovieDto, file), HttpStatus.CREATED);
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDto> getMovie(@PathVariable(name = "movieId") Integer movieId) {

        return new ResponseEntity<>(movieService.getMovie(movieId), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<MovieDto>> getAllMovies() {

        return new ResponseEntity<>(movieService.getAllMovies(), HttpStatus.OK);
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<String> deleteMovie(@PathVariable(name = "movieId") Integer movieId) {

        return new ResponseEntity<>(movieService.deleteMovie(movieId), HttpStatus.OK);
    }

    private MovieDto convertToMovieDto(String movieDtoObj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(movieDtoObj, MovieDto.class);
    }
}
