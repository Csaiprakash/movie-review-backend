package com.example.moviereview.Repository;
import com.example.moviereview.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface ReviewRepository extends JpaRepository<Review,Long>{
    List<Review> findByMovieMovieId(Long movieId);
    Optional<Review> findByMovieMovieIdAndUserUsername(Long movieId, String username);
}
