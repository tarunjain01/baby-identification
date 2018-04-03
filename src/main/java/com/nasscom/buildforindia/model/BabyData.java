/**
 * 
 */
package com.nasscom.buildforindia.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

/**
 * @author tarun_000
 *
 */
@Entity
public class BabyData {
	@javax.persistence.Id
	@GeneratedValue
	private Long id;
	
	private String lefImageFile;
	
	private String rightImageFile;
	
	private String motherAadhar;
	
	private String fatherAadhar;
	
	private String birthPlace;
	
	private boolean isMissing;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLefImageFile() {
		return lefImageFile;
	}

	public void setLefImageFile(String lefImageFile) {
		this.lefImageFile = lefImageFile;
	}

	public String getRightImageFile() {
		return rightImageFile;
	}

	public void setRightImageFile(String rightImageFile) {
		this.rightImageFile = rightImageFile;
	}

	public String getMotherAadhar() {
		return motherAadhar;
	}

	public void setMotherAadhar(String motherAadhar) {
		this.motherAadhar = motherAadhar;
	}

	public String getFatherAadhar() {
		return fatherAadhar;
	}

	public void setFatherAadhar(String fatherAadhar) {
		this.fatherAadhar = fatherAadhar;
	}

	public String getBirthPlace() {
		return birthPlace;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	public boolean isMissing() {
		return isMissing;
	}

	public void setMissing(boolean isMissing) {
		this.isMissing = isMissing;
	}
	
}
