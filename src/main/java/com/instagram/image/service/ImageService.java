package com.instagram.image.service;

import java.util.List;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.instagram.response.ImageReponse;

public interface ImageService {

	public List<ImageReponse> listImages();
	
	public Resource getImage(UUID uuid);
	
	public ImageReponse uploadImage(MultipartFile file);
	
}
