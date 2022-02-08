package com.swlc.ScrumPepperCPU6001.service.impl;

import com.swlc.ScrumPepperCPU6001.constant.ApplicationConstant;
import com.swlc.ScrumPepperCPU6001.constant.EmailTextConstant;
import com.swlc.ScrumPepperCPU6001.dto.CorporateEmployeeDTO;
import com.swlc.ScrumPepperCPU6001.dto.UserDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddCorporateEmployeeRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.ApproveRejectInvitationRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.SearchEmployeeRequestDTO;
import com.swlc.ScrumPepperCPU6001.entity.CorporateEmployeeEntity;
import com.swlc.ScrumPepperCPU6001.entity.CorporateEmployeeInvitationEntity;
import com.swlc.ScrumPepperCPU6001.entity.CorporateEntity;
import com.swlc.ScrumPepperCPU6001.entity.UserEntity;
import com.swlc.ScrumPepperCPU6001.enums.CorporateAccessStatusType;
import com.swlc.ScrumPepperCPU6001.enums.CorporateAccessType;
import com.swlc.ScrumPepperCPU6001.enums.CorporateEmployeeInvitationStatusType;
import com.swlc.ScrumPepperCPU6001.exception.CorporateEmployeeException;
import com.swlc.ScrumPepperCPU6001.exception.CorporateException;
import com.swlc.ScrumPepperCPU6001.repository.CorporateEmployeeInvitationRepository;
import com.swlc.ScrumPepperCPU6001.repository.CorporateEmployeeRepository;
import com.swlc.ScrumPepperCPU6001.repository.CorporateRepository;
import com.swlc.ScrumPepperCPU6001.repository.UserRepository;
import com.swlc.ScrumPepperCPU6001.service.CorporateEmployeeService;
import com.swlc.ScrumPepperCPU6001.util.EmailSender;
import com.swlc.ScrumPepperCPU6001.util.TokenValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
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
public class CorporateEmployeeServiceImpl implements CorporateEmployeeService {

    private final UserRepository userRepository;
    private final CorporateRepository corporateRepository;
    private final CorporateEmployeeRepository corporateEmployeeRepository;
    private final CorporateEmployeeInvitationRepository corporateEmployeeInvitationRepository;
    private final EmailTextConstant emailTextConstant;
    @Autowired
    private TokenValidator tokenValidator;
    @Autowired
    private EmailSender emailSender;

    public CorporateEmployeeServiceImpl(UserRepository userRepository, CorporateRepository corporateRepository,
                                        CorporateEmployeeRepository corporateEmployeeRepository, CorporateEmployeeInvitationRepository corporateEmployeeInvitationRepository, EmailTextConstant emailTextConstant) {
        this.userRepository = userRepository;
        this.corporateRepository = corporateRepository;
        this.corporateEmployeeRepository = corporateEmployeeRepository;
        this.corporateEmployeeInvitationRepository = corporateEmployeeInvitationRepository;
        this.emailTextConstant = emailTextConstant;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean addCorporateEmployee(AddCorporateEmployeeRequestDTO addCorporateEmployeeRequestDTO) {
        log.info("Execute method addCorporateEmployee : addCorporateEmployeeRequestDTO : " + addCorporateEmployeeRequestDTO.toString());
        try {
            Optional<CorporateEntity> corporateById = corporateRepository.findById(addCorporateEmployeeRequestDTO.getCorporateId());
            if(!corporateById.isPresent())
                throw new CorporateEmployeeException(ApplicationConstant.RESOURCE_NOT_FOUND,
                        "Corporate account not found");
            CorporateEntity corporateEntity = corporateById.get();
            UserEntity userAdminEntity = tokenValidator.retrieveUserInformationFromAuthentication();
            Optional<CorporateEmployeeEntity> auth_user_admin = corporateEmployeeRepository.findByUserEntityAndCorporateEntityAndStatusType(
                    userAdminEntity,
                    corporateEntity,
                    CorporateAccessStatusType.ACTIVE
            );
            if(!auth_user_admin.isPresent())
                throw new CorporateException(
                        ApplicationConstant.UN_AUTH_ACTION,
                        "Unauthorized action. You can't processed this action"
                );
            CorporateEmployeeEntity corporateEmployeeAdminEntity = auth_user_admin.get();
            if(!(corporateEmployeeAdminEntity.getCorporateAccessType().equals(CorporateAccessType.CORPORATE_SUPER) ||
                    corporateEmployeeAdminEntity.getCorporateAccessType().equals(CorporateAccessType.CORPORATE_ADMIN)))
                throw new CorporateException(
                        ApplicationConstant.UN_AUTH_ACTION,
                        "Unauthorized action. You can't processed this action"
                );
            Optional<UserEntity> byId = userRepository.findById(addCorporateEmployeeRequestDTO.getUserId());
            if(!byId.isPresent())
                throw new CorporateEmployeeException(ApplicationConstant.RESOURCE_NOT_FOUND, "User not found");
            UserEntity userEntity = byId.get();
            Optional<CorporateEmployeeEntity> byUserEntity =
                    corporateEmployeeRepository.findByUserEntityAndCorporateEntity(userEntity, corporateEntity);
            if(byUserEntity.isPresent())
                throw new CorporateEmployeeException(ApplicationConstant.RESOURCE_ALREADY_EXIST,
                        "This user already exist as an employee in your corporate");
            CorporateEmployeeEntity saveCorporateEmployeeEntity = corporateEmployeeRepository.save(
                    new CorporateEmployeeEntity(
                            userEntity,
                            corporateEntity,
                            addCorporateEmployeeRequestDTO.getAccessType(),
                            new Date(),
                            null,
                            new Date(),
                            CorporateAccessStatusType.PENDING
                    )
            );
            corporateEmployeeInvitationRepository.save(
                    new CorporateEmployeeInvitationEntity(
                            saveCorporateEmployeeEntity,
                            userEntity.getEmail(),
                            corporateEmployeeAdminEntity,
                            new Date(),
                            CorporateEmployeeInvitationStatusType.PENDING
                    )
            );
            String corporateInvitationTemplate = emailTextConstant.getCorporateInvitationTemplate(
                    corporateEntity.getName(),
                    userEntity.getFirstName() + " " + userEntity.getLastName()
            );
            emailSender.sendCorporateInvitations2(
                    userEntity.getEmail(),
                    "You have corporate joining invitation",
                    corporateInvitationTemplate
            );
            return true;
        } catch (Exception e) {
            log.error("Method addCorporateEmployee : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean approveRejectCorporateEmployeeInvitation(ApproveRejectInvitationRequestDTO approveRejectInvitationRequestDTO) {
        log.info("Execute method approveRejectCorporateEmployeeInvitation : approveRejectInvitationRequestDTO : " +
                approveRejectInvitationRequestDTO.toString());
        try {
            UserEntity userEntityToken = tokenValidator.retrieveUserInformationFromAuthentication();
            Optional<CorporateEmployeeInvitationEntity> invitationById =
                    corporateEmployeeInvitationRepository.findById(approveRejectInvitationRequestDTO.getInvitationId());
            if(!invitationById.isPresent())
                throw new CorporateEmployeeException(
                        ApplicationConstant.RESOURCE_NOT_FOUND,
                        "Corporate employee invitation not found"
                );
            CorporateEmployeeInvitationEntity corporateEmployeeInvitationEntity = invitationById.get();
            CorporateEmployeeEntity corporateEmployeeEntity =
                    corporateEmployeeInvitationEntity.getCorporateEmployeeEntity();
            UserEntity userEntity = corporateEmployeeEntity.getUserEntity();
            if(userEntityToken.getId()!=userEntity.getId())
                throw new CorporateEmployeeException(
                        ApplicationConstant.UN_AUTH_ACTION,
                        "Unauthorized action"
                );
            switch (approveRejectInvitationRequestDTO.getInvitationStatus()) {
                case "ACCEPTED":
                    corporateEmployeeInvitationEntity.setStatusType(CorporateEmployeeInvitationStatusType.ACCEPTED);
                    corporateEmployeeEntity.setAcceptedDate(new Date());
                    corporateEmployeeEntity.setStatusType(CorporateAccessStatusType.ACTIVE);
                    break;
                case "REJECT":
                    corporateEmployeeInvitationEntity.setStatusType(CorporateEmployeeInvitationStatusType.REJECT);
                    corporateEmployeeEntity.setAcceptedDate(new Date());
                    corporateEmployeeEntity.setStatusType(CorporateAccessStatusType.INACTIVE);
                    break;
            }
            corporateEmployeeInvitationRepository.save(corporateEmployeeInvitationEntity);
            corporateEmployeeRepository.save(corporateEmployeeEntity);
            return true;
        } catch (Exception e) {
            log.error("Method approveRejectCorporateEmployeeInvitation : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<CorporateEmployeeDTO> searchCorporateEmployee(SearchEmployeeRequestDTO searchEmployeeRequestDTO) {
        log.info("Execute method searchCorporateEmployee : searchEmployeeRequestDTO : " + searchEmployeeRequestDTO.toString());
        try {
            Optional<CorporateEntity> corporateById = corporateRepository.findById(searchEmployeeRequestDTO.getCorporateId());
            if(!corporateById.isPresent())
                throw new CorporateEmployeeException(ApplicationConstant.RESOURCE_NOT_FOUND,
                        "Corporate account not found");
            CorporateEntity corporateEntity = corporateById.get();
            List<CorporateEmployeeEntity> corporateEmployeeEntities =
                    corporateEmployeeRepository.searchCorporateEmployeeEntity(
                            corporateEntity.getId(),
                            searchEmployeeRequestDTO.getSearch()
                    );
            List<CorporateEmployeeDTO> corporateEmployeeDTOS =  new ArrayList<>();
            for (CorporateEmployeeEntity e : corporateEmployeeEntities) {
                corporateEmployeeDTOS.add(new CorporateEmployeeDTO(
                        e.getId(),
                        new UserDTO(
                                e.getUserEntity().getId(),
                                e.getUserEntity().getRefNo(),
                                e.getUserEntity().getFirstName(),
                                e.getUserEntity().getLastName(),
                                e.getUserEntity().getContactNumber(),
                                e.getUserEntity().getEmail(),
                                null,
                                e.getUserEntity().getCreatedDate(),
                                e.getUserEntity().getStatusType()
                        ),
                        null,
                        e.getCorporateAccessType(),
                        e.getCreatedDate(),
                        e.getModifiedDate(),
                        e.getAcceptedDate(),
                        e.getStatusType()
                ));
            }
            return corporateEmployeeDTOS;
        } catch (Exception e) {
            log.error("Method searchCorporateEmployee : " + e.getMessage(), e);
            throw e;
        }
    }
}
