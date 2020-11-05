package com.neighbourhood.online.neighbourhood.services;

import com.neighbourhood.online.neighbourhood.models.FileObject;
import com.neighbourhood.online.neighbourhood.models.NeighbourHood;
import com.neighbourhood.online.neighbourhood.models.User;
import com.neighbourhood.online.neighbourhood.payloads.ApiResponse;
import com.neighbourhood.online.neighbourhood.payloads.RegistrationRequest;
import com.neighbourhood.online.neighbourhood.payloads.UserLoginRequest;
import com.neighbourhood.online.neighbourhood.repositories.FileObjectRepository;
import com.neighbourhood.online.neighbourhood.repositories.NeighbourHoodRepository;
import com.neighbourhood.online.neighbourhood.repositories.UserRepository;
import com.neighbourhood.online.neighbourhood.utils.Utility;
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
public class UserService{

    private final UserRepository userRepository;
    private final NeighbourHoodRepository neighbourHoodRepository;
    private final FileObjectRepository fileObjectRepository;

    @Autowired
    public UserService(UserRepository userRepository, NeighbourHoodRepository neighbourHoodRepository
            , FileObjectRepository fileObjectRepository) {
        this.userRepository = userRepository;
        this.neighbourHoodRepository = neighbourHoodRepository;
        this.fileObjectRepository = fileObjectRepository;
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

        if (!encodedPassword.equals(optionalUser.get().getPassword()))
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

    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    public ResponseEntity<?> findUser(String userId) {
        if (userRepository.existsById(userId))
            return ResponseEntity.ok( new ApiResponse(false,203,
                    "User does not exists"));
        return ResponseEntity.ok(userRepository.findById(userId));
    }

    public ResponseEntity<?> updateProfilePicture(MultipartFile profilePicture, String userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent())
            return ResponseEntity.badRequest().body("User Not Found");

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
        optionalUser.get().setProfilePictureFileUuid(savedFile.getFileId());
        userRepository.save(optionalUser.get());

        return ResponseEntity.ok(new ApiResponse(true,200,"Profile Picture Successfully Set"));
    }

    public ResponseEntity<?> getProfilePicture(String userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (!optionalUser.isPresent())
            return ResponseEntity.ok(new ApiResponse(false,201,"User Not Found"));

        Optional<FileObject> optionalFileObject = fileObjectRepository.findById(
                optionalUser.get().getProfilePictureFileUuid());

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