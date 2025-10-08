package com.gerop.stockcontrol.jewelry.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.gerop.stockcontrol.jewelry.model.entity.User;

@Repository
public interface UserRepository {
    public boolean existsByUsername(String username);

    public Optional<User> findByUsername(String username);

}
