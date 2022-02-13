package com.swlc.ScrumPepperCPU6001.service.impl;

import com.swlc.ScrumPepperCPU6001.constant.ApplicationConstant;
import com.swlc.ScrumPepperCPU6001.dto.*;
import com.swlc.ScrumPepperCPU6001.dto.request.AddSprintRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.MoveUserStoryRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateSprintRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.SprintResponseDTO;
import com.swlc.ScrumPepperCPU6001.entity.*;
import com.swlc.ScrumPepperCPU6001.enums.*;
import com.swlc.ScrumPepperCPU6001.exception.CorporateException;
import com.swlc.ScrumPepperCPU6001.exception.ProjectException;
import com.swlc.ScrumPepperCPU6001.repository.*;
import com.swlc.ScrumPepperCPU6001.service.SprintService;
import com.swlc.ScrumPepperCPU6001.util.TokenValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private final ProjectSprintUserStoryRepository projectSprintUserStoryRepository;
    private final UserStoryServiceImpl userStoryServiceImpl;
    @Autowired
    private TokenValidator tokenValidator;


    public SprintServiceImpl(ProjectRepository projectRepository, SprintRepository sprintRepository,
                             UserStoryRepository userStoryRepository, CorporateEmployeeRepository corporateEmployeeRepository,
                             ProjectMemberRepository projectMemberRepository, UserStoryLabelRepository userStoryLabelRepository,
                             ProjectUserStoryLabelRepository projectUserStoryLabelRepository, ProjectSprintUserStoryRepository projectSprintUserStoryRepository, UserStoryServiceImpl userStoryServiceImpl) {
        this.projectRepository = projectRepository;
        this.sprintRepository = sprintRepository;
        this.userStoryRepository = userStoryRepository;
        this.corporateEmployeeRepository = corporateEmployeeRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.userStoryLabelRepository = userStoryLabelRepository;
        this.projectUserStoryLabelRepository = projectUserStoryLabelRepository;
        this.projectSprintUserStoryRepository = projectSprintUserStoryRepository;
        this.userStoryServiceImpl = userStoryServiceImpl;
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
            SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDate = formatter.parse(addSprintRequestDTO.getStartDate() + " 00:00:00");
            Date endDate = formatter.parse(addSprintRequestDTO.getEndDate() + " 23:59:59");
            ProjectSprintEntity save = sprintRepository.save(
                    new ProjectSprintEntity(
                            projectById.get(),
                            addSprintRequestDTO.getSprintName(),
                            addSprintRequestDTO.getDescription(),
                            startDate,
                            endDate,
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
        } catch (ParseException ex) {
            log.error("Method createSprint : " + ex.getMessage(), ex);
            throw new ProjectException(ApplicationConstant.COMMON_ERROR_CODE, "Something went wrong");
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

            SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDate = formatter.parse(updateSprintRequestDTO.getStartDate() + " 00:00:00");
            Date endDate = formatter.parse(updateSprintRequestDTO.getEndDate() + " 23:59:59");

            ProjectSprintEntity projectSprintEntity = byId.get();
            projectSprintEntity.setSprintName(updateSprintRequestDTO.getSprintName());
            projectSprintEntity.setDescription(updateSprintRequestDTO.getDescription());
            projectSprintEntity.setStartDate(startDate);
            projectSprintEntity.setEndDate(endDate);
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
        } catch (ParseException ex) {
            log.error("Method updateSprint : " + ex.getMessage(), ex);
            throw new ProjectException(ApplicationConstant.COMMON_ERROR_CODE, "Something went wrong");
        } catch (Exception e) {
            log.error("Method updateSprint : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<SprintResponseDTO> getProjectSprints(long projectId) {
        log.info("Execute method getProjectSprints : projectId : " + projectId);
        try {
            Optional<ProjectEntity> projectOptional = projectRepository.findById(projectId);
            if(!projectOptional.isPresent())
                throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "Project not found");
            ProjectEntity projectEntity = projectOptional.get();
            List<ProjectSprintEntity> sprintEntities = sprintRepository.findByProjectEntity(projectEntity);
            return this.prepareSprintResponseDTO(sprintEntities);
        } catch (Exception e) {
            log.error("Method getProjectSprints : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean userStoryMove(MoveUserStoryRequestDTO moveUserStoryRequestDTO) {
        log.info("Execute method userStoryMove : Param{} " + moveUserStoryRequestDTO.toString());
        try {

            Optional<ProjectUserStoryEntity> userStoryById =
                    userStoryRepository.findById(moveUserStoryRequestDTO.getUserStoryId());
            if(!userStoryById.isPresent())
                throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "User story not found");

            ProjectEntity projectEntity = userStoryById.get().getProjectEntity();
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
            }

            Optional<ProjectSprintEntity> sprintById = sprintRepository.findById(moveUserStoryRequestDTO.getSprintId());
            if(!sprintById.isPresent())
                throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "sprint not found");

            List<ProjectSprintUserStoryEntity> byLatestSprintUserStoryRecord =
                    projectSprintUserStoryRepository.getByLatestSprintUserStoryRecord(moveUserStoryRequestDTO.getUserStoryId());

            if(!byLatestSprintUserStoryRecord.isEmpty()) {
                ProjectSprintUserStoryEntity projectSprintUserStoryEntity_old = byLatestSprintUserStoryRecord.get(0);
                projectSprintUserStoryEntity_old.setStatus(SprintUserStoryStatus.INACTIVE);
                projectSprintUserStoryEntity_old.setRemovedDate(new Date());
                projectSprintUserStoryRepository.save(projectSprintUserStoryEntity_old);
                ProjectSprintUserStoryEntity projectSprintUserStoryEntity_new =
                        new ProjectSprintUserStoryEntity(
                                projectSprintUserStoryEntity_old.getProjectUserStoryEntity(),
                                new Date(),
                                sprintById.get(),
                                SprintUserStoryStatus.ACTIVE,
                                null
                        );
                projectSprintUserStoryRepository.save(projectSprintUserStoryEntity_new);
                return true;
            } else {
                ProjectSprintUserStoryEntity projectSprintUserStoryEntity_new =
                        new ProjectSprintUserStoryEntity(
                                userStoryById.get(),
                                new Date(),
                                sprintById.get(),
                                SprintUserStoryStatus.ACTIVE,
                                null
                        );
                projectSprintUserStoryRepository.save(projectSprintUserStoryEntity_new);
                return true;
            }
        } catch (Exception e) {
            log.error("Method userStoryMove : " + e.getMessage(), e);
            throw e;
        }

    }

    private List<SprintResponseDTO> prepareSprintResponseDTO(List<ProjectSprintEntity> sprintEntities) {
        try {
            List<SprintResponseDTO> sprintResponseDTOS = new ArrayList<>();
            for (ProjectSprintEntity sprint : sprintEntities) {
                sprintResponseDTOS.add(prepareSprintResponseDTO(sprint));
            }
            return sprintResponseDTOS;
        } catch (Exception e) {
            throw e;
        }
    }

    private SprintResponseDTO prepareSprintResponseDTO(ProjectSprintEntity sprintEntity) {
        try {
            List<UserStoryDTO> userStoryDTOS = new ArrayList<>();
            List<ProjectSprintUserStoryEntity> sprintUserStoryEntities = projectSprintUserStoryRepository.getByUserStoriesBySprint(sprintEntity.getId());
            for (ProjectSprintUserStoryEntity projectSprintUserStoryEntity : sprintUserStoryEntities) {
                userStoryDTOS.add(userStoryServiceImpl.prepareUserStoryDTO(projectSprintUserStoryEntity.getProjectUserStoryEntity()));
            }
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startDate = dateFormat.format(sprintEntity.getStartDate());
            String endDate = dateFormat.format(sprintEntity.getEndDate());
            return new SprintResponseDTO(
                    sprintEntity.getId(),
                    sprintEntity.getProjectEntity().getId(),
                    sprintEntity.getSprintName(),
                    sprintEntity.getDescription(),
                    startDate,
                    endDate,
                    sprintEntity.getCreatedDate(),
                    sprintEntity.getModifiedDate(),
                    this.prepareCorporateEmployeeDTO(sprintEntity.getAssignedBy()),
                    this.prepareCorporateEmployeeDTO(sprintEntity.getModifiedBy()),
                    sprintEntity.getStatusType(),
                    userStoryDTOS
            );
        } catch (Exception e) {
            throw e;
        }
    }

    private CorporateEmployeeDTO prepareCorporateEmployeeDTO(CorporateEmployeeEntity corporateEmployeeEntity) {
        try {
            CorporateEntity corporateEntity = corporateEmployeeEntity.getCorporateEntity();
            UserEntity userEntity = corporateEmployeeEntity.getUserEntity();
            return new CorporateEmployeeDTO(
                    corporateEmployeeEntity.getId(),
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
                    corporateEmployeeEntity.getCorporateAccessType(),
                    corporateEmployeeEntity.getCreatedDate(),
                    corporateEmployeeEntity.getModifiedDate(),
                    corporateEmployeeEntity.getAcceptedDate(),
                    corporateEmployeeEntity.getStatusType()
            );
        } catch (Exception e) {
            throw e;
        }
    }

}
