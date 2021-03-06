/**
 * 
 */
package com.nasscom.buildforindia.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.rowset.serial.SerialException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;
import com.nasscom.buildforindia.model.Address;
import com.nasscom.buildforindia.model.BabyData;
import com.nasscom.buildforindia.repositories.IdentificationRepository;

/**
 * @author tarun_000
 *
 */
@Service
public class IdentificationService {
	
	private final Logger logger = LoggerFactory.getLogger(IdentificationService.class);
	
	private final String UPLOADED_FOLDER;
	
	@Autowired
	private IdentificationRepository identificationRepository;
	
	@Autowired
	public IdentificationService(@Value("${uploaded.folder}") String uploadedFolder) {
		this.UPLOADED_FOLDER = uploadedFolder;
	}
	
	public BabyData saveData(String motherAadhar, String fatherAadhar, String birthDate, String contactNumber, String babyGender, String addressLine1, String addressLine2, String city, String state, MultipartFile leftPalmScan, MultipartFile rightPalmScan) throws IOException, SerialException, SQLException, ParseException {
		logger.debug("Executing save method - saving image files");
		BabyData babyData = new BabyData();
		byte[] leftPalmFingerprint = leftPalmScan.getBytes();
		byte[] rightPalmFingerprint = rightPalmScan.getBytes();
		FingerprintTemplate babyLeftFingerprintTemplate = new FingerprintTemplate().dpi(500).create(leftPalmFingerprint);
		FingerprintTemplate babyRightFingerprintTemplate = new FingerprintTemplate().dpi(500).create(rightPalmFingerprint);
		// Generation of UUID
		synchronized(this) {
			boolean isUnique = false;
			while (!isUnique) {
				String uniqueNumber = String.valueOf(System.currentTimeMillis() % 1000000000);
				BabyData nonUniqueBaby = identificationRepository.findOneByUuid(String.valueOf(uniqueNumber));
				isUnique = nonUniqueBaby == null ? true : false;
				babyData.setUuid(uniqueNumber);
			}
		}
		
		// Saving of fingerprint images of babies to datafolder
		Path leftPalmPath = Paths.get(UPLOADED_FOLDER + leftPalmScan.getOriginalFilename()+"-"+babyData.getUuid());
		Path rightPalmPath = Paths.get(UPLOADED_FOLDER + rightPalmScan.getOriginalFilename()+"-"+babyData.getUuid());
		Files.write(leftPalmPath, leftPalmFingerprint);
		Files.write(rightPalmPath, rightPalmFingerprint);
		
		// Creation of babyData object and mapping
		babyData.setLeftImageFile(leftPalmScan.getOriginalFilename()+"-"+babyData.getUuid());
		babyData.setRightImageFile(rightPalmScan.getOriginalFilename()+"-"+babyData.getUuid());
		babyData.setLeftTemplate(babyLeftFingerprintTemplate.serialize());
		babyData.setRightTemplate(babyRightFingerprintTemplate.serialize());
		babyData.setMotherAadhar(motherAadhar);
		babyData.setFatherAadhar(fatherAadhar);
		babyData.setBirthDate(new SimpleDateFormat("dd/mm/yyyy").parse(birthDate));
		babyData.setContactNumber(contactNumber);
		babyData.setGender(babyGender);
		
		Address address = new Address();
		address.setAddressLine1(addressLine1);
		address.setAddressLine2(addressLine2);
		address.setCity(city);
		address.setState(state);
		babyData.setAddress(address);
		
		//Persisting data
		return identificationRepository.save(babyData);
	}
	
	public List<BabyData> retrieveAll() {
		List<BabyData> babyList = new ArrayList<>();
		Iterable<BabyData> iterable = identificationRepository.findAll();
		iterable.forEach(babyList::add);
		return babyList;
	}

	public BabyData[] retrieveSimilarImageData(MultipartFile left, MultipartFile right) throws IOException {
		MinHeap  closelyResembelingBabies = new MinHeap(3);
		for(int i=0; i<3; i++){
			BabyData babyData = new BabyData();
			babyData.setScore(Double.MIN_VALUE);
			closelyResembelingBabies.insert(babyData);
		}
		logger.debug("Executing retrieve call to get similar image data");
		if (!StringUtils.isEmpty(left) && !StringUtils.isEmpty(right)) {
			// read multipart data and convert it into bytes
			// loop through all the files in uploaded folder and check similarity
			// find babyData based on the filename which is a match
			// return that babyData object or else return null
			byte[] babyFingerprintLeft = left.getBytes();
			byte[] babyFingerprintRight = right.getBytes();
			//BabyData baby = new BabyData();
			FingerprintTemplate babyFingerprintTemplateLeft = new FingerprintTemplate()
		    	    .dpi(500)
		    	    .create(babyFingerprintLeft);
			FingerprintTemplate babyFingerprintTemplateRight = new FingerprintTemplate()
		    	    .dpi(500)
		    	    .create(babyFingerprintRight);
			List<BabyData> babyList = identificationRepository.findByIsMissing(true);
			babyList.parallelStream().forEach(baby -> {
				FingerprintTemplate babytemplateLeft = new FingerprintTemplate()
					    .deserialize(baby.getLeftTemplate());
				FingerprintTemplate babytemplateRight = new FingerprintTemplate()
					    .deserialize(baby.getRightTemplate());
				double scoreLeft = new FingerprintMatcher()
		    		    .index(babytemplateLeft)
		    		    .match(babyFingerprintTemplateLeft);
				double scoreRight = new FingerprintMatcher()
		    		    .index(babytemplateRight)
		    		    .match(babyFingerprintTemplateRight);
				double scoreAverage = (scoreLeft + scoreRight)/2;
				baby.setScore(scoreAverage);
					
				synchronized (closelyResembelingBabies) {
					BabyData heapFrontBaby = closelyResembelingBabies.getHeap()[closelyResembelingBabies.getFront()];
					if(heapFrontBaby.getScore() < baby.getScore()){
						closelyResembelingBabies.remove();
						closelyResembelingBabies.insert(baby);
						closelyResembelingBabies.minHeap();
					}
				}
			});
			// todo get iterable of babies missing and match their template with this one
		}
		BabyData[] list = closelyResembelingBabies.getHeap();
		Arrays.sort(list);
		return list;
	}
	
	public boolean reportMissing(String babyId){
		
		BabyData missingBabyData = identificationRepository.findOneByUuid(babyId);
		missingBabyData.setMissing(true);
		identificationRepository.save(missingBabyData);
		
		return true;
	}

	public BabyData findBabyByUuid(String uuid) {
		return identificationRepository.findOneByUuid(uuid);
	}

	public BabyData updateData(BabyData babyData, MultipartFile leftMultipartFile, MultipartFile rightMultipartFile,
			String contactNumber) throws IOException {
		logger.debug("Executing update method - updating image files");
		
		if (leftMultipartFile != null && !StringUtils.isEmpty(leftMultipartFile.getOriginalFilename())) {
			byte[] leftPalmFingerprint = leftMultipartFile.getBytes();
			FingerprintTemplate babyLeftFingerprintTemplate = new FingerprintTemplate().dpi(500).create(leftPalmFingerprint);
			Path leftPalmPath = Paths.get(UPLOADED_FOLDER + leftMultipartFile.getOriginalFilename()+"-"+babyData.getUuid());
			Files.write(leftPalmPath, leftPalmFingerprint);
			babyData.setLeftImageFile(leftMultipartFile.getOriginalFilename()+"-"+babyData.getUuid());
			babyData.setLeftTemplate(babyLeftFingerprintTemplate.serialize());
		}
		
		if (rightMultipartFile != null && !StringUtils.isEmpty(rightMultipartFile.getOriginalFilename())) {
			byte[] rightPalmFingerprint = rightMultipartFile.getBytes();
			FingerprintTemplate babyRightFingerprintTemplate = new FingerprintTemplate().dpi(500).create(rightPalmFingerprint);
			Path rightPalmPath = Paths.get(UPLOADED_FOLDER + rightMultipartFile.getOriginalFilename()+"-"+babyData.getUuid());
			Files.write(rightPalmPath, rightPalmFingerprint);
			babyData.setRightImageFile(leftMultipartFile.getOriginalFilename()+"-"+babyData.getUuid());
			babyData.setRightTemplate(babyRightFingerprintTemplate.serialize());
		}
		
		babyData.setContactNumber(contactNumber);
		//Persisting data
		return identificationRepository.save(babyData);
	}

	public void deleteAll() {
		identificationRepository.deleteAll();		
	}

}
