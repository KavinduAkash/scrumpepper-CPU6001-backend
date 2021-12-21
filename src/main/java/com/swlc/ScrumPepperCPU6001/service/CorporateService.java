package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.request.AddCorporateRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateCorporateRequestDTO;

/**
 * @author hp
 */
public interface CorporateService {
    boolean createNewCorporate(AddCorporateRequestDTO addCorporateRequestDTO);
    boolean updateCorporate(UpdateCorporateRequestDTO updateCorporateRequestDTO);
}
