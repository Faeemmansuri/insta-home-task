package com.instagram.file.service;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.instagram.exception.FileNotFoundException;
import com.instagram.exception.FileStorageException;
import com.instagram.properties.FileUploadProperties;

@Service
public class FileServiceImpl implements FileService {
	
	private final Path dirLocation;
	
	@Autowired
	public FileServiceImpl(FileUploadProperties fileUploadProperties) {
		this.dirLocation = Paths.get(fileUploadProperties.getLocation())
                .toAbsolutePath()
                .normalize();
	}
	
	@Override
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(this.dirLocation);
        } 
        catch (Exception ex) {
            throw new FileStorageException("Could not create upload dir!");
        }
    }

	@Override
	public String saveFile(MultipartFile file, String fileName) {
		try {
			Path destinationPath = this.dirLocation.resolve(fileName);
			Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
			return destinationPath.toString();
		} catch (Exception e) {
			throw new FileStorageException("Could not upload file");
		}
	}

	@Override
	public Resource loadFile(String fileId) {
		try {
			Path file = this.dirLocation.resolve(fileId).normalize();
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new FileNotFoundException("Could not find file");
			}
		} catch (MalformedURLException e) {
			throw new FileNotFoundException("Could not download file");
		}
	}

}
