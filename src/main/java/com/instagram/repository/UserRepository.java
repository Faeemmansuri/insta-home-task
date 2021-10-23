package com.instagram.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.instagram.entity.UserEntity;

@Repository
public interface UserRepository extends BaseRepository<UserEntity, UUID> {
	
	Optional<UserEntity> findByEmail(String email);
	
}