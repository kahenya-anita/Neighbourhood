package com.neighbourhood.online.neighbourhood.interfaces;

import com.neighbourhood.online.neighbourhood.models.Business;
import com.neighbourhood.online.neighbourhood.payloads.BusinessRequest;
import org.springframework.http.ResponseEntity;

public interface BusinessFunctions {
    ResponseEntity<?> createBusiness(BusinessRequest business);
    ResponseEntity<?> deleteBusiness(Business business);
    ResponseEntity<?> findBusiness(String businessId);
    ResponseEntity<?> updateBusiness(BusinessRequest business);
    ResponseEntity<?> getAllBusinesses();
}