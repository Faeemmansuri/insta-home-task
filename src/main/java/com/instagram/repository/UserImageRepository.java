package com.instagram.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.instagram.entity.UserEntity;
import com.instagram.entity.UserImageEntity;

@Repository
public interface UserImageRepository extends BaseRepository<UserImageEntity, UUID> {
	
	List<UserImageEntity> findByCreatedBy(UserEntity user);
	
	Optional<UserImageEntity> findByCreatedByAndId(UserEntity user, UUID uuid);

}
