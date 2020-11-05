package com.neighbourhood.online.neighbourhood.services;

import com.neighbourhood.online.neighbourhood.interfaces.NeighbourHoodFunctions;
import com.neighbourhood.online.neighbourhood.models.NeighbourHood;
import com.neighbourhood.online.neighbourhood.payloads.ApiResponse;
import com.neighbourhood.online.neighbourhood.repositories.NeighbourHoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NeighbourHoodService implements NeighbourHoodFunctions {

    private final NeighbourHoodRepository neighbourHoodRepository;

    @Autowired
    public NeighbourHoodService(NeighbourHoodRepository neighbourHoodRepository) {
        this.neighbourHoodRepository = neighbourHoodRepository;
    }

    @Override
    public ResponseEntity<?> createNeigbourhood(NeighbourHood neighbourHood) {
        if (neighbourHoodRepository.existsByNeighbourHoodName(neighbourHood.getNeighbourHoodName()))
            return ResponseEntity.ok( new ApiResponse(false,203,
                    "Neighbourhood Name Already exists"));

        return ResponseEntity.ok(neighbourHoodRepository.save(neighbourHood));
    }

    @Override
    public ResponseEntity<?> deleteNeigbourhood(NeighbourHood neighbourHood) {
        if (neighbourHoodRepository.existsById(neighbourHood.getNeighbourHoodId()))
            return ResponseEntity.ok( new ApiResponse(false,203,
                    "Neighbourhood does not exists"));

        neighbourHoodRepository.deleteById(neighbourHood.getNeighbourHoodId());

        return ResponseEntity.ok( new ApiResponse(true,200,
                "Neighbourhood deletes successfully"));
    }

    @Override
    public ResponseEntity<?> findNeigbourhood(String neighbourHoodId) {
        if (neighbourHoodRepository.existsById(neighbourHoodId))
            return ResponseEntity.ok( new ApiResponse(false,203,
                    "Neighbourhood does not exists"));
        return ResponseEntity.ok(neighbourHoodRepository.findById(neighbourHoodId));
    }

    @Override
    public ResponseEntity<?> updateNeighbourhood(NeighbourHood neighbourHood) {
        if (neighbourHoodRepository.existsById(neighbourHood.getNeighbourHoodId()))
            return ResponseEntity.ok( new ApiResponse(false,203,
                    "Neighbourhood does not exists"));
        return ResponseEntity.ok(neighbourHoodRepository.save(neighbourHood));
    }

    @Override
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public ResponseEntity<?> updateOccupants(NeighbourHood neighbourHood) {
        if (neighbourHoodRepository.existsById(neighbourHood.getNeighbourHoodId()))
            return ResponseEntity.ok( new ApiResponse(false,203,
                    "Neighbourhood does not exists"));

        Optional<NeighbourHood> optionalNeighbourHood = neighbourHoodRepository
                .findById(neighbourHood.getNeighbourHoodId());

        optionalNeighbourHood.get().setOccupantCount(neighbourHood.getOccupantCount());

        return ResponseEntity.ok(neighbourHoodRepository.save(neighbourHood));
    }

    @Override
    public ResponseEntity<?> getAllNeighbourHoods() {
    return ResponseEntity.ok(neighbourHoodRepository.findAll());
}
}
