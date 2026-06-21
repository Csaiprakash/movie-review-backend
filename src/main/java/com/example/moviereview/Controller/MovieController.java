package com.example.moviereview.Controller;

import lombok.RequiredArgsConstructor;
import com.example.moviereview.Entity.Movie;
import com.example.moviereview.Service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@SecurityRequirement(name = "Bearer Authentication")
@Validated
@RestController
@RequestMapping("/api")
public class MovieController {

    @Autowired
    private MovieService service;

    @PostMapping("/admin/createMovie")
    public ResponseEntity<Movie> addMovie(@Valid @RequestBody Movie movie){
        Movie newMovie=service.addMovie(movie);
        return ResponseEntity.status(201).body(newMovie);
    }

    @GetMapping("/public/movie/{movieId}")
    public ResponseEntity<Movie> getMovieById(
            @PathVariable Long movieId) {

        return ResponseEntity.ok(
                service.getMovieById(movieId)
        );
    }

    @GetMapping("/public/search")
    public ResponseEntity<Page<Movie>> searchMovies(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable =
                PageRequest.of(page, size);

        return ResponseEntity.ok(
                service.searchMovies(
                        title,
                        pageable
                )
        );
    }

    @GetMapping("/public/getMovies")
    public Page<Movie> getAllMovies(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="5") int size){
        return service.getAll(page,size);
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<Movie> update(@RequestBody Movie movie,@PathVariable Long id){
        Movie m=service.updateById(movie,id);
        return ResponseEntity.ok(m);
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.ok("Movie Successfully deleted");
    }

    @GetMapping("/public/getMovies/director")
    public Page<Movie> getAllMoviesByDirector(@RequestParam String director,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="5") int size){
        return service.getAllByDirector(director,page,size);
    }
    @GetMapping("/public/getMovies/genre")
    public Page<Movie> getAllMoviesByGenre(@RequestParam String genre,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="5") int size){
        return service.getAllByGenre(genre,page,size);
    }
}
