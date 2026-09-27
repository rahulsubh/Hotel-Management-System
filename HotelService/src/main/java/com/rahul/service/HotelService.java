package com.rahul.service;

import com.rahul.entities.Hotel;

import java.util.List;

public interface HotelService {
    Hotel createHotel(Hotel hotel);
    List<Hotel> getAllHotels();
    Hotel getHotelById(String hotelId);
    Hotel updateHotel(Hotel hotel, String hotelId);
    void deleteHotel(String hotelId);
}
