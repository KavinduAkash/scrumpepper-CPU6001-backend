package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.request.AddCorporateRequestDTO;

/**
 * @author hp
 */
public interface CorporateService {
    boolean createNewCorporate(AddCorporateRequestDTO addCorporateRequestDTO);
}
