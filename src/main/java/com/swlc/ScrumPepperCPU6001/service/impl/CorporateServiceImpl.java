package com.swlc.ScrumPepperCPU6001.service.impl;

import com.swlc.ScrumPepperCPU6001.constant.ApplicationConstant;
import com.swlc.ScrumPepperCPU6001.dto.CorporateDTO;
import com.swlc.ScrumPepperCPU6001.dto.MyCorporateDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddCorporateRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.DeleteCorporateRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateCorporateRequestDTO;
import com.swlc.ScrumPepperCPU6001.entity.CorporateEmployeeEntity;
import com.swlc.ScrumPepperCPU6001.entity.CorporateEntity;
import com.swlc.ScrumPepperCPU6001.entity.UserEntity;
import com.swlc.ScrumPepperCPU6001.enums.CorporateAccessStatusType;
import com.swlc.ScrumPepperCPU6001.enums.CorporateAccessType;
import com.swlc.ScrumPepperCPU6001.enums.StatusType;
import com.swlc.ScrumPepperCPU6001.exception.CorporateException;
import com.swlc.ScrumPepperCPU6001.repository.CorporateEmployeeRepository;
import com.swlc.ScrumPepperCPU6001.repository.CorporateRepository;
import com.swlc.ScrumPepperCPU6001.service.CorporateService;
import com.swlc.ScrumPepperCPU6001.util.FileWriter;
import com.swlc.ScrumPepperCPU6001.util.TokenValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    @Autowired
    private FileWriter fileWriter;

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
                            new Date(),
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

    @Override
    public boolean updateCorporate(UpdateCorporateRequestDTO updateCorporateRequestDTO) {
        log.info("Execute method updateCorporate : updateCorporateRequestDTO : " + updateCorporateRequestDTO.toString());
        try {
            UserEntity userEntity = tokenValidator.retrieveUserInformationFromAuthentication();
            Optional<CorporateEntity> corporateById = corporateRepository.findById(updateCorporateRequestDTO.getId());
            if(!corporateById.isPresent())
                throw new CorporateException(ApplicationConstant.RESOURCE_NOT_FOUND, "Corporate account not found");
            CorporateEntity corporateEntity = corporateById.get();
            Optional<CorporateEmployeeEntity> corporateSuperAdminOptional =
                    corporateEmployeeRepository.findByUserEntityAndCorporateEntityAndCorporateAccessTypeAndStatusType(
                    userEntity,
                    corporateEntity,
                    CorporateAccessType.CORPORATE_SUPER,
                    CorporateAccessStatusType.ACTIVE
            );
            if(!corporateSuperAdminOptional.isPresent())
                throw new CorporateException(
                        ApplicationConstant.UN_AUTH_ACTION,
                        "Unauthorized action. You can't processed this action"
                );
            String corporateLogoPath = "";
            corporateEntity.setName(updateCorporateRequestDTO.getName());
            corporateEntity.setAddress(updateCorporateRequestDTO.getAddress());
            corporateEntity.setContactNumber1(updateCorporateRequestDTO.getContactNumber1());
            corporateEntity.setContactNumber2(updateCorporateRequestDTO.getContactNumber2());
            corporateEntity.setEmail(updateCorporateRequestDTO.getEmail());
            corporateEntity.setStatusType(updateCorporateRequestDTO.getStatusType());
            if(updateCorporateRequestDTO.getCorporateLogo()!=null)
                corporateEntity.setCorporateLogo(corporateLogoPath);
            corporateRepository.save(corporateEntity);
            return true;
        } catch (Exception e) {
            log.error("Method updateCorporate : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean deleteCorporate(DeleteCorporateRequestDTO deleteCorporateRequestDTO) {
        log.info("Execute method deleteCorporate : corporateId : " + deleteCorporateRequestDTO.getId());
        try {
            UserEntity userEntity = tokenValidator.retrieveUserInformationFromAuthentication();
            Optional<CorporateEntity> corporateById = corporateRepository.findById(deleteCorporateRequestDTO.getId());
            if(!corporateById.isPresent())
                throw new CorporateException(ApplicationConstant.RESOURCE_NOT_FOUND, "Corporate account not found");
            CorporateEntity corporateEntity = corporateById.get();
            Optional<CorporateEmployeeEntity> corporateSuperAdminOptional =
                    corporateEmployeeRepository.findByUserEntityAndCorporateEntityAndCorporateAccessTypeAndStatusType(
                            userEntity,
                            corporateEntity,
                            CorporateAccessType.CORPORATE_SUPER,
                            CorporateAccessStatusType.ACTIVE
                    );
            if(!corporateSuperAdminOptional.isPresent())
                throw new CorporateException(
                        ApplicationConstant.UN_AUTH_ACTION,
                        "Unauthorized action. You can't processed this action"
                );
            corporateEntity.setStatusType(StatusType.DELETE);
            corporateRepository.save(corporateEntity);
            return true;
        } catch (Exception e) {
            log.error("Method deleteCorporate : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<MyCorporateDTO> getMyCorporates() {
        log.info("Execute method getMyCorporates : ");
        List<MyCorporateDTO> result = new ArrayList<>();
        try {
            UserEntity userEntity = tokenValidator.retrieveUserInformationFromAuthentication();
            List<CorporateEmployeeEntity> byUserEntityAndStatusType =
                    corporateEmployeeRepository.findByUserEntityAndStatusType(userEntity, CorporateAccessStatusType.ACTIVE);
            for (CorporateEmployeeEntity ce:byUserEntityAndStatusType) {
                result.add(this.prepareMyCorporateDTOs(ce));
            }
        } catch (Exception e) {
            log.error("Method getMyCorporates : " + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public String uploadCorporateLogo(MultipartFile file) {
        log.info("Execute method uploadCorporateLogo : " + file);
        try {
            String path = fileWriter.saveMultipartImage(file);
            return path;
        } catch (Exception e) {
            log.error("Method uploadCorporateLogo : " + e.getMessage(), e);
            throw e;
        }
    }

    private MyCorporateDTO prepareMyCorporateDTOs(CorporateEmployeeEntity ce) {
        log.info("Execute method prepareMyCorporateDTOs : " + ce.toString());
        try {
            CorporateEntity c = ce.getCorporateEntity();
            CorporateDTO corporateDTO = new CorporateDTO(
                    c.getId(),
                    c.getName(),
                    c.getAddress(),
                    c.getContactNumber1(),
                    c.getContactNumber2(),
                    c.getEmail(),
                    c.getCorporateLogo(),
                    c.getStatusType()
            );
            CorporateAccessType corporateAccessType = ce.getCorporateAccessType();
            return new MyCorporateDTO(corporateDTO, corporateAccessType);
        } catch (Exception e) {
            log.error("Method prepareMyCorporateDTOs : " + e.getMessage(), e);
            throw e;
        }
    }
}
