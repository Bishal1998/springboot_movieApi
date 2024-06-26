package com.movie.service.impl;

import com.movie.dto.MovieDto;
import com.movie.entity.Movie;
import com.movie.repository.MovieRepository;
import com.movie.service.FileService;
import com.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final FileService fileService;

    public MovieServiceImpl(MovieRepository movieRepository, FileService fileService) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
    }

    @Value("${spring.project.poster}")
    private String path;

    @Value("${spring.baseUrl}")
    private String baseUrl;

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {

        // upload the file
        String uploadedFileName = fileService.uploadFile(path, file);

        // set the value of field "poster" as filename
        movieDto.setPoster(uploadedFileName);

        // map dto to movie object
        Movie movie = new Movie(
                movieDto.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );

        // save the movie object
        Movie savedMovie = movieRepository.save(movie);

        // generate posterUrl
        String posterUrl = baseUrl + "/file/" + uploadedFileName;

        // map movie object to movieDto and return it

        return new MovieDto(
                savedMovie.getMovieId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getStudio(),
                savedMovie.getMovieCast(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                posterUrl
        );
    }

    @Override
    public MovieDto getMovie(Integer movieId) {

        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Movie Not Found!"));

        String posterUrl = baseUrl + "/file/" + movie.getPoster();

        return new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );
    }

    @Override
    public List<MovieDto> getAllMovies() {

        List<Movie> movies = movieRepository.findAll();

       return movies.stream().map((movie) -> new MovieDto(movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                baseUrl + "/file/" + movie.getPoster())).toList();
    }
}
