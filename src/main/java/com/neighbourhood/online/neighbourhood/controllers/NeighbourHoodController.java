package com.neighbourhood.online.neighbourhood.controllers;

import com.neighbourhood.online.neighbourhood.models.NeighbourHood;
import com.neighbourhood.online.neighbourhood.payloads.NeighbourHoodPictureRequest;
import com.neighbourhood.online.neighbourhood.services.NeighbourHoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("find")
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

    @GetMapping("list_all")
    public ResponseEntity<?> getAllNeighbourHoods(){
        return neighbourHoodService.getAllNeighbourHoods();
    }

    @GetMapping("get_neighbourhood_picture_list_uuids")
    public ResponseEntity<?> getNeighbourhoodPictureList(@RequestParam String neighbourhoodId){
        return neighbourHoodService.getNeighbourhoodPictureList(neighbourhoodId);
    }

    @PostMapping("add_neighbourhood_picture")
    public ResponseEntity<?> addNeighbourhoodPicture(@RequestParam("file") MultipartFile picture
            ,@RequestParam("neighbourhood_id") String neighbourhoodId){
        return neighbourHoodService.addNeighbourhoodPicture(picture,neighbourhoodId);
    }

    @PostMapping("get_neighbourhood_picture")
    public ResponseEntity<?> getNeighbourhoodPicture(
            @RequestBody NeighbourHoodPictureRequest neighbourHoodPictureRequest){
        return neighbourHoodService.getNeighbourhoodPicture(neighbourHoodPictureRequest);
    }
}
