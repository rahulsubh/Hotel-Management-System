package com.rahul.service.impl;

import com.rahul.entities.User;
import com.rahul.exceptions.ResourceNotFoundException;
import com.rahul.repository.UserRepository;
import com.rahul.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            ArrayList ratingOfUser = restTemplate.getForObject(
                    "http://localhost:8083/ratings/users/" + user.getUserId(),
                    ArrayList.class);
            user.setRatings(ratingOfUser);
            return user;
        }).toList();
    }

    @Override
    public User getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        //fetch rating from the above user
        ArrayList ratings = restTemplate.getForObject(
                "http://localhost:8083/ratings/users/" + user.getUserId()
                , ArrayList.class);
        log.info("Ratings for user {}: {}", user.getUserId(), ratings);
        user.setRatings(ratings);
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
