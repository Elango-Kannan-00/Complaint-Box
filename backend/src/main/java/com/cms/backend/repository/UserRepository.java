package com.cms.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cms.backend.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> { }
