/**
 * 
 */
package com.nasscom.buildforindia.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nasscom.buildforindia.model.BabyData;

/**
 * @author tarun_000
 *
 */
@Repository
public interface IdentificationRepository extends CrudRepository<BabyData, Integer>{
	
	//public List<BabyData> findByLeftImageFileOrRightImageFile(String fileName);
	
	//public List<BabyData> findAllMissingBabies(boolean isMissing);
}
