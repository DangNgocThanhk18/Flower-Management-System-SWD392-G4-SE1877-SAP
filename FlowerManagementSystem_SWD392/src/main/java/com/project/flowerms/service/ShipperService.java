package com.project.flowerms.service;

import com.project.flowerms.entity.User;
import com.project.flowerms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipperService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllShippers() {
        return userRepository.findAllShippers();
    }

    public void toggleActiveStatus(Integer userId) {
        User shipper = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Shipper not found"));

        shipper.setIsActive(shipper.getIsActive() == 1 ? 0 : 1);
        userRepository.save(shipper);
    }
}

