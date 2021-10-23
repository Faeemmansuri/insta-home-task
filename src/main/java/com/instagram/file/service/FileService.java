package com.instagram.file.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	
    void init();
    
    String saveFile(MultipartFile file, String fileName);
    
    Resource loadFile(String fileId);
    
}