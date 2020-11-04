package com.neighbourhood.online.neighbourhood.services;

import com.neighbourhood.online.neighbourhood.interfaces.BusinessFunctions;
import com.neighbourhood.online.neighbourhood.models.Business;
import com.neighbourhood.online.neighbourhood.models.NeighbourHood;
import com.neighbourhood.online.neighbourhood.models.User;
import com.neighbourhood.online.neighbourhood.payloads.ApiResponse;
import com.neighbourhood.online.neighbourhood.payloads.BusinessRequest;
import com.neighbourhood.online.neighbourhood.repositories.BusinessRepository;
import com.neighbourhood.online.neighbourhood.repositories.NeighbourHoodRepository;
import com.neighbourhood.online.neighbourhood.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BusinessService implements BusinessFunctions {

    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;
    private final NeighbourHoodRepository neighbourHoodRepository;

    @Autowired
    public BusinessService(BusinessRepository businessRepository
            , UserRepository userRepository, NeighbourHoodRepository neighbourHoodRepository) {
        this.businessRepository = businessRepository;
        this.userRepository = userRepository;
        this.neighbourHoodRepository = neighbourHoodRepository;
    }

    @Override
    public ResponseEntity<?> createBusiness(BusinessRequest businessRequest) {
        if (businessRepository.existsByBusinessName(businessRequest.getBusinessName()))
            return ResponseEntity.ok( new ApiResponse(false,203,
                    "Business Name Already exists"));

        if (!userRepository.existsById(businessRequest.getBusinessOwnerId()))
            return ResponseEntity.ok( new ApiResponse(false,203,
                    "User does not exist"));

        if (!neighbourHoodRepository.existsById(businessRequest.getNeighbourHoodId()))
            return ResponseEntity.ok( new ApiResponse(false,203,
                    "User does not exist"));

        Optional<User> optionalUser = userRepository.findById(businessRequest.getBusinessOwnerId());

        if (!optionalUser.isPresent())
            return ResponseEntity.ok(new ApiResponse(false,201
                    ,"Failed to retrieve Business Owner"));

        Optional<NeighbourHood> optionalNeighbourHood = neighbourHoodRepository
                .findById(businessRequest.getNeighbourHoodId());

        if (!optionalNeighbourHood.isPresent())
            return ResponseEntity.ok(new ApiResponse(false,201
                    ,"Failed to retrieve Neighbourhood"));

        Business business = new Business();
        business.setBusinessName(businessRequest.getBusinessName());
        business.setBusinessOwner(optionalUser.get());
        business.setNeighbourHood(optionalNeighbourHood.get());
        business.setEmailAddress(businessRequest.getEmailAddress());

        return ResponseEntity.ok(businessRepository.save(business));
    }

    @Override
    public ResponseEntity<?> deleteBusiness(Business business) {
        if (businessRepository.existsById(business.getNeighbourHoodBusinessId()))
            return ResponseEntity.ok( new ApiResponse(false,203,
                    "Business does not exists"));
        businessRepository.deleteById(business.getNeighbourHoodBusinessId());

        return ResponseEntity.ok( new ApiResponse(true,200,
                "Business deletes successfully"));
    }

    @Override
    public ResponseEntity<?> findBusiness(String businessId) {
        if (businessRepository.existsById(businessId))
            return ResponseEntity.ok( new ApiResponse(false,203,
                    "Business does not exists"));
        return ResponseEntity.ok(businessRepository.findById(businessId));
    }

    @Override
    public ResponseEntity<?> updateBusiness(BusinessRequest business) {
        return createBusiness(business);
    }
}