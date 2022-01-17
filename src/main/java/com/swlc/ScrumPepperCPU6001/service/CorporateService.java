package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.request.AddCorporateRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.DeleteCorporateRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateCorporateRequestDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hp
 */
public interface CorporateService {
    boolean createNewCorporate(AddCorporateRequestDTO addCorporateRequestDTO);
    boolean updateCorporate(UpdateCorporateRequestDTO updateCorporateRequestDTO);
    boolean deleteCorporate(DeleteCorporateRequestDTO deleteCorporateRequestDTO);
    String uploadCorporateLogo(MultipartFile file);

}
