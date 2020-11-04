package com.neighbourhood.online.neighbourhood.repositories;

import com.neighbourhood.online.neighbourhood.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    boolean existsByEmailAddress(String  emailAddress);
    Optional<User> findByEmailAddress(String emailAddress);
}