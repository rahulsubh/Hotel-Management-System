package com.rahul.service.impl;

import com.rahul.entities.User;
import com.rahul.exceptions.ResourceNotFoundException;
import com.rahul.payload.Hotel;
import com.rahul.payload.Rating;
import com.rahul.repository.UserRepository;
import com.rahul.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RestTemplate restTemplate;

    @Override
    public User createUser(User user) {
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> {
            //fetch rating from the above user
            Rating[] rating = restTemplate.getForObject(
                    "http://RATING-SERVICE/ratings/users/" + user.getUserId(),
                    Rating[].class);
            List<Rating> ratingList1 = Arrays.stream(rating).toList();
            List<Rating> ratingList = ratingList1.stream().map(ratingOfUser -> {
                //api call to hotel service to get the hotel
                ResponseEntity<Hotel> forEntity = restTemplate.getForEntity(
                        "http://HOTEL-SERVICE/hotels/" + ratingOfUser.getHotelId(),
                        Hotel.class
                );
                Hotel hotel = forEntity.getBody();
                ratingOfUser.setHotel(hotel);
                return ratingOfUser;
            }).toList();
            user.setRatings(ratingList);
            return user;
        }).toList();
    }

    @Override
    public User getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        //fetch rating from the above user
        Rating[] ratings = restTemplate.getForObject(
                "http://RATING-SERVICE/ratings/users/" + user.getUserId()
                , Rating[].class);
        List<Rating> ratingList1 = Arrays.stream(ratings).toList();
        log.info("Ratings for user {}: {}", user.getUserId(), ratings);
        List<Rating> ratingList = ratingList1.stream().map(rating -> {
            String hotelId = rating.getHotelId();
            //api call to hotel service to get the hotel
            ResponseEntity<Hotel> forEntity = restTemplate.getForEntity(
                    "http://HOTEL-SERVICE/hotels/" + hotelId,
                    Hotel.class
            );
            Hotel hotel = forEntity.getBody();
            rating.setHotel(hotel);
            return rating;
        }).toList();
        user.setRatings(ratingList);
        return user;
    }

    @Override
    public User updateUser(User user, String userId) {
        User user1 = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        user1.setUserName(user.getUserName());
        user1.setEmail(user.getEmail());
        user1.setAbout(user.getAbout());
        return userRepository.save(user1);
    }

    @Override
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}
