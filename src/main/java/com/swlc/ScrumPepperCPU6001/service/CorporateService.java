package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.MyCorporateDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddCorporateRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.DeleteCorporateRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.GetCorporateDetailsRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateCorporateRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.GetCorporateDetailsResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author hp
 */
public interface CorporateService {
    boolean createNewCorporate(AddCorporateRequestDTO addCorporateRequestDTO);
    boolean updateCorporate(UpdateCorporateRequestDTO updateCorporateRequestDTO);
    boolean deleteCorporate(DeleteCorporateRequestDTO deleteCorporateRequestDTO);
    List<MyCorporateDTO> getMyCorporates();
    String uploadCorporateLogo(MultipartFile file);
    GetCorporateDetailsResponseDTO getCorporateDetails(GetCorporateDetailsRequestDTO getCorporateDetailsRequestDTO);

}
