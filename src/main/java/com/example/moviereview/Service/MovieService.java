package com.example.moviereview.Service;

import com.example.moviereview.Entity.Movie;
import com.example.moviereview.Repository.MovieRepository;
import com.example.moviereview.exception.MovieNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    @Autowired
    private MovieRepository repo;

    public Movie addMovie(Movie movie) {
        return repo.save(movie);
    }

    public Page<Movie> searchMovies(
            String title,
            Pageable pageable) {

        return repo
                .findByTitleContainingIgnoreCase(
                        title,
                        pageable
                );
    }
    public Movie getMovieById(Long movieId) {

        return repo
                .findById(movieId)
                .orElseThrow(() ->
                        new RuntimeException("Movie not found"));
    }

    public Page<Movie> getAll(int page, int size) {

        Pageable pageable =
                PageRequest.of(page, size);

        return repo.findAll(pageable);
    }

    public Movie updateById(Movie movie, Long id) {

        Movie existingMovie =
                repo.findById(id)
                        .orElseThrow(() ->
                                new MovieNotFoundException(
                                        "Movie not found with id : " + id
                                ));

        existingMovie.setDescription(
                movie.getDescription());

        existingMovie.setDirector(
                movie.getDirector());

        return repo.save(existingMovie);
    }

    public void deleteById(Long id) {

        Movie movie =
                repo.findById(id)
                        .orElseThrow(() ->
                                new MovieNotFoundException(
                                        "Movie not found with id : " + id
                                ));

        repo.delete(movie);
    }

    public Page<Movie> getAllByDirector(
            String director,
            int page,
            int size) {

        Pageable pageable =
                PageRequest.of(page, size);

        Page<Movie> movies =
                repo.findByDirector(
                        director,
                        pageable);

        if(movies.isEmpty()) {
            throw new MovieNotFoundException(
                    "No movies found for director : "
                            + director
            );
        }

        return movies;
    }

    public Page<Movie> getAllByGenre(
            String genre,
            int page,
            int size) {

        Pageable pageable =
                PageRequest.of(page, size);

        Page<Movie> movies =
                repo.findByGenre(
                        genre,
                        pageable);

        if(movies.isEmpty()) {
            throw new MovieNotFoundException(
                    "No movies found for genre : "
                            + genre
            );
        }

        return movies;
    }
}