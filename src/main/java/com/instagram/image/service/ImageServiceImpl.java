package com.instagram.image.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.instagram.entity.UserEntity;
import com.instagram.entity.UserImageEntity;
import com.instagram.exception.BadRequestException;
import com.instagram.file.service.FileService;
import com.instagram.repository.UserImageRepository;
import com.instagram.response.ImageReponse;

@Service
public class ImageServiceImpl implements ImageService {
	
	@Autowired UserImageRepository userImageRepository;

	@Autowired FileService fileService;

	private static final List<String> contentTypes = Arrays.asList("image/png", "image/jpeg", "image/jpg", "image/gif");

	
	@Override
	public List<ImageReponse> listImages() {
		UserEntity user = (UserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<UserImageEntity> images = userImageRepository.findByCreatedBy(user);
		List<ImageReponse> imageList = images.stream().map(e -> {
			return new ImageReponse(e.getId(), e.getImageName(), 
					generateImageDownloadPath(e.getId().toString()), e.getCreatedOn());
		}).collect(Collectors.toList());
		return imageList;
	}

	@Override
	public Resource getImage(UUID uuid) {
		UserEntity user = (UserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<UserImageEntity> userImageOptional = userImageRepository.findByCreatedByAndId(user, uuid);
		
		userImageOptional.orElseThrow(() -> new BadRequestException("Image Not Found"));
		
		UserImageEntity userImageEntity = userImageOptional.get();
		
		return fileService.loadFile(userImageEntity.getImagePath());
	}

	@Override
	public ImageReponse uploadImage(MultipartFile file) {
		if(file.isEmpty()) {
			throw new BadRequestException("Image is required.");
		}
		
		if(!contentTypes.contains(file.getContentType())) {
			throw new BadRequestException("Invalid File Format. Allowed formats are jpeg, gif, png.");
		}
		
		UserEntity user = (UserEntity)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		UserImageEntity entity = new UserImageEntity();
		entity.setImageName(file.getOriginalFilename());
		entity.setCreatedBy(user);
		entity.setFileExt(getFileExtension(file.getOriginalFilename()));
		entity = userImageRepository.save(entity);
		
		String destinationPath = fileService.saveFile(file, entity.getId().toString() + "." + entity.getFileExt());
		entity.setImagePath(destinationPath);
		userImageRepository.save(entity);
		
		return new ImageReponse(entity.getId(), 
				entity.getImageName(),
				generateImageDownloadPath(entity.getId().toString()), 
				entity.getCreatedOn());
	}
	
	private String generateImageDownloadPath(String filePath) {
		return ServletUriComponentsBuilder
				.fromCurrentContextPath()
				.path("/images/")
				.path(filePath).toUriString();
	}
	
	private String getFileExtension(String fileName) {
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		else
			return "";
	}
	
	
}
