package com.example.moviereview.Controller;

import com.example.moviereview.Entity.Review;
import com.example.moviereview.Service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.*;
import org.springframework.security.core.Authentication;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@SecurityRequirement(name = "Bearer Authentication")
@Validated
@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService service;

    @PostMapping("/add")
    public Review addReview(@RequestParam Long movieId,@RequestParam String review,Authentication authentication){
        return service.addReview(movieId,review,authentication.getName());
    }

    @GetMapping("/movie/{movieId}/user/{username}")
    public ResponseEntity<Review> getUserReview(@PathVariable Long movieId, @PathVariable String username) {

        return ResponseEntity.ok(service.getUserReview(movieId, username));
    }

    @PutMapping("/{reviewId}")
    public Review updateReview(
            @PathVariable Long reviewId,
            @RequestParam String reviewText,
            Authentication authentication) {

        return service.updateReview(
                reviewId,
                reviewText,
                authentication.getName());
    }

    @DeleteMapping("/{reviewId}")
    public String deleteReview(
            @PathVariable Long reviewId,
            Authentication authentication) {

        service.deleteReview(
                reviewId,
                authentication.getName());

        return "Review deleted successfully";
    }
}
