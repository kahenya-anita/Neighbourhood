package com.neighbourhood.online.neighbourhood.repositories;

import com.neighbourhood.online.neighbourhood.models.Business;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRepository extends CrudRepository<Business,String> {
    boolean existsByBusinessName(String businessName);
}
