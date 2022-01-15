package com.swlc.ScrumPepperCPU6001.service.impl;

import com.swlc.ScrumPepperCPU6001.constant.ApplicationConstant;
import com.swlc.ScrumPepperCPU6001.dto.request.AddProjectMemberDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateProjectMemberDTO;
import com.swlc.ScrumPepperCPU6001.entity.*;
import com.swlc.ScrumPepperCPU6001.enums.CorporateAccessStatusType;
import com.swlc.ScrumPepperCPU6001.enums.CorporateAccessType;
import com.swlc.ScrumPepperCPU6001.enums.ProjectMemberStatusType;
import com.swlc.ScrumPepperCPU6001.enums.ScrumRoles;
import com.swlc.ScrumPepperCPU6001.exception.CorporateEmployeeException;
import com.swlc.ScrumPepperCPU6001.exception.CorporateException;
import com.swlc.ScrumPepperCPU6001.exception.ProjectException;
import com.swlc.ScrumPepperCPU6001.repository.CorporateEmployeeRepository;
import com.swlc.ScrumPepperCPU6001.repository.CorporateRepository;
import com.swlc.ScrumPepperCPU6001.repository.ProjectMemberRepository;
import com.swlc.ScrumPepperCPU6001.repository.ProjectRepository;
import com.swlc.ScrumPepperCPU6001.service.ProjectMemberService;
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
public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final CorporateRepository corporateRepository;
    private final CorporateEmployeeRepository corporateEmployeeRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    @Autowired
    private TokenValidator tokenValidator;

    public ProjectMemberServiceImpl(CorporateRepository corporateRepository,
                                    CorporateEmployeeRepository corporateEmployeeRepository,
                                    ProjectRepository projectRepository, ProjectMemberRepository projectMemberRepository) {
        this.corporateRepository = corporateRepository;
        this.corporateEmployeeRepository = corporateEmployeeRepository;
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
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
}
