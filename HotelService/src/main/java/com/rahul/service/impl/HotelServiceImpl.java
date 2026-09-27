package com.rahul.service.impl;

import com.rahul.Exception.ResourceNotFoundException;
import com.rahul.entities.Hotel;
import com.rahul.repository.HotelRepository;
import com.rahul.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    @Override
    public Hotel createHotel(Hotel hotel) {
        String hotelId = UUID.randomUUID().toString();
        hotel.setId(hotelId);
        return hotelRepository.save(hotel);
    }

    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @Override
    public Hotel getHotelById(String hotelId) {
        return hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel with given id is not found on server !! : " + hotelId));
    }

    @Override
    public Hotel updateHotel(Hotel hotel, String hotelId) {
        Hotel hotel1 = getHotelById(hotelId);
        hotel1.setName(hotel.getName());
        hotel1.setLocation(hotel.getLocation());
        hotel1.setAbout(hotel.getAbout());
        return hotelRepository.save(hotel1);
    }

    @Override
    public void deleteHotel(String hotelId) {
        hotelRepository.delete(getHotelById(hotelId));
    }
}
