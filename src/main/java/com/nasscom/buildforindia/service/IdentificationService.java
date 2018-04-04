/**
 * 
 */
package com.nasscom.buildforindia.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;
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
	public IdentificationService(@Value("${uploaded.folder") String uploadedFolder) {
		this.UPLOADED_FOLDER = uploadedFolder;
	}
	
	public BabyData saveData(String motherAadhar, String fatherAadhar, String birthPlace, MultipartFile[] uploadedFiles) throws IOException {
		logger.debug("Executing save method - saving image files");
		BabyData babyData = new BabyData();
		for (MultipartFile file : uploadedFiles) {
			if (file.isEmpty())
				continue;
			
			byte[] babyFingerprint = file.getBytes();
			
			FingerprintTemplate babyFingerprintTemplate = new FingerprintTemplate()
		    	    .dpi(500)
		    	    .create(babyFingerprint);
			
			Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename()+babyData.getId());
			Files.write(path, babyFingerprint);
			
			babyData.setLefImageFile(file.getOriginalFilename()+babyData.getId());
			babyData.setRightImageFile(file.getOriginalFilename()+babyData.getId());
			
			babyData.setLeftTemplate(babyFingerprintTemplate.serialize());
			babyData.setRightTemplate(babyFingerprintTemplate.serialize());
			
		}
		babyData.setMotherAadhar(motherAadhar);
		babyData.setFatherAadhar(fatherAadhar);
		babyData.setBirthPlace(birthPlace);
		
		return identificationRepository.save(babyData);
	}

	public List<BabyData> retrieveAll() {
		List<BabyData> babyList = new ArrayList<>();
		Iterable<BabyData> iterable = identificationRepository.findAll();
		iterable.forEach(babyList::add);
		return babyList;
	}

	public BabyData retrieveSimilarImageData(MultipartFile footPrint) throws IOException {
		logger.debug("Executing retrieve call to get similar image data");
		if (footPrint != null && !footPrint.isEmpty()) {
			// read multipart data and convert it into bytes
			// loop through all the files in uploaded folder and check similarity
			// find babyData based on the filename which is a match
			// return that babyData object or else return null
			byte[] babyFingerprint = footPrint.getBytes();
			//BabyData baby = new BabyData();
			FingerprintTemplate babyFingerprintTemplate = new FingerprintTemplate()
		    	    .dpi(500)
		    	    .create(babyFingerprint);
			List<BabyData> babyList = identificationRepository.findAllMissingBabies(true);
			babyList.forEach(baby -> {
				FingerprintTemplate babytemplate = new FingerprintTemplate()
					    .deserialize(baby.getLeftTemplate());
				double score = new FingerprintMatcher()
		    		    .index(babytemplate)
		    		    .match(babyFingerprintTemplate);
				if( score > 40){
					
				}
				
			});
			// todo get iterable of babies missing and match their template with this one
		}
		return null;
	}

}
