package com.neighbourhood.online.neighbourhood.repositories;

import com.neighbourhood.online.neighbourhood.models.NeighbourHood;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NeighbourHoodRepository extends CrudRepository<NeighbourHood,String> {
    boolean existsByNeighbourHoodName(String emailAddress);
}
