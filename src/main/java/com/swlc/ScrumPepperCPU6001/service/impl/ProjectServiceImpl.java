package com.swlc.ScrumPepperCPU6001.service.impl;

import com.swlc.ScrumPepperCPU6001.constant.ApplicationConstant;
import com.swlc.ScrumPepperCPU6001.dto.request.AddProjectRequestDTO;
import com.swlc.ScrumPepperCPU6001.entity.CorporateEmployeeEntity;
import com.swlc.ScrumPepperCPU6001.entity.CorporateEntity;
import com.swlc.ScrumPepperCPU6001.entity.ProjectEntity;
import com.swlc.ScrumPepperCPU6001.entity.UserEntity;
import com.swlc.ScrumPepperCPU6001.enums.CorporateAccessStatusType;
import com.swlc.ScrumPepperCPU6001.enums.CorporateAccessType;
import com.swlc.ScrumPepperCPU6001.enums.ProjectStatusType;
import com.swlc.ScrumPepperCPU6001.exception.CorporateEmployeeException;
import com.swlc.ScrumPepperCPU6001.exception.CorporateException;
import com.swlc.ScrumPepperCPU6001.repository.CorporateEmployeeRepository;
import com.swlc.ScrumPepperCPU6001.repository.CorporateRepository;
import com.swlc.ScrumPepperCPU6001.repository.ProjectRepository;
import com.swlc.ScrumPepperCPU6001.service.ProjectService;
import com.swlc.ScrumPepperCPU6001.util.TokenValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * @author hp
 */
@Service
@Log4j2
public class ProjectServiceImpl implements ProjectService {

    private final CorporateRepository corporateRepository;
    private final CorporateEmployeeRepository corporateEmployeeRepository;
    private final ProjectRepository projectRepository;
    @Autowired
    private TokenValidator tokenValidator;

    public ProjectServiceImpl(CorporateRepository corporateRepository,
                              CorporateEmployeeRepository corporateEmployeeRepository,
                              ProjectRepository projectRepository) {
        this.corporateRepository = corporateRepository;
        this.corporateEmployeeRepository = corporateEmployeeRepository;
        this.projectRepository = projectRepository;
    }


    @Override
    public boolean createNewProject(AddProjectRequestDTO addProjectRequestDTO) {
        log.info("Execute method createNewProject : addProjectRequestDTO : " + addProjectRequestDTO.toString());
        try {
            Optional<CorporateEntity> corporateById = corporateRepository.findById(addProjectRequestDTO.getCorporateId());
            if(!corporateById.isPresent())
                throw new CorporateEmployeeException(ApplicationConstant.RESOURCE_NOT_FOUND,
                        "Corporate account not found");
            CorporateEntity corporateEntity = corporateById.get();
            UserEntity userAdminEntity = tokenValidator.retrieveUserInformationFromAuthentication();
            Optional<CorporateEmployeeEntity> auth_user_admin =
                    corporateEmployeeRepository.findByUserEntityAndCorporateEntityAndStatusType(
                    userAdminEntity,
                    corporateEntity,
                    CorporateAccessStatusType.ACTIVE
            );
            if(!auth_user_admin.isPresent())
                throw new CorporateException(
                        ApplicationConstant.UN_AUTH_ACTION,
                        "Unauthorized action. You can't processed this action"
                );
            if(!(auth_user_admin.get().getCorporateAccessType().equals(CorporateAccessType.CORPORATE_SUPER) ||
                    auth_user_admin.get().getCorporateAccessType().equals(CorporateAccessType.CORPORATE_ADMIN)))
                throw new CorporateException(
                        ApplicationConstant.UN_AUTH_ACTION,
                        "Unauthorized action. You can't processed this action"
                );
            projectRepository.save(
                    new ProjectEntity(
                            corporateEntity,
                            addProjectRequestDTO.getProjectName(),
                            new Date(),
                            new Date(),
                            auth_user_admin.get(),
                            auth_user_admin.get(),
                            ProjectStatusType.ACTIVE
                    )
            );
            return true;
        } catch (Exception e) {
            log.error("Method createNewProject : " + e.getMessage(), e);
            throw e;
        }
    }
}
