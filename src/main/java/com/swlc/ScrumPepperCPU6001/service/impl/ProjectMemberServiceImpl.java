package com.swlc.ScrumPepperCPU6001.service.impl;

import com.swlc.ScrumPepperCPU6001.constant.ApplicationConstant;
import com.swlc.ScrumPepperCPU6001.dto.CorporateEmployeeDTO;
import com.swlc.ScrumPepperCPU6001.dto.UserDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddProjectMemberDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateProjectMemberDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.GetTaskEmployeeDTO;
import com.swlc.ScrumPepperCPU6001.entity.*;
import com.swlc.ScrumPepperCPU6001.enums.*;
import com.swlc.ScrumPepperCPU6001.exception.CorporateEmployeeException;
import com.swlc.ScrumPepperCPU6001.exception.CorporateException;
import com.swlc.ScrumPepperCPU6001.exception.ProjectException;
import com.swlc.ScrumPepperCPU6001.repository.*;
import com.swlc.ScrumPepperCPU6001.service.ProjectMemberService;
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
public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final CorporateRepository corporateRepository;
    private final CorporateEmployeeRepository corporateEmployeeRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectTaskRepository projectTaskRepository;
    private final ProjectTaskAssignsRepository projectTaskAssignsRepository;
    @Autowired
    private TokenValidator tokenValidator;

    public ProjectMemberServiceImpl(CorporateRepository corporateRepository,
                                    CorporateEmployeeRepository corporateEmployeeRepository,
                                    ProjectRepository projectRepository, ProjectMemberRepository projectMemberRepository, ProjectTaskRepository projectTaskRepository, ProjectTaskAssignsRepository projectTaskAssignsRepository) {
        this.corporateRepository = corporateRepository;
        this.corporateEmployeeRepository = corporateEmployeeRepository;
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.projectTaskRepository = projectTaskRepository;
        this.projectTaskAssignsRepository = projectTaskAssignsRepository;
    }

    @Override
    public boolean addProjectMembers(AddProjectMemberDTO addProjectMemberDTO) {
        log.info("Execute method addProjectMembers : addProjectMemberDTO : " + addProjectMemberDTO.toString());
        try {
            Optional<ProjectEntity> projectById = projectRepository.findById(addProjectMemberDTO.getProjectId());
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

            Optional<CorporateEmployeeEntity> corporateEmployeeById =
                    corporateEmployeeRepository.findById(addProjectMemberDTO.getCorporateEmployeeId());
            if(!corporateEmployeeById.isPresent())
                throw new CorporateEmployeeException(ApplicationConstant.RESOURCE_NOT_FOUND, "Corporate employee not found");
            projectMemberRepository.save(
                    new ProjectMemberEntity(
                            projectEntity,
                            corporateEmployeeById.get(),
                            new Date(),
                            new Date(),
                            auth_user_admin.get(),
                            auth_user_admin.get(),
                            addProjectMemberDTO.getScrumRole(),
                            ProjectMemberStatusType.ACTIVE
                    )
            );
            return true;
        } catch (Exception e) {
            log.error("Method addProjectMembers : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean updateProjectMember(UpdateProjectMemberDTO updateProjectMemberDTO) {
        log.info("Execute method updateProjectMember : updateProjectMemberDTO : " + updateProjectMemberDTO.toString());
        try {

            Optional<ProjectMemberEntity> projectMemberById =
                    projectMemberRepository.findById(updateProjectMemberDTO.getProjectMemberId());
            if(!projectMemberById.isPresent())
                throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "Project member not found");
            ProjectMemberEntity projectMemberEntity = projectMemberById.get();
            CorporateEmployeeEntity corporateEmployeeMemberEntity = projectMemberEntity.getCorporateEmployeeEntity();
            ProjectEntity projectEntity = projectMemberEntity.getProjectEntity();
            CorporateEntity corporateEntity = corporateEmployeeMemberEntity.getCorporateEntity();

            //action auth
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
                ProjectMemberEntity authProjectMemberEntity = byProjectEntityAndCorporateEmployeeEntity.get();
                if(!(authProjectMemberEntity.getScrumRole().equals(ScrumRoles.PRODUCT_OWNER) ||
                        authProjectMemberEntity.getScrumRole().equals(ScrumRoles.PRODUCT_OWNER)))
                    throw new CorporateException(
                            ApplicationConstant.UN_AUTH_ACTION,
                            "Unauthorized action. You can't processed this action"
                    );
            }

            //proceed update
            projectMemberEntity.setScrumRole(updateProjectMemberDTO.getScrumRole());
            projectMemberEntity.setModifiedDate(new Date());
            projectMemberEntity.setModifiedBy(auth_user_admin.get());
            projectMemberRepository.save(projectMemberEntity);
            return true;
        } catch (Exception e) {
            log.error("Method updateProjectMember : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean removeProjectMember(long projectMemberId) {
        log.info("Execute method removeProjectMember : projectMemberId : " + projectMemberId);
        try {
            Optional<ProjectMemberEntity> projectMemberById =
                    projectMemberRepository.findById(projectMemberId);
            if(!projectMemberById.isPresent())
                throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "Project member not found");
            ProjectMemberEntity projectMemberEntity = projectMemberById.get();
            CorporateEmployeeEntity corporateEmployeeMemberEntity = projectMemberEntity.getCorporateEmployeeEntity();
            ProjectEntity projectEntity = projectMemberEntity.getProjectEntity();
            CorporateEntity corporateEntity = corporateEmployeeMemberEntity.getCorporateEntity();

            //action auth
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
                ProjectMemberEntity authProjectMemberEntity = byProjectEntityAndCorporateEmployeeEntity.get();
                if(!(authProjectMemberEntity.getScrumRole().equals(ScrumRoles.PRODUCT_OWNER) ||
                        authProjectMemberEntity.getScrumRole().equals(ScrumRoles.PRODUCT_OWNER)))
                    throw new CorporateException(
                            ApplicationConstant.UN_AUTH_ACTION,
                            "Unauthorized action. You can't processed this action"
                    );
            }
            projectMemberEntity.setStatusType(ProjectMemberStatusType.DELETE);
            projectMemberRepository.save(projectMemberEntity);
            return true;
        } catch (Exception e) {
            log.error("Method removeProjectMember : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<CorporateEmployeeDTO> getProjectMember(long projectId) {
        log.info("Execute method removeProjectMember : projectId : " + projectId);
        try {
            Optional<ProjectEntity> byId = projectRepository.findById(projectId);
            if(!byId.isPresent())
                throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "Project not found");
            List<ProjectMemberEntity> projectMembers = projectMemberRepository.findByProjectEntity(byId.get());
            List<CorporateEmployeeDTO> projectMemberList = new ArrayList<>();
            for (ProjectMemberEntity pm : projectMembers) {
                projectMemberList.add(this.prepareCorporateEmployeeDTO(pm.getCorporateEmployeeEntity()));
            }
            return projectMemberList;
        } catch (Exception e) {
            log.error("Method getProjectMember : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public GetTaskEmployeeDTO getTaskMembers(long taskId) {
        try {

            Optional<ProjectTaskEntity> taskOptional = projectTaskRepository.findById(taskId);
            if(!taskOptional.isPresent())
                throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "Task not found");
            ProjectTaskEntity projectTaskEntity = taskOptional.get();
            ProjectEntity projectEntity = projectTaskEntity.getProjectUserStoryEntity().getProjectEntity();
            CorporateEntity corporateEntity = projectEntity.getCorporateEntity();

            List<CorporateEmployeeEntity> taskAssigns = projectTaskAssignsRepository.getTaskAssignsCorporateEmployees(StatusType.ACTIVE, projectTaskEntity, ProjectMemberStatusType.ACTIVE);
            List<CorporateEmployeeEntity> projectMembers = projectTaskAssignsRepository.getTaskNotAssignsProjectCorporateEmployees(StatusType.ACTIVE, ProjectMemberStatusType.ACTIVE, projectTaskEntity, projectEntity, ProjectMemberStatusType.ACTIVE);
            List<CorporateEmployeeEntity> otherCorporateEmployees = projectTaskAssignsRepository.getTaskNotAssignsProjectCorporateEmployees(CorporateAccessStatusType.ACTIVE, corporateEntity, projectEntity, ProjectMemberStatusType.ACTIVE);

            return
                    new GetTaskEmployeeDTO(
                    this.prepareCorporateEmployeeDTOList(taskAssigns),
                    this.prepareCorporateEmployeeDTOList(projectMembers),
                    this.prepareCorporateEmployeeDTOList(otherCorporateEmployees)
            );
        } catch (Exception e) {
            throw e;
        }
    }

    private List<CorporateEmployeeDTO> prepareCorporateEmployeeDTOList(List<CorporateEmployeeEntity> corporateEmployeeEntities) {
        try {
            List<CorporateEmployeeDTO> corporateEmployeeDTOS =  new ArrayList<>();
            for (CorporateEmployeeEntity corporateEmployeeEntity : corporateEmployeeEntities) {
                corporateEmployeeDTOS.add(this.prepareCorporateEmployeeDTO(corporateEmployeeEntity));
            }
            return corporateEmployeeDTOS;
        } catch (Exception e) {
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
