package com.neighbourhood.online.neighbourhood.controllers;

import com.neighbourhood.online.neighbourhood.models.Business;
import com.neighbourhood.online.neighbourhood.payloads.BusinessRequest;
import com.neighbourhood.online.neighbourhood.services.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/business")
public class BusinessController {

    private final BusinessService businessService;

    @Autowired
    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @PostMapping("create")
    public ResponseEntity<?> createBusines(@RequestBody BusinessRequest businesRequest){
        return businessService.createBusiness(businesRequest);
    }

    @PostMapping("delete")
    public ResponseEntity<?> deleteBusiness(@RequestBody Business businessRequest){
        return businessService.deleteBusiness(businessRequest);
    }

    @GetMapping("find")
    public ResponseEntity<?> findBusiness(@RequestParam String businessId){
        return businessService.findBusiness(businessId);
    }

    @PostMapping("update")
    public ResponseEntity<?> updateNeighbourhood(@RequestBody BusinessRequest businessRequest){
        return businessService.updateBusiness(businessRequest);
    }

    @GetMapping("list_all")
    public ResponseEntity<?> getAllBusinesses(){
        return businessService.getAllBusinesses();
    }

    @PostMapping("update_profile_picture")
    public ResponseEntity<?> updateProfilePicture(@RequestParam("file") MultipartFile profilePicture
            ,@RequestParam("user_id") String businessId){
        return businessService.updateProfilePicture(profilePicture,businessId);
    }

    @GetMapping("get_profile_picture")
    public ResponseEntity<?> getProfilePicture(@RequestParam String businessId){
        return businessService.getProfilePicture(businessId);
    }
}
