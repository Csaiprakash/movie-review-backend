package com.example.moviereview.Repository;
import com.example.moviereview.Entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public interface MovieRepository extends JpaRepository<Movie,Long> {
    Page<Movie> findByTitleContainingIgnoreCase(
            String title,
            Pageable pageable);
    Page<Movie> findByDirector(String director,Pageable pageable);
    Page<Movie> findByGenre(String genre,Pageable pageable);
    Page<Movie> findAll(Pageable pageable);
}
