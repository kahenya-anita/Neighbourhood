package com.neighbourhood.online.neighbourhood.interfaces;

import com.neighbourhood.online.neighbourhood.models.NeighbourHood;
import org.springframework.http.ResponseEntity;

public interface NeighbourHoodFunctions {
    ResponseEntity<?> createNeigbourhood(NeighbourHood neighbourHood);
    ResponseEntity<?> deleteNeigbourhood(NeighbourHood neighbourHood);
    ResponseEntity<?> findNeigbourhood(String neighbourHoodId);
    ResponseEntity<?>  updateNeighbourhood(NeighbourHood neighbourHood);
    ResponseEntity<?>  updateOccupants(NeighbourHood neighbourHood);
    ResponseEntity<?> getAllNeighbourHoods();
}
