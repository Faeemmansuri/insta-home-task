package com.instagram.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
@Table(name = "user_images")
public class UserImageEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -5304249764109433697L;
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
    private UUID id;
	
	@Column(name = "image_name")
	private String imageName;
	
	@Column(name = "image_path")
	private String imagePath;
	
	@Column(name = "file_ext")
	private String fileExt;

}
