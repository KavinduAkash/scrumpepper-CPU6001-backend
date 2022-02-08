package com.swlc.ScrumPepperCPU6001.service.impl;

import com.swlc.ScrumPepperCPU6001.constant.ApplicationConstant;
import com.swlc.ScrumPepperCPU6001.dto.CorporateDTO;
import com.swlc.ScrumPepperCPU6001.dto.ProjectDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddProjectMemberDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddProjectRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.GetCorporateDetailsResponseDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.YourProjectResponseDTO;
import com.swlc.ScrumPepperCPU6001.entity.*;
import com.swlc.ScrumPepperCPU6001.enums.CorporateAccessStatusType;
import com.swlc.ScrumPepperCPU6001.enums.CorporateAccessType;
import com.swlc.ScrumPepperCPU6001.enums.ProjectMemberStatusType;
import com.swlc.ScrumPepperCPU6001.enums.ProjectStatusType;
import com.swlc.ScrumPepperCPU6001.exception.CorporateEmployeeException;
import com.swlc.ScrumPepperCPU6001.exception.CorporateException;
import com.swlc.ScrumPepperCPU6001.exception.ProjectException;
import com.swlc.ScrumPepperCPU6001.repository.CorporateEmployeeRepository;
import com.swlc.ScrumPepperCPU6001.repository.CorporateRepository;
import com.swlc.ScrumPepperCPU6001.repository.ProjectMemberRepository;
import com.swlc.ScrumPepperCPU6001.repository.ProjectRepository;
import com.swlc.ScrumPepperCPU6001.service.ProjectService;
import com.swlc.ScrumPepperCPU6001.util.TokenValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private final ProjectMemberRepository projectMemberRepository;
    @Autowired
    private TokenValidator tokenValidator;

    public ProjectServiceImpl(CorporateRepository corporateRepository,
                              CorporateEmployeeRepository corporateEmployeeRepository,
                              ProjectRepository projectRepository, ProjectMemberRepository projectMemberRepository) {
        this.corporateRepository = corporateRepository;
        this.corporateEmployeeRepository = corporateEmployeeRepository;
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
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
            ProjectEntity savedProject = projectRepository.save(
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
            List<AddProjectMemberDTO> projectMembers = addProjectRequestDTO.getProjectMembers();
            if(projectMembers!=null) {
                for (AddProjectMemberDTO pm : projectMembers) {
                    Optional<CorporateEmployeeEntity> byIdAndCorporateEntity = corporateEmployeeRepository.findByIdAndCorporateEntity(pm.getCorporateEmployeeId(), corporateEntity);
                    if(!byIdAndCorporateEntity.isPresent())
                        throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "Corporate member not found");
                    projectMemberRepository.save(
                            new ProjectMemberEntity(
                                    savedProject,
                                    byIdAndCorporateEntity.get(),
                                    new Date(),
                                    new Date(),
                                    auth_user_admin.get(),
                                    auth_user_admin.get(),
                                    pm.getScrumRole(),
                                    ProjectMemberStatusType.ACTIVE
                            )
                    );
                }
            }
            return true;
        } catch (Exception e) {
            log.error("Method createNewProject : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<GetCorporateDetailsResponseDTO> getAllMyProjects() {
        try {
            List<GetCorporateDetailsResponseDTO> corporateProjectList = new ArrayList<>();
            UserEntity userEntity = tokenValidator.retrieveUserInformationFromAuthentication();
            List<CorporateEmployeeEntity> byUserEntityAndStatusType =
                    corporateEmployeeRepository.findByUserEntityAndStatusType(userEntity, CorporateAccessStatusType.ACTIVE);
            for (CorporateEmployeeEntity ce:byUserEntityAndStatusType) {
                CorporateEntity corporateEntity = ce.getCorporateEntity();
                CorporateDTO corporateDTO = new CorporateDTO(
                        corporateEntity.getId(),
                        corporateEntity.getName(),
                        corporateEntity.getAddress(),
                        corporateEntity.getContactNumber1(),
                        corporateEntity.getContactNumber2(),
                        corporateEntity.getEmail(),
                        corporateEntity.getCorporateLogo(),
                        corporateEntity.getStatusType()
                );
                List<ProjectMemberEntity> byCorporateEmployeeEntity = projectMemberRepository.getProjectMemberByCorporateEmployeeEntity(ce);
                List<YourProjectResponseDTO> projectList = new ArrayList<>();
                for (ProjectMemberEntity pm : byCorporateEmployeeEntity) {
                    ProjectEntity p = pm.getProjectEntity();
                    ProjectDTO projectDTO = new ProjectDTO(
                            p.getId(),
                            null,
                            p.getProjectName(),
                            p.getCreatedDate(),
                            p.getModifiedDate(),
                            null,
                            null,
                            p.getStatusType());
                    projectList.add(new YourProjectResponseDTO(projectDTO, null, pm.getScrumRole()));
                }
                corporateProjectList.add(
                        new GetCorporateDetailsResponseDTO(
                                corporateDTO,
                                ce.getCorporateAccessType(),
                                null,
                                projectList
                        )
                );
            }
            return corporateProjectList;
        } catch (Exception e) {
            throw e;
        }
    }
}
