package com.swlc.ScrumPepperCPU6001.service.impl;

import com.swlc.ScrumPepperCPU6001.constant.ApplicationConstant;
import com.swlc.ScrumPepperCPU6001.dto.*;
import com.swlc.ScrumPepperCPU6001.dto.request.AddProjectTaskRequestDTO;
import com.swlc.ScrumPepperCPU6001.entity.*;
import com.swlc.ScrumPepperCPU6001.enums.*;
import com.swlc.ScrumPepperCPU6001.exception.CorporateException;
import com.swlc.ScrumPepperCPU6001.exception.ProjectException;
import com.swlc.ScrumPepperCPU6001.repository.*;
import com.swlc.ScrumPepperCPU6001.service.TaskService;
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
@Log4j2
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class TasksServiceImpl implements TaskService {

    private final UserStoryRepository userStoryRepository;
    private final ProjectRepository projectRepository;
    private final CorporateEmployeeRepository corporateEmployeeRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectTaskRepository projectTaskRepository;
    private final ProjectTaskAssignsRepository projectTaskAssignsRepository;
    private final UserStoryLabelRepository userStoryLabelRepository;
    private final ProjectUserStoryLabelRepository projectUserStoryLabelRepository;
    private final CorporateRepository corporateRepository;
    private final SprintRepository sprintRepository;
    private final ProjectSprintUserStoryRepository projectSprintUserStoryRepository;
    private final TaskTrackRepository taskTrackRepository;
    private final UserStoryTrackRepository userStoryTrackRepository;
    @Autowired
    private TokenValidator tokenValidator;

    public TasksServiceImpl(UserStoryRepository userStoryRepository,
                            ProjectRepository projectRepository,
                            CorporateEmployeeRepository corporateEmployeeRepository,
                            ProjectMemberRepository projectMemberRepository,
                            ProjectTaskRepository projectTaskRepository,
                            ProjectTaskAssignsRepository projectTaskAssignsRepository,
                            UserStoryLabelRepository userStoryLabelRepository,
                            ProjectUserStoryLabelRepository projectUserStoryLabelRepository,
                            CorporateRepository corporateRepository,
                            SprintRepository sprintRepository,
                            ProjectSprintUserStoryRepository projectSprintUserStoryRepository, TaskTrackRepository taskTrackRepository, UserStoryTrackRepository userStoryTrackRepository) {
        this.userStoryRepository = userStoryRepository;
        this.projectRepository = projectRepository;
        this.corporateEmployeeRepository = corporateEmployeeRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.projectTaskRepository = projectTaskRepository;
        this.projectTaskAssignsRepository = projectTaskAssignsRepository;
        this.userStoryLabelRepository = userStoryLabelRepository;
        this.projectUserStoryLabelRepository = projectUserStoryLabelRepository;
        this.corporateRepository = corporateRepository;
        this.sprintRepository = sprintRepository;
        this.projectSprintUserStoryRepository = projectSprintUserStoryRepository;
        this.taskTrackRepository = taskTrackRepository;
        this.userStoryTrackRepository = userStoryTrackRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public TaskDTO createNewTask(AddProjectTaskRequestDTO task) {
        log.info("Execute method createNewTask : email : " + task.toString());
        try {
            Optional<ProjectUserStoryEntity> projectUserStoryById = userStoryRepository.findById(task.getUserStoryId());
            if(!projectUserStoryById.isPresent())
                throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "User story not found");
            ProjectEntity projectEntity = projectUserStoryById.get().getProjectEntity();
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

            ProjectTaskEntity save = null;

            if(task.getTaskId()!=0) {

                Optional<ProjectTaskEntity> taskEntity = projectTaskRepository.findById(task.getTaskId());
                if(!taskEntity.isPresent())
                    throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "Task not found");
                ProjectTaskEntity projectTaskEntity = taskEntity.get();
                projectTaskEntity.setTitle(task.getTitle());
                projectTaskEntity.setStatusType(task.getStatusType());
                save = projectTaskRepository.save(projectTaskEntity);
                taskTrackRepository.save(new TaskTrackEntity(save, save.getStatusType(), new Date()));
            } else {
                save = projectTaskRepository.save(
                        new ProjectTaskEntity(
                                projectUserStoryById.get(),
                                task.getTitle(),
                                new Date(),
                                auth_user_admin.get(),
                                auth_user_admin.get(),
                                task.getStatusType()
                        )
                );
            }
            return this.prepareTaskDTO(save);
        } catch (Exception e) {
            log.error("Method createNewTask : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public TaskDTO addMemberToTask(long taskId, long id) {
        log.info("Execute method addMemberToTask : id : " + id);
        try {
            Optional<ProjectTaskEntity> projectTaskById = projectTaskRepository.findById(taskId);
            if(!projectTaskById.isPresent())
                throw new CorporateException(
                        ApplicationConstant.RESOURCE_NOT_FOUND,
                        "Task not found"
                );
            ProjectUserStoryEntity projectUserStoryEntity = projectTaskById.get().getProjectUserStoryEntity();
            ProjectEntity projectEntity = projectUserStoryEntity.getProjectEntity();
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

            Optional<CorporateEmployeeEntity> corporateEmployeeById = corporateEmployeeRepository.findById(id);
            if (corporateEmployeeById.get().getCorporateEntity().getId()!=corporateEntity.getId() || !corporateEmployeeById.get().getStatusType().equals(CorporateAccessStatusType.ACTIVE))
                throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "Employee not found");

            Optional<ProjectMemberEntity> projectMemberEntity = projectMemberRepository.findByCorporateEmployeeEntityAndProjectEntity(corporateEmployeeById.get(), projectEntity);
            ProjectMemberEntity projectMember = null;
            if(!projectMemberEntity.isPresent()) {
                projectMember = projectMemberRepository.save(new ProjectMemberEntity(projectEntity, corporateEmployeeById.get(), new Date(), new Date(), auth_user_admin.get(), auth_user_admin.get(), ScrumRoles.TEAM_MEMBER, ProjectMemberStatusType.ACTIVE));
            } else {
                projectMember = projectMemberEntity.get();
            }

            ProjectTaskAssignsEntity save = projectTaskAssignsRepository.save(new ProjectTaskAssignsEntity(projectTaskById.get(), projectMember, new Date(), new Date(), auth_user_admin.get(), auth_user_admin.get(), StatusType.ACTIVE));
            Optional<ProjectTaskEntity> updatedTaskEntity = projectTaskRepository.findById(taskId);

            return this.prepareTaskDTO(updatedTaskEntity.get());
        } catch (Exception e) {
            log.error("Method addMemberToTask : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public TaskDTO removedMemberFromTask(long taskId, long id) {
        try {
            Optional<ProjectTaskEntity> projectTaskById = projectTaskRepository.findById(taskId);
            if(!projectTaskById.isPresent())
                throw new CorporateException(
                        ApplicationConstant.RESOURCE_NOT_FOUND,
                        "Task not found"
                );
            ProjectUserStoryEntity projectUserStoryEntity = projectTaskById.get().getProjectUserStoryEntity();
            ProjectEntity projectEntity = projectUserStoryEntity.getProjectEntity();
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

            Optional<CorporateEmployeeEntity> corporateEmployeeById = corporateEmployeeRepository.findById(id);
            if (corporateEmployeeById.get().getCorporateEntity().getId()!=corporateEntity.getId() || !corporateEmployeeById.get().getStatusType().equals(CorporateAccessStatusType.ACTIVE))
                throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "Employee not found");

            Optional<ProjectMemberEntity> projectMemberEntity = projectMemberRepository.findByCorporateEmployeeEntityAndProjectEntity(corporateEmployeeById.get(), projectEntity);
            ProjectMemberEntity projectMemberEntity1 = projectMemberEntity.get();

            Optional<ProjectTaskAssignsEntity> byProjectTaskEntityAndProjectMemberEntity = projectTaskAssignsRepository.findByProjectTaskEntityAndProjectMemberEntity(projectTaskById.get(), projectMemberEntity1);
            ProjectTaskAssignsEntity projectTaskAssignsEntity = byProjectTaskEntityAndProjectMemberEntity.get();
            projectTaskAssignsEntity.setStatusType(StatusType.DELETE);

            projectTaskAssignsRepository.save(projectTaskAssignsEntity);

            Optional<ProjectTaskEntity> updatedTaskEntity = projectTaskRepository.findById(taskId);
            return this.prepareTaskDTO(updatedTaskEntity.get());
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<TaskDTO> getAllTasksOfProject(long userStoryId) {
        log.info("Execute method getAllTasksOfProject : userStoryId : " + userStoryId);
        try {
            Optional<ProjectUserStoryEntity> projectUserStoryOptional = userStoryRepository.findById(userStoryId);
            if(!projectUserStoryOptional.isPresent())
                throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "User story not found");
            return this.getAllTasks(projectUserStoryOptional.get());
        } catch (Exception e) {
            log.error("Method getAllTasksOfProject : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<TaskDTO> changeTaskStatus(long taskId, String status) {
        log.info("Execute method changeTaskStatus: ");
        try {
            Optional<ProjectTaskEntity> projectTaskById = projectTaskRepository.findById(taskId);
            if(!projectTaskById.isPresent())
                throw new CorporateException(
                        ApplicationConstant.RESOURCE_NOT_FOUND,
                        "Task not found"
                );
            ProjectUserStoryEntity projectUserStoryEntity = projectTaskById.get().getProjectUserStoryEntity();
            ProjectEntity projectEntity = projectUserStoryEntity.getProjectEntity();
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
            ProjectTaskEntity task = projectTaskById.get();
            switch (status) {
                case "TODO":
                    task.setStatusType(UserStoryStatusType.TODO);
                    break;
                case "PROCESSING":
                    task.setStatusType(UserStoryStatusType.PROCESSING);
                    break;
                case "COMPLETED":
                    task.setStatusType(UserStoryStatusType.COMPLETED);
                    break;
                default:
                    break;
            }
            ProjectTaskEntity savedTask = projectTaskRepository.save(task);
            taskTrackRepository.save(new TaskTrackEntity(savedTask, savedTask.getStatusType(), new Date()));
            UserStoryStatusType statusType = projectUserStoryEntity.getStatusType();
            if(!statusType.equals(UserStoryStatusType.PROCESSING)) {
                projectUserStoryEntity.setStatusType(UserStoryStatusType.PROCESSING);
                ProjectUserStoryEntity savedUserStory = userStoryRepository.save(projectUserStoryEntity);
                userStoryTrackRepository.save(new UserStoryTrackEntity(savedUserStory, savedUserStory.getStatusType(), new Date()));
            }
            return this.getAllTasks(savedTask.getProjectUserStoryEntity());
        } catch (Exception e) {
            log.error("Method changeTaskStatus : " + e.getMessage(), e);
            throw e;
        }
    }

    private List<TaskDTO> getAllTasks(ProjectUserStoryEntity userStoryEntity) {
        try {
            List<ProjectTaskEntity> allByProjectUserStoryEntity = projectTaskRepository.findAllByProjectUserStoryEntity(userStoryEntity);
            return this.prepareTaskDTOList(allByProjectUserStoryEntity);
        } catch (Exception e) {
            throw e;
        }
    }

    private List<TaskDTO> prepareTaskDTOList(List<ProjectTaskEntity> projectTaskEntities) {
        log.info("Execute method prepareTaskDTOList : Params{} " + projectTaskEntities);
        try {
            List<TaskDTO> taskDTOS = new ArrayList<>();
            for (ProjectTaskEntity projectTaskEntity : projectTaskEntities) {
                taskDTOS.add(this.prepareTaskDTO(projectTaskEntity));
            }
            return taskDTOS;
        } catch (Exception e) {
            log.error("Method prepareTaskDTOList : " + e.getMessage(), e);
            throw e;
        }
    }

    private TaskDTO prepareTaskDTO(ProjectTaskEntity projectTaskEntity) {
        log.info("Execute method prepareTaskDTO : param{} " + projectTaskEntity);
        try {
            return new TaskDTO(
                    projectTaskEntity.getId(),
                    this.prepareUserStoryDTO(projectTaskEntity.getProjectUserStoryEntity()),
                    projectTaskEntity.getTitle(),
                    projectTaskEntity.getModifiedDate(),
                    this.prepareCorporateEmployeeDTO(projectTaskEntity.getCreatedBy()),
                    this.prepareCorporateEmployeeDTO(projectTaskEntity.getModifiedBy()),
                    projectTaskEntity.getStatusType(),
                    this.prepareTaskMembershipList(projectTaskEntity)
            );
        } catch (Exception e) {
            log.error("Method prepareTaskDTO : " + e.getMessage(), e);
            throw e;
        }
    }

    private List<ProjectMemberDTO> prepareTaskMembershipList(ProjectTaskEntity projectTaskEntity) {
        List<ProjectTaskAssignsEntity> byProjectTaskEntity = projectTaskAssignsRepository.findByProjectTaskEntity(projectTaskEntity);
        List<ProjectMemberDTO> projectMemberDTOS = new ArrayList<>();
        for (ProjectTaskAssignsEntity projectTaskAssignsEntity : byProjectTaskEntity) {
            ProjectMemberEntity m = projectTaskAssignsEntity.getProjectMemberEntity();
            projectMemberDTOS.add(
                    new ProjectMemberDTO(
                            m.getId(),
                            this.prepareCorporateEmployeeDTO(m.getCorporateEmployeeEntity()),
                            m.getAssignedDate(),
                            m.getModifiedDate(),
                            m.getScrumRole(),
                            m.getStatusType()
                    )
            );
        }
        return projectMemberDTOS;
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

    private UserStoryDTO prepareUserStoryDTO(ProjectUserStoryEntity userStoryEntity) {
        log.info("Execute method prepareUserStoryDTO : @Param {} " + (userStoryEntity.getId()==199?userStoryEntity.getDescription():""));
        try {
            ProjectEntity p = userStoryEntity.getProjectEntity();
            List<UserStoryLblDTO> userStoryLblDTOS = prepareProjectUserStoryLblByUserStory(userStoryEntity, p);
            List<ProjectSprintUserStoryEntity> sprintUserStoryEntities = projectSprintUserStoryRepository.getByLatestSprintUserStoryRecord(userStoryEntity.getId());
            List<ProjectSprintEntity> otherSprints = new ArrayList<>();
            SprintDTO currentSprint = null;
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
                    null,
                    sprintDTOList,
                    currentSprint,
                    userStoryEntity.getPoints()
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
