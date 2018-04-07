/**
 * 
 */
package com.nasscom.buildforindia.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToOne;

/**
 * @author tarun_000
 *
 */
@Entity
public class BabyData implements Comparable<BabyData>{
	@javax.persistence.Id
	@GeneratedValue
	private Long id;
	
	private String uuid;
	
	private String contactNumber;
	
	private String gender;
	
	private String leftImageFile;
	
	private String rightImageFile;
	
	private String babyRecentImage;
	
	@OneToOne(cascade=CascadeType.ALL)
	private Address address;
	
	private String motherAadhar;
	
	private String fatherAadhar;
	
	private Date birthDate;
	
	private Date missingDate;
	
	private Date reportingDate;
	
	private boolean isMissing;
	
	@Column(length = 32672)
	private String leftTemplate;
	
	@Column(length = 32672)
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

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getBabyRecentImage() {
		return babyRecentImage;
	}

	public void setBabyRecentImage(String babyRecentImage) {
		this.babyRecentImage = babyRecentImage;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Date getMissingDate() {
		return missingDate;
	}

	public void setMissingDate(Date missingDate) {
		this.missingDate = missingDate;
	}

	public Date getReportingDate() {
		return reportingDate;
	}

	public void setReportingDate(Date reportingDate) {
		this.reportingDate = reportingDate;
	}
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public int compareTo(BabyData o) {
		// TODO Auto-generated method stub
		if(this.score > o.score){
			return -1;
		}else{
			return 1;
		}
	}
	
}
