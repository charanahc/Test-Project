package com.ibm.poc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.poc.entity.ImageDetails;
import com.ibm.poc.repository.ImageDetailsRepo;
import com.ibm.poc.service.ImageDetailsService;
import com.ibm.poc.util.ResponseDetails;
import com.ibm.poc.util.StringConstant;
import com.ibm.poc.util.Utility;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/imagedetails")
@CrossOrigin
@Api(value = "Image Details")
public class ImageDetailsController {

	@Autowired
	ImageDetailsRepo imageDetailsRepo;

	@Autowired
	ImageDetailsService imageDetailsService;

	@Autowired
	Utility utility;

	@SuppressWarnings({ "static-access", "rawtypes" })
	@GetMapping("/getData")
	public ResponseEntity<ResponseDetails> getImageDetails() {
		ResponseDetails responseDetails = null;
		try {
			List<ImageDetails> opl = imageDetailsRepo.findAll();
			if (!opl.isEmpty()) {
				responseDetails = utility.formResponseObject(true, StringConstant.SUCCESS_MESSAGE_GET, "", opl,
						HttpStatus.OK);
			} else {
				responseDetails = utility.formResponseObject(false, StringConstant.DATA_NOT_FOUND, "", opl,
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(responseDetails.getStatusCode()).body(responseDetails);
	}

	@SuppressWarnings({ "static-access", "rawtypes" })
	@PostMapping("/storeData")
	public ResponseEntity<ResponseDetails> addImageDetails(@RequestBody ImageDetails details) {
		ResponseDetails responseDetails = null;
		try {
			responseDetails = utility.formResponseObject(true, StringConstant.SUCCESS_MESSAGE_CREATE, "",
					imageDetailsRepo.save(details), HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(responseDetails.getStatusCode()).body(responseDetails);
	}

	@SuppressWarnings({ "static-access", "rawtypes" })
	@DeleteMapping("/deleteData/{id}")
	public ResponseEntity<ResponseDetails> deleteImageDetails(@PathVariable Integer id) {
		ResponseDetails responseDetails = null;
		try {
			imageDetailsRepo.deleteById(id);
			responseDetails = utility.formResponseObject(true, StringConstant.SUCCESS_MESSAGE_DELETE, "", "",
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(responseDetails.getStatusCode()).body(responseDetails);
	}

	@SuppressWarnings({ "static-access", "rawtypes" })
	@PostMapping(value = "/uploadImageDetails")
	public ResponseEntity<ResponseDetails> uploadImageDetails(@RequestParam("imageName") String imageName,
			@RequestParam("file") MultipartFile file) {
		ResponseDetails responseDetails = null;
		try {
			ImageDetails details = new ImageDetails();
			details.setImageName(imageName);
			details.setImageUrl(imageDetailsService.uploadImage(file));

			responseDetails = utility.formResponseObject(true, StringConstant.SUCCESS_MESSAGE_CREATE, "",
					imageDetailsRepo.save(details), HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			responseDetails = utility.formResponseObject(false, StringConstant.UN_SUCCESS_MESSAGE_CREATE,
					e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return ResponseEntity.status(responseDetails.getStatusCode()).body(responseDetails);
	}
}
