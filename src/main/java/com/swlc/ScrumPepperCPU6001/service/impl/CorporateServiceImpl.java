package com.swlc.ScrumPepperCPU6001.service.impl;

import com.swlc.ScrumPepperCPU6001.constant.ApplicationConstant;
import com.swlc.ScrumPepperCPU6001.dto.*;
import com.swlc.ScrumPepperCPU6001.dto.request.AddCorporateRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.DeleteCorporateRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.GetCorporateDetailsRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateCorporateRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.GetCorporateDetailsResponseDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.ProjectMemberResponse.ProjectMemberResponseDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.YourProjectResponseDTO;
import com.swlc.ScrumPepperCPU6001.entity.*;
import com.swlc.ScrumPepperCPU6001.enums.CorporateAccessStatusType;
import com.swlc.ScrumPepperCPU6001.enums.CorporateAccessType;
import com.swlc.ScrumPepperCPU6001.enums.ScrumRoles;
import com.swlc.ScrumPepperCPU6001.enums.StatusType;
import com.swlc.ScrumPepperCPU6001.exception.CorporateException;
import com.swlc.ScrumPepperCPU6001.repository.CorporateEmployeeRepository;
import com.swlc.ScrumPepperCPU6001.repository.CorporateRepository;
import com.swlc.ScrumPepperCPU6001.repository.ProjectMemberRepository;
import com.swlc.ScrumPepperCPU6001.repository.ProjectRepository;
import com.swlc.ScrumPepperCPU6001.service.CorporateService;
import com.swlc.ScrumPepperCPU6001.util.FileWriter;
import com.swlc.ScrumPepperCPU6001.util.TokenValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
public class CorporateServiceImpl implements CorporateService {

    private final CorporateRepository corporateRepository;
    private final CorporateEmployeeRepository corporateEmployeeRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    @Autowired
    private TokenValidator tokenValidator;
    @Autowired
    private FileWriter fileWriter;

    public CorporateServiceImpl(CorporateRepository corporateRepository, CorporateEmployeeRepository corporateEmployeeRepository, ProjectRepository projectRepository, ProjectMemberRepository projectMemberRepository) {
        this.corporateRepository = corporateRepository;
        this.corporateEmployeeRepository = corporateEmployeeRepository;
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean createNewCorporate(AddCorporateRequestDTO addCorporateRequestDTO) {
        log.info("Execute method createNewCorporate : addCorporateRequestDTO : " + addCorporateRequestDTO.toString());
        try {
            UserEntity userEntity = tokenValidator.retrieveUserInformationFromAuthentication();
            String corporateLogoPath = "";
            CorporateEntity corporateEntity = corporateRepository.save(
                    new CorporateEntity(
                            addCorporateRequestDTO.getName(),
                            addCorporateRequestDTO.getAddress(),
                            addCorporateRequestDTO.getContactNumber1(),
                            addCorporateRequestDTO.getContactNumber2(),
                            addCorporateRequestDTO.getEmail(),
                            corporateLogoPath,
                            new Date(),
                            StatusType.ACTIVE
                    )
            );
            corporateEmployeeRepository.save(
                    new CorporateEmployeeEntity(
                            userEntity,
                            corporateEntity,
                            CorporateAccessType.CORPORATE_SUPER,
                            new Date(),
                            new Date(),
                            new Date(),
                            CorporateAccessStatusType.ACTIVE
                    )
            );
            return true;
        } catch (Exception e) {
            log.error("Method createNewCorporate : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean updateCorporate(UpdateCorporateRequestDTO updateCorporateRequestDTO) {
        log.info("Execute method updateCorporate : updateCorporateRequestDTO : " + updateCorporateRequestDTO.toString());
        try {
            UserEntity userEntity = tokenValidator.retrieveUserInformationFromAuthentication();
            Optional<CorporateEntity> corporateById = corporateRepository.findById(updateCorporateRequestDTO.getId());
            if(!corporateById.isPresent())
                throw new CorporateException(ApplicationConstant.RESOURCE_NOT_FOUND, "Corporate account not found");
            CorporateEntity corporateEntity = corporateById.get();
            Optional<CorporateEmployeeEntity> corporateSuperAdminOptional =
                    corporateEmployeeRepository.findByUserEntityAndCorporateEntityAndCorporateAccessTypeAndStatusType(
                    userEntity,
                    corporateEntity,
                    CorporateAccessType.CORPORATE_SUPER,
                    CorporateAccessStatusType.ACTIVE
            );
            if(!corporateSuperAdminOptional.isPresent())
                throw new CorporateException(
                        ApplicationConstant.UN_AUTH_ACTION,
                        "Unauthorized action. You can't processed this action"
                );
            String corporateLogoPath = "";
            corporateEntity.setName(updateCorporateRequestDTO.getName());
            corporateEntity.setAddress(updateCorporateRequestDTO.getAddress());
            corporateEntity.setContactNumber1(updateCorporateRequestDTO.getContactNumber1());
            corporateEntity.setContactNumber2(updateCorporateRequestDTO.getContactNumber2());
            corporateEntity.setEmail(updateCorporateRequestDTO.getEmail());
            corporateEntity.setStatusType(updateCorporateRequestDTO.getStatusType());
            if(updateCorporateRequestDTO.getCorporateLogo()!=null)
                corporateEntity.setCorporateLogo(corporateLogoPath);
            corporateRepository.save(corporateEntity);
            return true;
        } catch (Exception e) {
            log.error("Method updateCorporate : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean deleteCorporate(DeleteCorporateRequestDTO deleteCorporateRequestDTO) {
        log.info("Execute method deleteCorporate : corporateId : " + deleteCorporateRequestDTO.getId());
        try {
            UserEntity userEntity = tokenValidator.retrieveUserInformationFromAuthentication();
            Optional<CorporateEntity> corporateById = corporateRepository.findById(deleteCorporateRequestDTO.getId());
            if(!corporateById.isPresent())
                throw new CorporateException(ApplicationConstant.RESOURCE_NOT_FOUND, "Corporate account not found");
            CorporateEntity corporateEntity = corporateById.get();
            Optional<CorporateEmployeeEntity> corporateSuperAdminOptional =
                    corporateEmployeeRepository.findByUserEntityAndCorporateEntityAndCorporateAccessTypeAndStatusType(
                            userEntity,
                            corporateEntity,
                            CorporateAccessType.CORPORATE_SUPER,
                            CorporateAccessStatusType.ACTIVE
                    );
            if(!corporateSuperAdminOptional.isPresent())
                throw new CorporateException(
                        ApplicationConstant.UN_AUTH_ACTION,
                        "Unauthorized action. You can't processed this action"
                );
            corporateEntity.setStatusType(StatusType.DELETE);
            corporateRepository.save(corporateEntity);
            return true;
        } catch (Exception e) {
            log.error("Method deleteCorporate : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<MyCorporateDTO> getMyCorporates() {
        log.info("Execute method getMyCorporates : ");
        List<MyCorporateDTO> result = new ArrayList<>();
        try {
            UserEntity userEntity = tokenValidator.retrieveUserInformationFromAuthentication();
            List<CorporateEmployeeEntity> byUserEntityAndStatusType =
                    corporateEmployeeRepository.findByUserEntityAndStatusType(userEntity, CorporateAccessStatusType.ACTIVE);
            for (CorporateEmployeeEntity ce:byUserEntityAndStatusType) {
                result.add(this.prepareMyCorporateDTOs(ce));
            }
        } catch (Exception e) {
            log.error("Method getMyCorporates : " + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public String uploadCorporateLogo(MultipartFile file) {
        log.info("Execute method uploadCorporateLogo : " + file);
        try {
            String path = fileWriter.saveMultipartImage(file);
            return path;
        } catch (Exception e) {
            log.error("Method uploadCorporateLogo : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public GetCorporateDetailsResponseDTO getCorporateDetails(GetCorporateDetailsRequestDTO getCorporateDetailsRequestDTO) {
        log.info("Execute method getCorporateDetails : ");
        try {
            UserEntity userEntity = tokenValidator.retrieveUserInformationFromAuthentication();
            Optional<CorporateEntity> corporateEntityOptional =
                    corporateRepository.findById(getCorporateDetailsRequestDTO.getCorporateId());
            if(!corporateEntityOptional.isPresent())
                throw new CorporateException(ApplicationConstant.RESOURCE_NOT_FOUND, "Corporate not found");
            CorporateEntity corporateEntity = corporateEntityOptional.get();
            Optional<CorporateEmployeeEntity> corporateEmployeeOptional = corporateEmployeeRepository.findByUserEntityAndCorporateEntityAndStatusType(
                    userEntity,
                    corporateEntity,
                    CorporateAccessStatusType.ACTIVE
            );
            if(!corporateEmployeeOptional.isPresent())
                throw new CorporateException(ApplicationConstant.UN_AUTH_ACTION, "Access Denied");
            CorporateEmployeeEntity corporateEmployeeEntity = corporateEmployeeOptional.get();
            CorporateAccessType corporateAccessType = corporateEmployeeEntity.getCorporateAccessType();

            // prepare corporate dto
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

            // prepare corporate employee list
            List<CorporateEmployeeEntity> allByCorporateEntityAndStatusType = corporateEmployeeRepository.findAllByCorporateEntityAndStatusType(corporateEntity, CorporateAccessStatusType.ACTIVE);
            List<CorporateEmployeeDTO> corporateEmployeeDTOS = this.prepareCorporateEmployeeList(allByCorporateEntityAndStatusType, corporateAccessType);

            // prepare projects
            List<ProjectEntity> projectsByCorporateEntity = new ArrayList<>();
            if(corporateAccessType.equals(CorporateAccessType.CORPORATE_SUPER) || corporateAccessType.equals(CorporateAccessType.CORPORATE_ADMIN)) {
                projectsByCorporateEntity = projectRepository.findByCorporateEntity(corporateEntity);
            } else {
                projectsByCorporateEntity = projectMemberRepository.getByCorporateEmployeeEntity(corporateEmployeeEntity);
            }
            List<YourProjectResponseDTO> yourProjectResponseDTOS = this.prepareProjectList(projectsByCorporateEntity, corporateEmployeeEntity);

            return new GetCorporateDetailsResponseDTO(corporateDTO, corporateAccessType, corporateEmployeeDTOS, yourProjectResponseDTOS);
        } catch (Exception e) {
            log.error("Method getCorporateDetails : " + e.getMessage(), e);
            throw e;
        }
    }

    private List<YourProjectResponseDTO> prepareProjectList(List<ProjectEntity> projectEntityList, CorporateEmployeeEntity corporateEmployeeEntity) {
        log.info("Execute method prepareProjectList : ");
        try {
            List<YourProjectResponseDTO> yourProjectResponseDTOS =  new ArrayList<>();
            for (ProjectEntity p : projectEntityList) {
                CorporateEmployeeEntity createdBy = p.getCreated_CorporateEmployeeEntity();
                CorporateEmployeeEntity modifyBy = p.getModified_CorporateEmployeeEntity();
                UserEntity cu = createdBy.getUserEntity();
                UserEntity mu = modifyBy.getUserEntity();
                CorporateEmployeeDTO createdEmployee = new CorporateEmployeeDTO(
                        createdBy.getId(),
                        new UserDTO(
                                cu.getId(),
                                cu.getRefNo(),
                                cu.getFirstName(),
                                cu.getLastName(),
                                cu.getContactNumber(),
                                cu.getEmail(),
                                null,
                                cu.getCreatedDate(),
                                cu.getStatusType()
                        ),
                        null,
                        createdBy.getCorporateAccessType(),
                        createdBy.getCreatedDate(),
                        createdBy.getModifiedDate(),
                        createdBy.getAcceptedDate(),
                        createdBy.getStatusType()
                );

                CorporateEmployeeDTO modifiedEmployee = new CorporateEmployeeDTO(
                        modifyBy.getId(),
                        new UserDTO(
                                mu.getId(),
                                mu.getRefNo(),
                                mu.getFirstName(),
                                mu.getLastName(),
                                mu.getContactNumber(),
                                mu.getEmail(),
                                null,
                                mu.getCreatedDate(),
                                mu.getStatusType()
                        ),
                        null,
                        modifyBy.getCorporateAccessType(),
                        modifyBy.getCreatedDate(),
                        modifyBy.getModifiedDate(),
                        modifyBy.getAcceptedDate(),
                        modifyBy.getStatusType()
                );
                ProjectDTO projectDTO = new ProjectDTO(p.getId(), null, p.getProjectName(), p.getCreatedDate(), p.getModifiedDate(), createdEmployee, modifiedEmployee, p.getStatusType(), p.getRef());
                List<ProjectMemberResponseDTO> projectMemberResponseDTOS =  new ArrayList<>();
                List<ProjectMemberEntity> projectMemberEntities = projectMemberRepository.findByProjectEntity(p);
                for (ProjectMemberEntity pm : projectMemberEntities) {
                    CorporateEmployeeEntity assignedBy = pm.getAssignedBy();
                    CorporateEmployeeEntity modifyMemberBy = pm.getModifiedBy();
                    UserEntity am = assignedBy.getUserEntity();
                    UserEntity mm = modifyMemberBy.getUserEntity();
                    CorporateEmployeeDTO assignedMember = new CorporateEmployeeDTO(
                            assignedBy.getId(),
                            new UserDTO(
                                    am.getId(),
                                    am.getRefNo(),
                                    am.getFirstName(),
                                    am.getLastName(),
                                    am.getContactNumber(),
                                    am.getEmail(),
                                    null,
                                    am.getCreatedDate(),
                                    am.getStatusType()
                            ),
                            null,
                            assignedBy.getCorporateAccessType(),
                            assignedBy.getCreatedDate(),
                            assignedBy.getModifiedDate(),
                            assignedBy.getAcceptedDate(),
                            assignedBy.getStatusType()
                    );

                    CorporateEmployeeDTO modifiedMember = new CorporateEmployeeDTO(
                            modifyMemberBy.getId(),
                            new UserDTO(
                                    mm.getId(),
                                    mm.getRefNo(),
                                    mm.getFirstName(),
                                    mm.getLastName(),
                                    mm.getContactNumber(),
                                    mm.getEmail(),
                                    null,
                                    mm.getCreatedDate(),
                                    mm.getStatusType()
                            ),
                            null,
                            modifyMemberBy.getCorporateAccessType(),
                            modifyMemberBy.getCreatedDate(),
                            modifyMemberBy.getModifiedDate(),
                            modifyMemberBy.getAcceptedDate(),
                            modifyMemberBy.getStatusType()
                    );
                    projectMemberResponseDTOS.add(new ProjectMemberResponseDTO(
                            pm.getId(),
                            pm.getCorporateEmployeeEntity().getId(),
                            pm.getAssignedDate(),
                            pm.getModifiedDate(),
                            assignedMember,
                            modifiedMember,
                            pm.getScrumRole(),
                            pm.getStatusType()
                    ));
                }
                Optional<ProjectMemberEntity> byCorporateEmployeeEntity =
                        projectMemberRepository.findByCorporateEmployeeEntityAndProjectEntity(corporateEmployeeEntity, p);
                ScrumRoles scrumRole = null;
                if(byCorporateEmployeeEntity.isPresent())
                    scrumRole = byCorporateEmployeeEntity.get().getScrumRole();
                yourProjectResponseDTOS.add(new YourProjectResponseDTO(projectDTO, projectMemberResponseDTOS, scrumRole));
            }
            return yourProjectResponseDTOS;
        } catch (Exception e) {
            log.error("Method prepareProjectList : " + e.getMessage(), e);
            throw e;
        }
    }

    private List<CorporateEmployeeDTO> prepareCorporateEmployeeList(List<CorporateEmployeeEntity> corporateEmployeeEntities, CorporateAccessType corporateAccessType) {
        log.info("Execute method prepareCorporateEmployeeList : ");
        try {
            List<CorporateEmployeeDTO> corporateEmployeeDTOs = new ArrayList<>();
            for (CorporateEmployeeEntity ce : corporateEmployeeEntities) {
                UserEntity u = ce.getUserEntity();
                corporateEmployeeDTOs.add(
                        new CorporateEmployeeDTO(
                            ce.getId(),
                            new UserDTO(
                                    u.getId(),
                                    u.getRefNo(),
                                    u.getFirstName(),
                                    u.getLastName(),
                                    u.getContactNumber(),
                                    u.getEmail(),
                                    null,
                                    u.getCreatedDate(),
                                    u.getStatusType()
                            ),
                            null,
                            ce.getCorporateAccessType(),
                            ce.getCreatedDate(),
                            ce.getModifiedDate(),
                            ce.getAcceptedDate(),
                            ce.getStatusType()
                    )
                );
            }
            return corporateEmployeeDTOs;
        } catch (Exception e) {
            log.error("Method prepareCorporateEmployeeList : " + e.getMessage(), e);
            throw e;
        }
    }

    private MyCorporateDTO prepareMyCorporateDTOs(CorporateEmployeeEntity ce) {
        log.info("Execute method prepareMyCorporateDTOs : " + ce.toString());
        try {
            CorporateEntity c = ce.getCorporateEntity();
            CorporateDTO corporateDTO = new CorporateDTO(
                    c.getId(),
                    c.getName(),
                    c.getAddress(),
                    c.getContactNumber1(),
                    c.getContactNumber2(),
                    c.getEmail(),
                    c.getCorporateLogo(),
                    c.getStatusType()
            );
            CorporateAccessType corporateAccessType = ce.getCorporateAccessType();
            return new MyCorporateDTO(corporateDTO, corporateAccessType);
        } catch (Exception e) {
            log.error("Method prepareMyCorporateDTOs : " + e.getMessage(), e);
            throw e;
        }
    }
}
