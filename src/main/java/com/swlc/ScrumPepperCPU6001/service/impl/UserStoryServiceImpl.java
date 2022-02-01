package com.swlc.ScrumPepperCPU6001.service.impl;

import com.swlc.ScrumPepperCPU6001.constant.ApplicationConstant;
import com.swlc.ScrumPepperCPU6001.dto.CorporateEmployeeDTO;
import com.swlc.ScrumPepperCPU6001.dto.ProjectDTO;
import com.swlc.ScrumPepperCPU6001.dto.UserDTO;
import com.swlc.ScrumPepperCPU6001.dto.UserStoryDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.HandleUserStoryRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateUserStoryStatusRequestDTO;
import com.swlc.ScrumPepperCPU6001.entity.*;
import com.swlc.ScrumPepperCPU6001.enums.CorporateAccessStatusType;
import com.swlc.ScrumPepperCPU6001.enums.CorporateAccessType;
import com.swlc.ScrumPepperCPU6001.enums.ScrumRoles;
import com.swlc.ScrumPepperCPU6001.enums.UserStoryStatusType;
import com.swlc.ScrumPepperCPU6001.exception.CorporateException;
import com.swlc.ScrumPepperCPU6001.exception.ProjectException;
import com.swlc.ScrumPepperCPU6001.repository.*;
import com.swlc.ScrumPepperCPU6001.service.UserStoryService;
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
public class UserStoryServiceImpl implements UserStoryService {
    private final UserStoryRepository userStoryRepository;
    private final ProjectRepository projectRepository;
    private final CorporateEmployeeRepository corporateEmployeeRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserStoryLabelRepository userStoryLabelRepository;
    private final ProjectUserStoryLabelRepository projectUserStoryLabelRepository;
    private final CorporateRepository corporateRepository;
    @Autowired
    private TokenValidator tokenValidator;

    public UserStoryServiceImpl(UserStoryRepository userStoryRepository, ProjectRepository projectRepository, CorporateEmployeeRepository corporateEmployeeRepository, ProjectMemberRepository projectMemberRepository, UserStoryLabelRepository userStoryLabelRepository, ProjectUserStoryLabelRepository projectUserStoryLabelRepository, CorporateRepository corporateRepository) {
        this.userStoryRepository = userStoryRepository;
        this.projectRepository = projectRepository;
        this.corporateEmployeeRepository = corporateEmployeeRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.userStoryLabelRepository = userStoryLabelRepository;
        this.projectUserStoryLabelRepository = projectUserStoryLabelRepository;
        this.corporateRepository = corporateRepository;
    }

    @Override
    public UserStoryDTO handleUserStory(HandleUserStoryRequestDTO addUserStoryRequestDTO) {
        log.info("Execute method createNewUserStory : addUserStoryRequestDTO : " + addUserStoryRequestDTO.toString());
        try {
            Optional<ProjectEntity> projectById = projectRepository.findById(addUserStoryRequestDTO.getProjectId());
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

            long user_story_id = addUserStoryRequestDTO.getUserStoryId();

            if(addUserStoryRequestDTO.getUserStoryId()!=0) {
                Optional<ProjectUserStoryEntity> byId =
                        userStoryRepository.findById(addUserStoryRequestDTO.getUserStoryId());
                if(!byId.isPresent())
                    throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "User story not found");
                ProjectUserStoryEntity projectUserStoryEntity = byId.get();
                projectUserStoryEntity.setTitle(addUserStoryRequestDTO.getTitle());
                projectUserStoryEntity.setDescription(addUserStoryRequestDTO.getDescription());
                projectUserStoryEntity.setModifiedDate(new Date());
                projectUserStoryEntity.setModifiedBy(auth_user_admin.get());
                ProjectUserStoryEntity save = userStoryRepository.save(projectUserStoryEntity);

                List<String> userStoryLabels = addUserStoryRequestDTO.getUserStoryLabels();
                List<ProjectUserStoryLabelEntity> userStoryLabelIdsByUserStoryId = projectUserStoryLabelRepository.getUserStoryLabelsByUserStoryId(save);

                List<String> userStoryLabelSave = new ArrayList<>();
                List<ProjectUserStoryLabelEntity> userStoryLabelDelete = new ArrayList<>();

                for (String lbl : userStoryLabels) {
                    boolean alreadyExist = false;
                    for (int i = 0; i<userStoryLabelIdsByUserStoryId.size(); i++) {
                        if((userStoryLabelIdsByUserStoryId.get(i).getUserStoryLabelEntity().getLabel().toUpperCase()).equals(lbl.toUpperCase())) {
                            alreadyExist = true;
                        }
                    }
                    if(!alreadyExist) {
                        userStoryLabelSave.add(lbl);
                    }
                }

                for (ProjectUserStoryLabelEntity projectUserStoryLabelEntity : userStoryLabelIdsByUserStoryId) {
                    boolean alreadyExist = false;
                    for(int j = 0; j<userStoryLabels.size(); j++) {
                        if((userStoryLabels.get(j).toUpperCase()).equals(projectUserStoryLabelEntity.getUserStoryLabelEntity().getLabel())) {
                            alreadyExist = true;
                        }
                    }
                    if(!alreadyExist) {
                        userStoryLabelDelete.add(projectUserStoryLabelEntity);
                    }
                }

                if(!userStoryLabelDelete.isEmpty()) {
                    projectUserStoryLabelRepository.deleteAll(userStoryLabelDelete);
                }
                if(!userStoryLabelSave.isEmpty()) {
                    List<UserStoryLabelEntity> userStoryLabelEntities = new ArrayList<>();
                    for (String userStoryLbl : userStoryLabelSave) {
                        userStoryLabelEntities.add(new UserStoryLabelEntity(projectEntity, userStoryLbl));
                    }
                    List<UserStoryLabelEntity> userStoryLabelEntities1 = userStoryLabelRepository.saveAll(userStoryLabelEntities);
                    List<ProjectUserStoryLabelEntity> projectUserStories =  new ArrayList<>();
                    for (UserStoryLabelEntity u : userStoryLabelEntities1) {
                        projectUserStories.add(new ProjectUserStoryLabelEntity(save, u));
                    }
                    projectUserStoryLabelRepository.saveAll(projectUserStories);
                }

            } else {
                ProjectUserStoryEntity save = userStoryRepository.save(
                        new ProjectUserStoryEntity(
                                projectEntity,
                                addUserStoryRequestDTO.getTitle(),
                                addUserStoryRequestDTO.getDescription(),
                                new Date(),
                                new Date(),
                                auth_user_admin.get(),
                                auth_user_admin.get(),
                                UserStoryStatusType.TODO
                        )
                );

                List<ProjectUserStoryLabelEntity> projectUserStoryLabelEntityList =  new ArrayList<>();
                List<String> userStoryLabels = addUserStoryRequestDTO.getUserStoryLabels();

                List<UserStoryLabelEntity> byId = userStoryLabelRepository.findByProjectEntity(projectEntity);


                for (String lbl : userStoryLabels) {
                    for (UserStoryLabelEntity userStoryLabelEntity : byId) {
                        boolean alreadyExist = false;
                        if((userStoryLabelEntity.getLabel().toUpperCase()).equals(lbl)) {
                            alreadyExist= true;
                        }
                        if(!alreadyExist) {
                            UserStoryLabelEntity save1 = userStoryLabelRepository.save(new UserStoryLabelEntity(projectEntity, lbl));
                            projectUserStoryLabelEntityList.add(new ProjectUserStoryLabelEntity(save, save1));
                        } else {
                            projectUserStoryLabelEntityList.add(new ProjectUserStoryLabelEntity(save, userStoryLabelEntity));
                        }
                    }
                }

                projectUserStoryLabelRepository.saveAll(projectUserStoryLabelEntityList);
                user_story_id = save.getId();
            }

            Optional<ProjectUserStoryEntity> byId = userStoryRepository.findById(user_story_id);
            ProjectUserStoryEntity projectUserStoryEntity = byId.get();
            UserStoryDTO userStoryDTO = this.prepareUserStoryDTO(projectUserStoryEntity);
            return userStoryDTO;
        } catch (Exception e) {
            log.error("Method createNewUserStory : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean updateUserStoryStatus(UpdateUserStoryStatusRequestDTO updateUserStoryStatusRequestDTO) {
        log.info("Execute method updateUserStoryStatus : updateUserStoryStatusRequestDTO : " + updateUserStoryStatusRequestDTO.toString());
        try {
            UserEntity userAdminEntity = tokenValidator.retrieveUserInformationFromAuthentication();
            Optional<ProjectUserStoryEntity> byId = userStoryRepository.findById(updateUserStoryStatusRequestDTO.getUserStoryId());
            if(!byId.isPresent())
                throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "User story not found");
            ProjectUserStoryEntity projectUserStoryEntity = byId.get();
            if(projectUserStoryEntity.getStatusType().equals(UserStoryStatusType.DELETE))
                throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "User story not found");
            projectUserStoryEntity.setStatusType(updateUserStoryStatusRequestDTO.getStatus());
            ProjectEntity projectEntity = projectUserStoryEntity.getProjectEntity();
            CorporateEntity corporateEntity = projectEntity.getCorporateEntity();

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

            if(updateUserStoryStatusRequestDTO.getStatus().equals(UserStoryStatusType.DELETE)) {
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
            } else {
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

            }
            projectUserStoryEntity.setStatusType(updateUserStoryStatusRequestDTO.getStatus());
            userStoryRepository.save(projectUserStoryEntity);
            return true;
        } catch (Exception e) {
            log.error("Method updateUserStoryStatus : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<UserStoryDTO> getProjectBacklog(long id, long corporateId) {
        log.info("Execute method getProjectBacklog : id: " + id + ", corporateId: " + corporateId);
        try {
            UserEntity userAdminEntity = tokenValidator.retrieveUserInformationFromAuthentication();
            Optional<CorporateEntity> byCorporateId = corporateRepository.findById(corporateId);
            if(!byCorporateId.isPresent())
                throw new CorporateException(ApplicationConstant.RESOURCE_NOT_FOUND, "Corporate not found");



            Optional<CorporateEmployeeEntity> auth_user_admin =
                    corporateEmployeeRepository.findByUserEntityAndCorporateEntityAndStatusType(
                            userAdminEntity,
                            byCorporateId.get(),
                            CorporateAccessStatusType.ACTIVE
                    );
            if(!auth_user_admin.isPresent())
                throw new CorporateException(
                        ApplicationConstant.UN_AUTH_ACTION,
                        "Unauthorized action. You can't processed this action"
                );

            CorporateEmployeeEntity corporateEmployeeEntity = auth_user_admin.get();
            Optional<ProjectEntity> byProjectId = projectRepository.findById(id);
            if(!byProjectId.isPresent())
                throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "Project not found");

            Optional<ProjectMemberEntity> projectMemberOptional =
                    projectMemberRepository.findByProjectEntityAndCorporateEmployeeEntity(
                            byProjectId.get(),
                            corporateEmployeeEntity
                    );

            if(!projectMemberOptional.isPresent())
                throw new ProjectException(ApplicationConstant.UN_AUTH_ACTION, "Access Denied");

            List<ProjectUserStoryEntity> byProjectEntity = userStoryRepository.findByProjectEntity(byProjectId.get());
            List<UserStoryDTO> userStoryDTOS = new ArrayList<>();
            for (ProjectUserStoryEntity projectUserStoryEntity : byProjectEntity) {
                userStoryDTOS.add(this.prepareUserStoryDTO(projectUserStoryEntity));
            }
            return userStoryDTOS;
        } catch (Exception e) {
            log.error("Method getProjectBacklog : " + e.getMessage(), e);
            throw e;
        }
    }

    private UserStoryDTO prepareUserStoryDTO(ProjectUserStoryEntity userStoryEntity) {
        log.info("Execute method prepareUserStoryDTO : @Param {} " + (userStoryEntity.getId()==199?userStoryEntity.getDescription():""));
        try {
            ProjectEntity p = userStoryEntity.getProjectEntity();
            return new UserStoryDTO(
                    userStoryEntity.getId(),
                    new ProjectDTO(
                            p.getId(),
                            null,
                            p.getProjectName(),
                            p.getCreatedDate(),
                            p.getModifiedDate(),
                            this.prepareCorporateEmployeeDTO(p.getCreated_CorporateEmployeeEntity()),
                            this.prepareCorporateEmployeeDTO(p.getModified_CorporateEmployeeEntity()),
                            p.getStatusType()
                    ),
                    userStoryEntity.getTitle(),
                    userStoryEntity.getDescription(),
                    userStoryEntity.getCreatedDate(),
                    userStoryEntity.getModifiedDate(),
                    this.prepareCorporateEmployeeDTO(userStoryEntity.getCreatedBy()),
                    this.prepareCorporateEmployeeDTO(userStoryEntity.getModifiedBy()),
                    userStoryEntity.getStatusType()
            );

        } catch (Exception e) {
            log.error("Method prepareUserStoryDTO : " + e.getMessage(), e);
            throw e;
        }
    }


    private CorporateEmployeeDTO prepareCorporateEmployeeDTO(CorporateEmployeeEntity c) {
//        log.info("Execute method prepareCorporateEmployeeDTO : @Param {} " + c);
        try {
                return new CorporateEmployeeDTO(
                        c.getId(),
                        new UserDTO(
                                c.getUserEntity().getId(),
                                c.getUserEntity().getRefNo(),
                                c.getUserEntity().getFirstName(),
                                c.getUserEntity().getLastName(),
                                c.getUserEntity().getContactNumber(),
                                c.getUserEntity().getEmail(),
                                null,
                                c.getUserEntity().getCreatedDate(),
                                c.getUserEntity().getStatusType()
                        ),
                        null,
                        c.getCorporateAccessType(),
                        c.getCreatedDate(),
                        c.getModifiedDate(),
                        c.getAcceptedDate(),
                        c.getStatusType()
                );
        } catch (Exception e) {
            log.error("Method prepareUserStoryDTO : " + e.getMessage(), e);
            throw e;
        }
    }
}
