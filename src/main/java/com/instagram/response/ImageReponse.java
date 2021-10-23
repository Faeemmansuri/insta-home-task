package com.instagram.response;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageReponse {

	private UUID imageId;
	private String imageName;
	private String imageUrl;
	private Date uploadedOn; 
	
}
