package com.neighbourhood.online.neighbourhood.services;

import com.neighbourhood.online.neighbourhood.models.NeighbourHood;
import com.neighbourhood.online.neighbourhood.models.User;
import com.neighbourhood.online.neighbourhood.payloads.ApiResponse;
import com.neighbourhood.online.neighbourhood.payloads.RegistrationRequest;
import com.neighbourhood.online.neighbourhood.payloads.UserLoginRequest;
import com.neighbourhood.online.neighbourhood.repositories.NeighbourHoodRepository;
import com.neighbourhood.online.neighbourhood.repositories.UserRepository;
import com.neighbourhood.online.neighbourhood.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService{

    private final UserRepository userRepository;
    private final NeighbourHoodRepository neighbourHoodRepository;

    @Autowired
    public UserService(UserRepository userRepository, NeighbourHoodRepository neighbourHoodRepository) {
        this.userRepository = userRepository;
        this.neighbourHoodRepository = neighbourHoodRepository;
    }

    public ResponseEntity<?> loginUser(UserLoginRequest userLoginRequest) {
        if (!userRepository.existsByEmailAddress(userLoginRequest.getEmailAddress()))
            return ResponseEntity.ok(new ApiResponse(false,201,"No user exist with the email Address:"
                    .concat(userLoginRequest.getEmailAddress())));

        Optional<User> optionalUser = userRepository.findByEmailAddress(userLoginRequest.getEmailAddress());

        if (!optionalUser.isPresent())
            return ResponseEntity.ok(new ApiResponse(false,201,"Failed to retrieve user with emailAddress:"
                    .concat(userLoginRequest.getEmailAddress())));

        String encodedPassword = Utility.encodePassword(userLoginRequest.getPassword());
        String decodedPassword = Utility.decodePassword(optionalUser.get().getPassword());

        if (!encodedPassword.equals(decodedPassword))
            return ResponseEntity.ok(new ApiResponse(false,202,"Wrong User Password" +
                    ", Confirm credentials"));

        return ResponseEntity.ok(optionalUser.get());
    }

    public ResponseEntity<?> registerUser(RegistrationRequest registrationRequest) {

        if (userRepository.existsByEmailAddress(registrationRequest.getEmailAddress()))
            return ResponseEntity.ok( new ApiResponse(false,203,"Email Address Already exists"));

        if (!neighbourHoodRepository.existsById(registrationRequest.getNeighbourHoodId()))
            return ResponseEntity.ok(new ApiResponse(false,201,"Neighbourhood with id:"
                    +registrationRequest.getNeighbourHoodId()+" Does not exist"));

        Optional<NeighbourHood> optionalNeighbourHood = neighbourHoodRepository.findById(registrationRequest
                .getNeighbourHoodId());

        if (!optionalNeighbourHood.isPresent())
            return ResponseEntity.ok(new ApiResponse(false,201,("Failed to retrieve Neighbourhood " +
                    "with emailAddress:")
                    .concat(registrationRequest.getNeighbourHoodId()+" Try Again")));

        User user = new User();
        user.setName(registrationRequest.getName());
        user.setEmailAddress(registrationRequest.getEmailAddress());
        user.setPassword(Utility.encodePassword(registrationRequest.getPassword()));
        user.setNeighbourHood(optionalNeighbourHood.get());

        return ResponseEntity.ok(userRepository.save(user));
    }
}