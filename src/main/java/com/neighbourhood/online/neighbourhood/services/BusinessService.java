package com.neighbourhood.online.neighbourhood.services;

import com.neighbourhood.online.neighbourhood.interfaces.BusinessFunctions;
import com.neighbourhood.online.neighbourhood.models.Business;
import com.neighbourhood.online.neighbourhood.models.FileObject;
import com.neighbourhood.online.neighbourhood.models.NeighbourHood;
import com.neighbourhood.online.neighbourhood.models.User;
import com.neighbourhood.online.neighbourhood.payloads.ApiResponse;
import com.neighbourhood.online.neighbourhood.payloads.BusinessRequest;
import com.neighbourhood.online.neighbourhood.repositories.BusinessRepository;
import com.neighbourhood.online.neighbourhood.repositories.FileObjectRepository;
import com.neighbourhood.online.neighbourhood.repositories.NeighbourHoodRepository;
import com.neighbourhood.online.neighbourhood.repositories.UserRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
public class BusinessService implements BusinessFunctions {

    private final BusinessRepository businessRepository;
    private final UserRepository userRepository;
    private final NeighbourHoodRepository neighbourHoodRepository;
    private final FileObjectRepository fileObjectRepository;

    @Autowired
    public BusinessService(BusinessRepository businessRepository
            , UserRepository userRepository, NeighbourHoodRepository neighbourHoodRepository, FileObjectRepository fileObjectRepository) {
        this.businessRepository = businessRepository;
        this.userRepository = userRepository;
        this.neighbourHoodRepository = neighbourHoodRepository;
        this.fileObjectRepository = fileObjectRepository;
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

    @Override
    public ResponseEntity<?> getAllBusinesses() {
        return ResponseEntity.ok(businessRepository.findAll());
    }

    @SuppressWarnings("DuplicatedCode")
    public ResponseEntity<?> updateProfilePicture(MultipartFile profilePicture, String businessId) {
        Optional<Business> optionalBusiness = businessRepository.findById(businessId);
        if (!optionalBusiness.isPresent())
            return ResponseEntity.badRequest().body("BUsiness Not Found");

        FileObject fileObject = new FileObject();
        try {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(profilePicture.getOriginalFilename()));
            String fileType  = profilePicture.getContentType();
            val bytes = profilePicture.getBytes();
            fileObject.setFileData(bytes);
            fileObject.setFileName(fileName);
            fileObject.setFileType(fileType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FileObject savedFile = fileObjectRepository.save(fileObject);
        optionalBusiness.get().setProfilePictureFileUuid(savedFile.getFileId());
        businessRepository.save(optionalBusiness.get());

        return ResponseEntity.ok(new ApiResponse(true,200,"Profile Picture Successfully Set"));
    }

    @SuppressWarnings("DuplicatedCode")
    public ResponseEntity<?> getProfilePicture(String businessId) {

        Optional<Business> optionalBusiness = businessRepository.findById(businessId);

        if (!optionalBusiness.isPresent())
            return ResponseEntity.ok(new ApiResponse(false,201,"Business Not Found"));

        Optional<FileObject> optionalFileObject = fileObjectRepository.findById(
                optionalBusiness.get().getProfilePictureFileUuid());

        if (!optionalFileObject.isPresent())
            return ResponseEntity.ok(new ApiResponse(false,201,"Profile Picture Is Not Set"));

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="
                .concat(optionalFileObject.get().getFileName()));
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        MultipartFile multipartFile = new BASE64DecodedMultipartFile(optionalFileObject.get().getFileData());
        ByteArrayResource byteArrayResource = null;
        try { byteArrayResource = new ByteArrayResource(multipartFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(multipartFile.getSize())
                .contentType(MediaType.parseMediaType(optionalFileObject.get().getFileType()))
                .body(byteArrayResource);
    }
}