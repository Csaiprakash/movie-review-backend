package com.example.moviereview.Service;

import com.example.moviereview.Entity.Movie;
import com.example.moviereview.Entity.Review;
import com.example.moviereview.Entity.User;
import com.example.moviereview.Repository.MovieRepository;
import com.example.moviereview.Repository.ReviewRepository;
import com.example.moviereview.Repository.UserRepository;
import com.example.moviereview.exception.MovieNotFoundException;
import com.example.moviereview.exception.ReviewNotFoundException;
import com.example.moviereview.exception.UserNotAuthorizedException;
import com.example.moviereview.exception.UserNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository repo;

    @Autowired
    private UserRepository urepo;

    @Autowired
    private MovieRepository mrepo;

    public Review addReview(
            Long movieId,
            String reviewText,
            String username) {

        User user = urepo.findByUsername(username)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found : " + username));

        Movie movie = mrepo.findById(movieId)
                .orElseThrow(() ->
                        new MovieNotFoundException(
                                "Movie not found with id : " + movieId));

        Review review = new Review();

        review.setComment(reviewText);
        review.setUser(user);
        review.setMovie(movie);

        return repo.save(review);
    }

    public Review getUserReview(Long movieId, String username) {
        return repo.findByMovieMovieIdAndUserUsername(movieId, username).orElseThrow(() -> new RuntimeException("Review not found"));
    }

    public Review updateReview(
            Long reviewId,
            String reviewText,
            String username) {

        Review review = repo.findById(reviewId)
                .orElseThrow(() ->
                        new ReviewNotFoundException(
                                "Review not found with id : "
                                        + reviewId));

        if (!review.getUser()
                .getUsername()
                .equals(username)) {

            throw new UserNotAuthorizedException(
                    "You can update only your own review");
        }

        review.setComment(reviewText);

        return repo.save(review);
    }

    public void deleteReview(
            Long reviewId,
            String username) {

        Review review = repo.findById(reviewId)
                .orElseThrow(() ->
                        new ReviewNotFoundException(
                                "Review not found with id : "
                                        + reviewId));

        if (!review.getUser()
                .getUsername()
                .equals(username)) {

            throw new UserNotAuthorizedException(
                    "You can delete only your own review");
        }

        repo.delete(review);
    }
}