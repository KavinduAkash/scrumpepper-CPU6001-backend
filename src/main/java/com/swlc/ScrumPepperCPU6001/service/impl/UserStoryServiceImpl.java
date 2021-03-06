package com.swlc.ScrumPepperCPU6001.service.impl;

import com.swlc.ScrumPepperCPU6001.constant.ApplicationConstant;
import com.swlc.ScrumPepperCPU6001.dto.*;
import com.swlc.ScrumPepperCPU6001.dto.request.HandleUserStoryRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateUserStoryStatusRequestDTO;
import com.swlc.ScrumPepperCPU6001.entity.*;
import com.swlc.ScrumPepperCPU6001.enums.*;
import com.swlc.ScrumPepperCPU6001.exception.CorporateException;
import com.swlc.ScrumPepperCPU6001.exception.ProjectException;
import com.swlc.ScrumPepperCPU6001.repository.*;
import com.swlc.ScrumPepperCPU6001.service.TaskService;
import com.swlc.ScrumPepperCPU6001.service.UserStoryService;
import com.swlc.ScrumPepperCPU6001.util.TokenValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
public class UserStoryServiceImpl implements UserStoryService {
    private final UserStoryRepository userStoryRepository;
    private final ProjectRepository projectRepository;
    private final CorporateEmployeeRepository corporateEmployeeRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final UserStoryLabelRepository userStoryLabelRepository;
    private final ProjectUserStoryLabelRepository projectUserStoryLabelRepository;
    private final CorporateRepository corporateRepository;
    private final TaskService taskService;
    private final SprintRepository sprintRepository;
    private final ProjectSprintUserStoryRepository projectSprintUserStoryRepository;
    private final StoryPointsTrackRepository storyPointsTrackRepository;
    private final UserStoryTrackRepository userStoryTrackRepository;
    @Autowired
    private TokenValidator tokenValidator;

    public UserStoryServiceImpl(UserStoryRepository userStoryRepository, ProjectRepository projectRepository, CorporateEmployeeRepository corporateEmployeeRepository, ProjectMemberRepository projectMemberRepository, UserStoryLabelRepository userStoryLabelRepository, ProjectUserStoryLabelRepository projectUserStoryLabelRepository, CorporateRepository corporateRepository, TaskService taskService, SprintRepository sprintRepository, ProjectSprintUserStoryRepository projectSprintUserStoryRepository, StoryPointsTrackRepository storyPointsTrackRepository, UserStoryTrackRepository userStoryTrackRepository) {
        this.userStoryRepository = userStoryRepository;
        this.projectRepository = projectRepository;
        this.corporateEmployeeRepository = corporateEmployeeRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.userStoryLabelRepository = userStoryLabelRepository;
        this.projectUserStoryLabelRepository = projectUserStoryLabelRepository;
        this.corporateRepository = corporateRepository;
        this.taskService = taskService;
        this.sprintRepository = sprintRepository;
        this.projectSprintUserStoryRepository = projectSprintUserStoryRepository;
        this.storyPointsTrackRepository = storyPointsTrackRepository;
        this.userStoryTrackRepository = userStoryTrackRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UserStoryDTO handleUserStory(HandleUserStoryRequestDTO addUserStoryRequestDTO, long sprint_id) {
        log.info("============== Execute method createNewUserStory : addUserStoryRequestDTO : " + addUserStoryRequestDTO.toString());
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
                projectUserStoryEntity.setPriority(addUserStoryRequestDTO.getPriority());
                projectUserStoryEntity.setPoints(addUserStoryRequestDTO.getPoints());
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
                log.info("==============X Execute method createNewUserStory : ");
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
                                UserStoryStatusType.TODO,
                                addUserStoryRequestDTO.getPriority(),
                                addUserStoryRequestDTO.getPoints()
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

                if(sprint_id!=0) {
                    Optional<ProjectSprintEntity> sprintEntityOptional = sprintRepository.findById(sprint_id);
                    if(!sprintEntityOptional.isPresent())
                        throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "Sprint not found");
                    ProjectSprintUserStoryEntity projectSprintUserStoryEntity_new =
                            new ProjectSprintUserStoryEntity(
                                    save,
                                    new Date(),
                                    sprintEntityOptional.get(),
                                    SprintUserStoryStatus.ACTIVE,
                                    null
                            );
                    projectSprintUserStoryRepository.save(projectSprintUserStoryEntity_new);
                }

                  storyPointsTrackRepository.save(
                          new StoryPointsTrackEntity(
                                  save,
                                  save.getPoints(),
                                  new Date()
                          )
                  );

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
                projectUserStoryEntity.setStatusType(updateUserStoryStatusRequestDTO.getStatus());
                ProjectUserStoryEntity savedUserStory = userStoryRepository.save(projectUserStoryEntity);
                userStoryTrackRepository.save(new UserStoryTrackEntity(savedUserStory, savedUserStory.getStatusType(), new Date()));

            return true;
        } catch (Exception e) {
            log.error("Method updateUserStoryStatus : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<UserStoryDTO> getProjectBacklog(String ref, long corporateId) {
        log.info("Execute method getProjectBacklog : ref: " + ref + ", corporateId: " + corporateId);
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
            Optional<ProjectEntity> byProjectId = projectRepository.findByRef(ref);
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

    @Override
    public List<UserStoryDTO> getSprintUseStories(long id) {
        log.info("Execute method getSprintUseStories : sprint_id: " + id);
        try {

            Optional<ProjectSprintEntity> sprintById = sprintRepository.findById(id);
            if(!sprintById.isPresent())
                throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "Sprint not found");
            ProjectSprintEntity projectSprintEntity = sprintById.get();
            UserEntity userAdminEntity = tokenValidator.retrieveUserInformationFromAuthentication();

            CorporateEntity corporateEntity = projectSprintEntity.getProjectEntity().getCorporateEntity();

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

            CorporateEmployeeEntity corporateEmployeeEntity = auth_user_admin.get();
            Optional<ProjectMemberEntity> projectMemberOptional =
                    projectMemberRepository.findByProjectEntityAndCorporateEmployeeEntity(
                            sprintById.get().getProjectEntity(),
                            corporateEmployeeEntity
                    );

            if(!projectMemberOptional.isPresent())
                throw new ProjectException(ApplicationConstant.UN_AUTH_ACTION, "Access Denied");


            List<ProjectSprintUserStoryEntity> byUserStoriesBySprint = projectSprintUserStoryRepository.getByUserStoriesBySprint(sprintById.get().getId());


            List<UserStoryDTO> userStoryDTOS = new ArrayList<>();
            for (ProjectSprintUserStoryEntity projectSprintUserStoryEntity : byUserStoriesBySprint) {
                userStoryDTOS.add(this.prepareUserStoryDTO(projectSprintUserStoryEntity.getProjectUserStoryEntity()));
            }
            return userStoryDTOS;
        } catch (Exception e) {
            log.error("Method getProjectBacklog : " + e.getMessage(), e);
            throw e;
        }
    }

    public UserStoryDTO prepareUserStoryDTO(ProjectUserStoryEntity userStoryEntity) {
        log.info("Execute method prepareUserStoryDTO : @Param {} " + (userStoryEntity.getId()==199?userStoryEntity.getDescription():""));
        try {
            ProjectEntity p = userStoryEntity.getProjectEntity();
            List<UserStoryLblDTO> userStoryLblDTOS = prepareProjectUserStoryLblByUserStory(userStoryEntity, p);
            List<ProjectSprintUserStoryEntity> sprintUserStoryEntities = projectSprintUserStoryRepository.getByLatestSprintUserStoryRecord(userStoryEntity.getId());
            List<ProjectSprintEntity> otherSprints = new ArrayList<>();
            SprintDTO currentSprint = null;
            log.info("XXXXXXXXXXXXXXXX: " + sprintUserStoryEntities.size());
            if(!sprintUserStoryEntities.isEmpty()) {
                ProjectSprintUserStoryEntity projectSprintUserStoryEntity = sprintUserStoryEntities.get(0);
                SprintDTO sprintDTO = new SprintDTO();
                sprintDTO.setId(projectSprintUserStoryEntity.getProjectSprintEntity().getId());
                sprintDTO.setSprintName(projectSprintUserStoryEntity.getProjectSprintEntity().getSprintName());
                currentSprint = sprintDTO;
                otherSprints = sprintRepository.getOtherSprints(projectSprintUserStoryEntity.getProjectSprintEntity(), userStoryEntity.getProjectEntity());
            } else {
                otherSprints = sprintRepository.findByProjectEntity(userStoryEntity.getProjectEntity());
            }

            List<SprintDTO> sprintDTOList = new ArrayList<>();
            for (ProjectSprintEntity projectSprintEntity : otherSprints) {
                SprintDTO sprintDTO = new SprintDTO();
                sprintDTO.setId(projectSprintEntity.getId());
                sprintDTO.setSprintName(projectSprintEntity.getSprintName());
                sprintDTOList.add(sprintDTO);
            }

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
                            p.getStatusType(),
                            p.getRef()
                    ),
                    userStoryEntity.getTitle(),
                    userStoryEntity.getDescription(),
                    userStoryEntity.getCreatedDate(),
                    userStoryEntity.getModifiedDate(),
                    this.prepareCorporateEmployeeDTO(userStoryEntity.getCreatedBy()),
                    this.prepareCorporateEmployeeDTO(userStoryEntity.getModifiedBy()),
                    userStoryEntity.getStatusType(),
                    userStoryLblDTOS,
                    userStoryEntity.getPriority(),
                    taskService.getAllTasksOfProject(userStoryEntity.getId()),
                    sprintDTOList,
                    currentSprint,
                    userStoryEntity.getPoints()
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

    private List<UserStoryLblDTO> prepareProjectUserStoryLblByUserStory(ProjectUserStoryEntity projectUserStoryEntity, ProjectEntity projectEntity) {
        log.info("Execute method prepareProjectUserStoryLblByUserStory : Params{} " + projectUserStoryEntity);
        try {
            List<ProjectUserStoryLabelEntity> userStoryLabelsByUserStoryId =
                    projectUserStoryLabelRepository.getUserStoryLabelsByUserStoryId(projectUserStoryEntity);
            //create project dto
            ProjectDTO projectDTO = new ProjectDTO(
                    projectEntity.getId(),
                    null,
                    projectEntity.getProjectName(),
                    null,
                    projectEntity.getModifiedDate(),
                    null,
                    null,
                    projectEntity.getStatusType(),
                    projectEntity.getRef()
            );

            //prepare project user story lbl return list
            List<UserStoryLblDTO> lbl_list =  new ArrayList<>();
            for (ProjectUserStoryLabelEntity lbl : userStoryLabelsByUserStoryId) {
                lbl_list.add(new UserStoryLblDTO(lbl.getId(), projectDTO, lbl.getUserStoryLabelEntity().getLabel()));
            }
            return lbl_list;
        } catch (Exception e) {
            log.error("Method prepareProjectUserStoryLbl : " + e.getMessage(), e);
            throw e;
        }
    }
}
