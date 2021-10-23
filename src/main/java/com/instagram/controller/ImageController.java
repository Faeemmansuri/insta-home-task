package com.instagram.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.instagram.image.service.ImageService;
import com.instagram.response.ImageReponse;

@RestController
@RequestMapping("/images")
public class ImageController {
	
	@Autowired ImageService imageService;

	@GetMapping("/{id}")
	public ResponseEntity<Resource> getImage(@PathVariable("id") String imageId) {
		Resource image = imageService.getImage(UUID.fromString(imageId));
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFilename() + "\"")
				.body(image);
	}
	
	@PostMapping
	public ResponseEntity<ImageReponse> uploadImage(@RequestParam(name = "file", required = true) MultipartFile file) {
		return ResponseEntity.ok(imageService.uploadImage(file));
	}
	
	@GetMapping
	public ResponseEntity<List<ImageReponse>> getImages() {
		return ResponseEntity.ok().body(imageService.listImages());
	}
	
}
