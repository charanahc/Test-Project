package com.ibm.poc.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.poc.service.ImageDetailsService;

@Service
public class ImageDetailsServiceImpl implements ImageDetailsService {

	@Value("${upload.path}")
	private String uploadPath;

	@Value("${server.path}")
	private String serverPath;

	@Override
	public String uploadImage(MultipartFile file) throws FileUploadException {
		StringBuilder location = new StringBuilder();
		try {
			File f = new File(uploadPath);
			if (!f.exists()) {
				throw new FileUploadException("File path not exist");
			}

			String directoryName = uploadPath.concat("images");
			File directory = new File(directoryName);
			if (!directory.exists()) {
				directory.mkdir();
			}

			Path root = Paths.get(directoryName);
			String name = System.currentTimeMillis() + file.getOriginalFilename();
			location.append(serverPath + name);
			Path resolve = root.resolve(name);
			Files.copy(file.getInputStream(), resolve);
		} catch (Exception e) {
			throw new FileUploadException("Could not store the file. Error: " + e.getMessage());
		}
		return location.toString();
	}

}
