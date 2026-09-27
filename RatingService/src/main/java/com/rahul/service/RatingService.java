package com.rahul.service;

import com.rahul.entities.Rating;

import java.util.List;

public interface RatingService {
    Rating createRating(Rating rating);
    List<Rating> getAllRatings();
    Rating getRatingById(String ratingId);
    List<Rating> getRatingsByUserId(String userId);
    List<Rating> getRatingsByHotelId(String hotelId);
    Rating updateRating(String ratingId, Rating rating);
    void deleteRating(String ratingId);
}
