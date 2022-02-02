package com.swlc.ScrumPepperCPU6001.service.impl;

import com.swlc.ScrumPepperCPU6001.constant.ApplicationConstant;
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

import java.util.Date;
import java.util.Optional;

/**
 * @author hp
 */
@Log4j2
@Service
public class TasksServiceImpl implements TaskService {

    private final UserStoryRepository userStoryRepository;
    private final ProjectRepository projectRepository;
    private final CorporateEmployeeRepository corporateEmployeeRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectTaskRepository projectTaskRepository;
    private final ProjectTaskAssignsRepository projectTaskAssignsRepository;
    @Autowired
    private TokenValidator tokenValidator;

    public TasksServiceImpl(UserStoryRepository userStoryRepository,
                            ProjectRepository projectRepository,
                            CorporateEmployeeRepository corporateEmployeeRepository,
                            ProjectMemberRepository projectMemberRepository, ProjectTaskRepository projectTaskRepository, ProjectTaskAssignsRepository projectTaskAssignsRepository) {
        this.userStoryRepository = userStoryRepository;
        this.projectRepository = projectRepository;
        this.corporateEmployeeRepository = corporateEmployeeRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.projectTaskRepository = projectTaskRepository;
        this.projectTaskAssignsRepository = projectTaskAssignsRepository;
    }

    @Override
    public boolean createNewTask(AddProjectTaskRequestDTO task) {
        log.info("Execute method createNewTask : email : " + task.toString());
        try {
            Optional<ProjectUserStoryEntity> projectUserStoryById = userStoryRepository.findById(task.getUserStoryId());
            if(projectUserStoryById.isPresent())
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

            projectTaskRepository.save(
                    new ProjectTaskEntity(
                            projectUserStoryById.get(),
                            task.getTitle(),
                            new Date(),
                            auth_user_admin.get(),
                            auth_user_admin.get(),
                            task.getStatusType()
                    )
            );

            return true;

        } catch (Exception e) {
            log.error("Method createNewTask : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean addMemberToTask(long taskId, long id) {
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

            return true;
        } catch (Exception e) {
            log.error("Method addMemberToTask : " + e.getMessage(), e);
            throw e;
        }
    }


}
