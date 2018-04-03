/**
 * 
 */
package com.nasscom.buildforindia.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nasscom.buildforindia.model.BabyData;
import com.nasscom.buildforindia.service.IdentificationService;

/**
 * @author tarun_000
 *
 */
@RestController
public class IdentificationController {
	
	private final Logger logger = LoggerFactory.getLogger(IdentificationController.class);
	
	@Autowired
	private IdentificationService identificationService;
	
	@PostMapping("/api/upload/checkSimilarity")
	public BabyData getSimilarImageIfExist(@RequestParam MultipartFile footPrint) {
		return identificationService.retrieveSimilarImageData(footPrint);
	}
	
	@PostMapping("/api/upload/files")
	public ResponseEntity<?> addImageAndInfo(
			@RequestParam String motherAadhar, 
			@RequestParam String fatherAadhar,
			@RequestParam String birthPlace,
			@RequestParam("files") MultipartFile[] uploadedFiles) {
		
		// Get file name
        String uploadedFileName = Arrays.stream(uploadedFiles).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

        if (StringUtils.isEmpty(uploadedFileName)) {
            return new ResponseEntity<String>("please select a file!", HttpStatus.OK);
        }
        
        try {
        	BabyData babyData = identificationService.saveData(motherAadhar, fatherAadhar, birthPlace, uploadedFiles);
		
        } catch (Exception e) {
        	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<String>("Successfully Uploaded", HttpStatus.OK);
	}
	
	@GetMapping("/api/retrieve/list")
	public List<BabyData> retrieveImage() {
		return identificationService.retrieveAll();
	}
	
	@PostMapping("/api/upload/missing")
	public ResponseEntity<?> reportMissing(
			@RequestParam String motherAadhar, 
			@RequestParam String fatherAadhar,
			@RequestParam String babyId) {
	
		 return new ResponseEntity<String>("Successfully Uploaded", HttpStatus.OK);
	}
	
	@PostMapping("/api/update/files")
	public ResponseEntity<?> updateInfo(
			@RequestParam String babyId,
			@RequestParam("files") MultipartFile[] uploadedFiles) {
	
		 return new ResponseEntity<String>("Successfully Uploaded", HttpStatus.OK);
	}
	

}
