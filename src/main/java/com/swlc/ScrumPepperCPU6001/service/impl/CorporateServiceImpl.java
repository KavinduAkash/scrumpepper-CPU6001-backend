package com.swlc.ScrumPepperCPU6001.service.impl;

import com.swlc.ScrumPepperCPU6001.dto.request.AddCorporateRequestDTO;
import com.swlc.ScrumPepperCPU6001.entity.CorporateEmployeeEntity;
import com.swlc.ScrumPepperCPU6001.entity.CorporateEntity;
import com.swlc.ScrumPepperCPU6001.entity.UserEntity;
import com.swlc.ScrumPepperCPU6001.enums.CorporateAccessStatusType;
import com.swlc.ScrumPepperCPU6001.enums.CorporateAccessType;
import com.swlc.ScrumPepperCPU6001.enums.StatusType;
import com.swlc.ScrumPepperCPU6001.repository.CorporateEmployeeRepository;
import com.swlc.ScrumPepperCPU6001.repository.CorporateRepository;
import com.swlc.ScrumPepperCPU6001.service.CorporateService;
import com.swlc.ScrumPepperCPU6001.util.TokenValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author hp
 */
@Service
@Log4j2
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class CorporateServiceImpl implements CorporateService {

    private final CorporateRepository corporateRepository;
    private final CorporateEmployeeRepository corporateEmployeeRepository;
    @Autowired
    private TokenValidator tokenValidator;

    public CorporateServiceImpl(CorporateRepository corporateRepository, CorporateEmployeeRepository corporateEmployeeRepository) {
        this.corporateRepository = corporateRepository;
        this.corporateEmployeeRepository = corporateEmployeeRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean createNewCorporate(AddCorporateRequestDTO addCorporateRequestDTO) {
        log.info("Execute method createNewCorporate : addCorporateRequestDTO : " + addCorporateRequestDTO.toString());
        try {
            UserEntity userEntity = tokenValidator.retrieveUserInformationFromAuthentication();
            String corporateLogoPath = "";
            CorporateEntity corporateEntity = corporateRepository.save(
                    new CorporateEntity(
                            addCorporateRequestDTO.getName(),
                            addCorporateRequestDTO.getAddress(),
                            addCorporateRequestDTO.getContactNumber1(),
                            addCorporateRequestDTO.getContactNumber2(),
                            addCorporateRequestDTO.getEmail(),
                            corporateLogoPath,
                            new Date(),
                            StatusType.ACTIVE
                    )
            );
            corporateEmployeeRepository.save(
                    new CorporateEmployeeEntity(
                            userEntity,
                            corporateEntity,
                            CorporateAccessType.CORPORATE_SUPER,
                            new Date(),
                            CorporateAccessStatusType.ACTIVE
                    )
            );
            return true;
        } catch (Exception e) {
            log.error("Method createNewCorporate : " + e.getMessage(), e);
            throw e;
        }
    }
}
