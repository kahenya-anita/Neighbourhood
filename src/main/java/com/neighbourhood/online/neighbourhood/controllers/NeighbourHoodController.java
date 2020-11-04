package com.neighbourhood.online.neighbourhood.controllers;

import com.neighbourhood.online.neighbourhood.models.NeighbourHood;
import com.neighbourhood.online.neighbourhood.services.NeighbourHoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/neighbourhood")
public class NeighbourHoodController {

    private final NeighbourHoodService neighbourHoodService;

    @Autowired
    public NeighbourHoodController(NeighbourHoodService neighbourHoodService) {
        this.neighbourHoodService = neighbourHoodService;
    }

    @PostMapping("create")
    public ResponseEntity<?> createNeighbourHood(@RequestBody NeighbourHood neighbourHoodRequest){
        return neighbourHoodService.createNeigbourhood(neighbourHoodRequest);
    }

    @PostMapping("delete")
    public ResponseEntity<?> deleteNeigborhood(@RequestBody NeighbourHood neighbourHoodRequest){
        return neighbourHoodService.deleteNeigbourhood(neighbourHoodRequest);
    }

    @PostMapping("find")
    public ResponseEntity<?> findNeigbourhood(@RequestParam String neighbourHoodId){
        return neighbourHoodService.findNeigbourhood(neighbourHoodId);
    }

    @PostMapping("update")
    public ResponseEntity<?> updateNeighbourhood(@RequestBody NeighbourHood neighbourHoodRequest){
        return neighbourHoodService.updateNeighbourhood(neighbourHoodRequest);
    }

    @PostMapping("update_occupant")
    public ResponseEntity<?> updateOccupants(@RequestBody NeighbourHood neighbourHood){
        return neighbourHoodService.updateOccupants(neighbourHood);
    }
}
