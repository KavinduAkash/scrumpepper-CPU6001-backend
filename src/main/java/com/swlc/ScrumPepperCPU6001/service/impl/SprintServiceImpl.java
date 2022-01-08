package com.swlc.ScrumPepperCPU6001.service.impl;

import com.swlc.ScrumPepperCPU6001.constant.ApplicationConstant;
import com.swlc.ScrumPepperCPU6001.dto.CorporateDTO;
import com.swlc.ScrumPepperCPU6001.dto.CorporateEmployeeDTO;
import com.swlc.ScrumPepperCPU6001.dto.SprintDTO;
import com.swlc.ScrumPepperCPU6001.dto.UserDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddSprintRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateSprintRequestDTO;
import com.swlc.ScrumPepperCPU6001.entity.*;
import com.swlc.ScrumPepperCPU6001.enums.CorporateAccessStatusType;
import com.swlc.ScrumPepperCPU6001.enums.CorporateAccessType;
import com.swlc.ScrumPepperCPU6001.enums.ScrumRoles;
import com.swlc.ScrumPepperCPU6001.enums.SprintStatusType;
import com.swlc.ScrumPepperCPU6001.exception.CorporateException;
import com.swlc.ScrumPepperCPU6001.exception.ProjectException;
import com.swlc.ScrumPepperCPU6001.repository.*;
import com.swlc.ScrumPepperCPU6001.service.SprintService;
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
public class SprintServiceImpl implements SprintService {

    private final ProjectRepository projectRepository;
    private final SprintRepository sprintRepository;
    private final UserStoryRepository userStoryRepository;
    private final CorporateEmployeeRepository corporateEmployeeRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserStoryLabelRepository userStoryLabelRepository;
    private final ProjectUserStoryLabelRepository projectUserStoryLabelRepository;
    @Autowired
    private TokenValidator tokenValidator;


    public SprintServiceImpl(ProjectRepository projectRepository, SprintRepository sprintRepository,
                             UserStoryRepository userStoryRepository, CorporateEmployeeRepository corporateEmployeeRepository,
                             ProjectMemberRepository projectMemberRepository, UserStoryLabelRepository userStoryLabelRepository,
                             ProjectUserStoryLabelRepository projectUserStoryLabelRepository) {
        this.projectRepository = projectRepository;
        this.sprintRepository = sprintRepository;
        this.userStoryRepository = userStoryRepository;
        this.corporateEmployeeRepository = corporateEmployeeRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.userStoryLabelRepository = userStoryLabelRepository;
        this.projectUserStoryLabelRepository = projectUserStoryLabelRepository;
    }

    @Override
    public SprintDTO createSprint(AddSprintRequestDTO addSprintRequestDTO) {
        log.info("Execute method createSprint : addSprintRequestDTO : " + addSprintRequestDTO.toString());
        try {
            Optional<ProjectEntity> projectById = projectRepository.findById(addSprintRequestDTO.getProjectId());
            if(!projectById.isPresent())
                throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "Project not found");
            ProjectEntity projectEntity = projectById.get();
            CorporateEntity corporateEntity = projectEntity.getCorporateEntity();
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
                    auth_user_admin.get().getCorporateAccessType().equals(CorporateAccessType.CORPORATE_ADMIN))) {
                Optional<ProjectMemberEntity> byProjectEntityAndCorporateEmployeeEntity =
                        projectMemberRepository.findByProjectEntityAndCorporateEmployeeEntity(
                                projectEntity,
                                auth_user_admin.get()
                        );
                if(!byProjectEntityAndCorporateEmployeeEntity.isPresent())
                    throw new CorporateException(
                            ApplicationConstant.UN_AUTH_ACTION,
                            "Unauthorized action. You can't processed this action"
                    );
                ProjectMemberEntity projectMemberEntity = byProjectEntityAndCorporateEmployeeEntity.get();
                if(!(projectMemberEntity.getScrumRole().equals(ScrumRoles.PRODUCT_OWNER) ||
                        projectMemberEntity.getScrumRole().equals(ScrumRoles.PRODUCT_OWNER)))
                    throw new CorporateException(
                            ApplicationConstant.UN_AUTH_ACTION,
                            "Unauthorized action. You can't processed this action"
                    );
            }
            Optional<ProjectSprintEntity> byProjectEntityAndSprintName =
                    sprintRepository.findByProjectEntityAndSprintName(projectById.get(), addSprintRequestDTO.getSprintName());
            if(byProjectEntityAndSprintName.isPresent())
                throw new ProjectException(ApplicationConstant.RESOURCE_ALREADY_EXIST,
                        "A sprint already exist with given sprint name");
            ProjectSprintEntity save = sprintRepository.save(
                    new ProjectSprintEntity(
                            projectById.get(),
                            addSprintRequestDTO.getSprintName(),
                            addSprintRequestDTO.getDescription(),
                            new Date(),
                            new Date(),
                            auth_user_admin.get(),
                            auth_user_admin.get(),
                            SprintStatusType.TODO
                    )
            );
            //created by
            CorporateEmployeeEntity createdBy = save.getAssignedBy();
            UserEntity userEntity = createdBy.getUserEntity();
            CorporateEmployeeDTO assignedCorporateEmployeeDTO = new CorporateEmployeeDTO(
                    createdBy.getId(),
                    new UserDTO(
                            userEntity.getId(),
                            userEntity.getRefNo(),
                            userEntity.getFirstName(),
                            userEntity.getLastName(),
                            userEntity.getContactNumber(),
                            userEntity.getEmail(),
                            null,
                            userEntity.getCreatedDate(),
                            userEntity.getStatusType()
                    ),
                    new CorporateDTO(
                            corporateEntity.getId(),
                            corporateEntity.getName(),
                            corporateEntity.getAddress(),
                            corporateEntity.getContactNumber1(),
                            corporateEntity.getContactNumber2(),
                            corporateEntity.getEmail(),
                            corporateEntity.getCorporateLogo(),
                            corporateEntity.getStatusType()
                    ),
                    createdBy.getCorporateAccessType(),
                    createdBy.getCreatedDate(),
                    createdBy.getModifiedDate(),
                    createdBy.getAcceptedDate(),
                    createdBy.getStatusType()
            );

            CorporateEmployeeEntity modifiedBy = save.getModifiedBy();
            userEntity = modifiedBy.getUserEntity();

            CorporateEmployeeDTO modifiedCorporateEmployeeDTO = new CorporateEmployeeDTO(
                    modifiedBy.getId(),
                    new UserDTO(
                            userEntity.getId(),
                            userEntity.getRefNo(),
                            userEntity.getFirstName(),
                            userEntity.getLastName(),
                            userEntity.getContactNumber(),
                            userEntity.getEmail(),
                            null,
                            userEntity.getCreatedDate(),
                            userEntity.getStatusType()
                    ),
                    new CorporateDTO(
                            corporateEntity.getId(),
                            corporateEntity.getName(),
                            corporateEntity.getAddress(),
                            corporateEntity.getContactNumber1(),
                            corporateEntity.getContactNumber2(),
                            corporateEntity.getEmail(),
                            corporateEntity.getCorporateLogo(),
                            corporateEntity.getStatusType()
                    ),
                    modifiedBy.getCorporateAccessType(),
                    modifiedBy.getCreatedDate(),
                    modifiedBy.getModifiedDate(),
                    modifiedBy.getAcceptedDate(),
                    modifiedBy.getStatusType()
            );

            return new SprintDTO(
                    save.getId(),
                    save.getProjectEntity().getId(),
                    save.getSprintName(),
                    save.getDescription(),
                    save.getCreatedDate(),
                    save.getModifiedDate(),
                    assignedCorporateEmployeeDTO,
                    modifiedCorporateEmployeeDTO,
                    save.getStatusType()
            );
        } catch (Exception e) {
            log.error("Method createSprint : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public SprintDTO updateSprint(UpdateSprintRequestDTO updateSprintRequestDTO) {
        log.info("Execute method updateSprint : updateSprintRequestDTO : " + updateSprintRequestDTO.toString());
        try {
            Optional<ProjectEntity> projectById = projectRepository.findById(updateSprintRequestDTO.getProjectId());
            if(!projectById.isPresent())
                throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "Project not found");
            ProjectEntity projectEntity = projectById.get();
            CorporateEntity corporateEntity = projectEntity.getCorporateEntity();
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
                    auth_user_admin.get().getCorporateAccessType().equals(CorporateAccessType.CORPORATE_ADMIN))) {
                Optional<ProjectMemberEntity> byProjectEntityAndCorporateEmployeeEntity =
                        projectMemberRepository.findByProjectEntityAndCorporateEmployeeEntity(
                                projectEntity,
                                auth_user_admin.get()
                        );
                if(!byProjectEntityAndCorporateEmployeeEntity.isPresent())
                    throw new CorporateException(
                            ApplicationConstant.UN_AUTH_ACTION,
                            "Unauthorized action. You can't processed this action"
                    );
                ProjectMemberEntity projectMemberEntity = byProjectEntityAndCorporateEmployeeEntity.get();
                if(!(projectMemberEntity.getScrumRole().equals(ScrumRoles.PRODUCT_OWNER) ||
                        projectMemberEntity.getScrumRole().equals(ScrumRoles.PRODUCT_OWNER)))
                    throw new CorporateException(
                            ApplicationConstant.UN_AUTH_ACTION,
                            "Unauthorized action. You can't processed this action"
                    );
            }

            Optional<ProjectSprintEntity> byId = sprintRepository.findById(updateSprintRequestDTO.getId());
            if(!byId.isPresent())
                throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "Sprint not found");

            Optional<ProjectSprintEntity> byProjectEntityAndSprintName =
                    sprintRepository.findByProjectEntityAndSprintName(projectById.get(), updateSprintRequestDTO.getSprintName());
            if(byProjectEntityAndSprintName.isPresent()) {
                if(byProjectEntityAndSprintName.get().getId()!=updateSprintRequestDTO.getId()) {
                    throw new ProjectException(ApplicationConstant.RESOURCE_ALREADY_EXIST,
                            "A sprint already exist with given sprint name");
                }
            }

            ProjectSprintEntity projectSprintEntity = byId.get();
            projectSprintEntity.setSprintName(updateSprintRequestDTO.getSprintName());
            projectSprintEntity.setDescription(updateSprintRequestDTO.getDescription());
            projectSprintEntity.setModifiedDate(new Date());
            projectSprintEntity.setModifiedBy(auth_user_admin.get());
            ProjectSprintEntity save = sprintRepository.save(projectSprintEntity);

            //created by
            CorporateEmployeeEntity createdBy = save.getAssignedBy();
            UserEntity userEntity = createdBy.getUserEntity();
            CorporateEmployeeDTO assignedCorporateEmployeeDTO = new CorporateEmployeeDTO(
                    createdBy.getId(),
                    new UserDTO(
                            userEntity.getId(),
                            userEntity.getRefNo(),
                            userEntity.getFirstName(),
                            userEntity.getLastName(),
                            userEntity.getContactNumber(),
                            userEntity.getEmail(),
                            null,
                            userEntity.getCreatedDate(),
                            userEntity.getStatusType()
                    ),
                    new CorporateDTO(
                            corporateEntity.getId(),
                            corporateEntity.getName(),
                            corporateEntity.getAddress(),
                            corporateEntity.getContactNumber1(),
                            corporateEntity.getContactNumber2(),
                            corporateEntity.getEmail(),
                            corporateEntity.getCorporateLogo(),
                            corporateEntity.getStatusType()
                    ),
                    createdBy.getCorporateAccessType(),
                    createdBy.getCreatedDate(),
                    createdBy.getModifiedDate(),
                    createdBy.getAcceptedDate(),
                    createdBy.getStatusType()
            );

            CorporateEmployeeEntity modifiedBy = save.getModifiedBy();
            userEntity = modifiedBy.getUserEntity();

            CorporateEmployeeDTO modifiedCorporateEmployeeDTO = new CorporateEmployeeDTO(
                    modifiedBy.getId(),
                    new UserDTO(
                            userEntity.getId(),
                            userEntity.getRefNo(),
                            userEntity.getFirstName(),
                            userEntity.getLastName(),
                            userEntity.getContactNumber(),
                            userEntity.getEmail(),
                            null,
                            userEntity.getCreatedDate(),
                            userEntity.getStatusType()
                    ),
                    new CorporateDTO(
                            corporateEntity.getId(),
                            corporateEntity.getName(),
                            corporateEntity.getAddress(),
                            corporateEntity.getContactNumber1(),
                            corporateEntity.getContactNumber2(),
                            corporateEntity.getEmail(),
                            corporateEntity.getCorporateLogo(),
                            corporateEntity.getStatusType()
                    ),
                    modifiedBy.getCorporateAccessType(),
                    modifiedBy.getCreatedDate(),
                    modifiedBy.getModifiedDate(),
                    modifiedBy.getAcceptedDate(),
                    modifiedBy.getStatusType()
            );

            return new SprintDTO(
                    save.getId(),
                    save.getProjectEntity().getId(),
                    save.getSprintName(),
                    save.getDescription(),
                    save.getCreatedDate(),
                    save.getModifiedDate(),
                    assignedCorporateEmployeeDTO,
                    modifiedCorporateEmployeeDTO,
                    save.getStatusType()
            );
        } catch (Exception e) {
            log.error("Method updateSprint : " + e.getMessage(), e);
            throw e;
        }
    }
}
