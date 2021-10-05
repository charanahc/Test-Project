package com.ibm.poc.service;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

public interface ImageDetailsService {

	String uploadImage(MultipartFile file) throws FileUploadException;
}
