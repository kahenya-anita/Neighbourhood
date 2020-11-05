package com.neighbourhood.online.neighbourhood.services;

import com.neighbourhood.online.neighbourhood.interfaces.NeighbourHoodFunctions;
import com.neighbourhood.online.neighbourhood.models.Business;
import com.neighbourhood.online.neighbourhood.models.FileObject;
import com.neighbourhood.online.neighbourhood.models.NeighbourHood;
import com.neighbourhood.online.neighbourhood.payloads.ApiResponse;
import com.neighbourhood.online.neighbourhood.payloads.NeighbourHoodPictureRequest;
import com.neighbourhood.online.neighbourhood.repositories.FileObjectRepository;
import com.neighbourhood.online.neighbourhood.repositories.NeighbourHoodRepository;
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
public class NeighbourHoodService implements NeighbourHoodFunctions {

    private final NeighbourHoodRepository neighbourHoodRepository;
    private final FileObjectRepository fileObjectRepository;

    @Autowired
    public NeighbourHoodService(NeighbourHoodRepository neighbourHoodRepository, FileObjectRepository fileObjectRepository) {
        this.neighbourHoodRepository = neighbourHoodRepository;
        this.fileObjectRepository = fileObjectRepository;
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

    public ResponseEntity<?> getNeighbourhoodPictureList(String neighbourhoodId) {

        Optional<NeighbourHood> optionalNeighbourHood = neighbourHoodRepository
                .findById(neighbourhoodId);

        if (!optionalNeighbourHood.isPresent())
            return ResponseEntity.ok( new ApiResponse(false,203,
                    "Neighbourhood does not exists"));

        return ResponseEntity.ok(optionalNeighbourHood.get().getNeighbourHoodPicsUuids());
    }

    @SuppressWarnings("DuplicatedCode")
    public ResponseEntity<?> addNeighbourhoodPicture(MultipartFile picture, String neighbourhoodId) {
        Optional<NeighbourHood> optionalNeighbourHood = neighbourHoodRepository.findById(neighbourhoodId);
        if (!optionalNeighbourHood.isPresent())
            return ResponseEntity.badRequest().body(" Neighbourhood Not Found");

        FileObject fileObject = new FileObject();
        try {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(picture.getOriginalFilename()));
            String fileType  = picture.getContentType();
            val bytes = picture.getBytes();
            fileObject.setFileData(bytes);
            fileObject.setFileName(fileName);
            fileObject.setFileType(fileType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FileObject savedFile = fileObjectRepository.save(fileObject);
        optionalNeighbourHood.get().getNeighbourHoodPicsUuids().add(savedFile.getFileId());
        neighbourHoodRepository.save(optionalNeighbourHood.get());

        return ResponseEntity.ok(new ApiResponse(true,200,"Neighbourhood Picture Successfully Added"));
    }

    @SuppressWarnings("DuplicatedCode")
    public ResponseEntity<?> getNeighbourhoodPicture(NeighbourHoodPictureRequest neighbourHoodPictureRequest) {

        Optional<NeighbourHood> optionalNeighbourHood = neighbourHoodRepository
                .findById(neighbourHoodPictureRequest.getNeighbourHoodId());

        if (!optionalNeighbourHood.isPresent())
            return ResponseEntity.ok(new ApiResponse(false,201,"Neighbourhood Not Found"));

        Optional<FileObject> optionalFileObject = fileObjectRepository.findById(
                neighbourHoodPictureRequest.getNeighbourHoodPicsUuid());

        if (!optionalFileObject.isPresent())
            return ResponseEntity.ok(new ApiResponse(false,201,"Picture Is Not Set"));

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
