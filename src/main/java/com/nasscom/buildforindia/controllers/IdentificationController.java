/**
 * 
 */
package com.nasscom.buildforindia.controllers;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nasscom.buildforindia.model.Address;
import com.nasscom.buildforindia.model.BabyData;
import com.nasscom.buildforindia.service.IdentificationService;
import com.nasscom.buildforindia.service.UIDAIVerificationService;

/**
 * @author tarun_000
 *
 */
@RestController
public class IdentificationController {
	
	private final Logger logger = LoggerFactory.getLogger(IdentificationController.class);
	
	@Autowired
	private IdentificationService identificationService;

	@Autowired
	private UIDAIVerificationService otpVerificationService;
	
	@PostMapping("/api/upload/files")
	public ResponseEntity<?> addImageAndInfo(
			@RequestParam String motherAadhar, 
			@RequestParam String fatherAadhar,
			@RequestParam String birthDate,
			@RequestParam String babyGender,
			@RequestParam String addressLine1,
			@RequestParam(required = false) String addressLine2,
			@RequestParam String city,
			@RequestParam String state,
			@RequestParam("leftPalm") MultipartFile leftPalmScan,
			@RequestParam("rightPalm") MultipartFile rightPalmScan) {
		
		MultipartFile[] uploadedFiles = new MultipartFile[] {leftPalmScan, rightPalmScan};
		
		// Get file name
        String uploadedFileName = Arrays.stream(uploadedFiles).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

        if (StringUtils.isEmpty(uploadedFileName)) {
            return new ResponseEntity<String>("please select a file!", HttpStatus.OK);
        }
        BabyData babyData = null;
        try {
        	babyData = identificationService.saveData(motherAadhar, fatherAadhar, birthDate, babyGender, addressLine1, addressLine2, city, state, leftPalmScan, rightPalmScan);
		
        } catch (Exception e) {
        	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<BabyData>(babyData, HttpStatus.OK);
	}
	
	@PostMapping("/api/update/data")
	public ResponseEntity<?> updateImageAndInfo(@RequestParam(name = "leftPalm", required = false) MultipartFile leftMultipartFile,
			@RequestParam(name = "rightPalm", required = false) MultipartFile rightMultipartFile, 
			@RequestParam(required = false) String contactNumber,
			@RequestParam String uuid) {
		BabyData babyData = identificationService.findBabyByUuid(uuid);
		
		try {
        	babyData = identificationService.updateData(babyData, leftMultipartFile, rightMultipartFile, contactNumber);
		} catch (Exception e) {
        	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<BabyData>(babyData, HttpStatus.OK);
	}
	
	@GetMapping("/api/find/{uuid}")
	public BabyData findBabyByUuid(@PathVariable String uuid) {
		return identificationService.findBabyByUuid(uuid);
	}
	
	@GetMapping("/api/retrieve/list")
	public List<BabyData> retrieveImage() {
		return identificationService.retrieveAll();
	}
	
	@PostMapping("/api/retrieve/match")
	public BabyData[] retrieveMatch(@RequestParam("scanToMatchLeft") MultipartFile uploadedFileLeft,
			@RequestParam("scanToMatchRight") MultipartFile uploadedFileRight) throws IOException {
		return identificationService.retrieveSimilarImageData(uploadedFileLeft, uploadedFileRight);
	}
	
	@PostMapping("/api/upload/missing")
	public ResponseEntity<?> reportMissing(
			@RequestParam String motherAadhar, 
			@RequestParam String fatherAadhar,
			@RequestParam String babyId) {
	
		identificationService.reportMissing(babyId);
		 return new ResponseEntity<String>("Successfully Uploaded", HttpStatus.OK);
	}
	
	@PostMapping("/api/update/files")
	public ResponseEntity<?> updateInfo(
			@RequestParam String babyId,
			@RequestParam("files") MultipartFile[] uploadedFiles) {
	
		 return new ResponseEntity<String>("Successfully Uploaded", HttpStatus.OK);
	}

	@GetMapping("/api/otp/send")
	public ResponseEntity<?> sentUidaiOtp(
			@RequestParam String aadharId) {
		 
		try {
			return new ResponseEntity<String>(this.otpVerificationService.sendOTPForUid(aadharId), HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>("ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@GetMapping("/api/otp/verify")
	public ResponseEntity<?> sentUidaiOtp(
			@RequestParam String key,
			@RequestParam String otp) {
		 
		try {
			return new ResponseEntity<String>(this.otpVerificationService.verifyOTP(key, otp)? "SUCCESS" : "FAILURE", HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<String>("ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
}
