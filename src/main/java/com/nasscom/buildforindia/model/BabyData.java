/**
 * 
 */
package com.nasscom.buildforindia.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * @author tarun_000
 *
 */
@Entity
public class BabyData {
	@javax.persistence.Id
	@GeneratedValue
	private Long id;
	
	private String uuid;
	
	private String leftImageFile;
	
	private String rightImageFile;
	
	private String motherAadhar;
	
	private String fatherAadhar;
	
	private String birthPlace;
	
	private boolean isMissing;
	
	private String leftTemplate;
	
	private String rightTemplate;
	
	private double score;
	
	public String getLeftImageFile() {
		return leftImageFile;
	}

	public void setLeftImageFile(String leftImageFile) {
		this.leftImageFile = leftImageFile;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getLeftTemplate() {
		return leftTemplate;
	}

	public void setLeftTemplate(String leftTemplate) {
		this.leftTemplate = leftTemplate;
	}

	public String getRightTemplate() {
		return rightTemplate;
	}

	public void setRightTemplate(String rightTemplate) {
		this.rightTemplate = rightTemplate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLefImageFile() {
		return leftImageFile;
	}

	public void setLefImageFile(String lefImageFile) {
		this.leftImageFile = lefImageFile;
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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
}
