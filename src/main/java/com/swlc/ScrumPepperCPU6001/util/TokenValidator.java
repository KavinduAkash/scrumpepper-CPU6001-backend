package com.swlc.ScrumPepperCPU6001.util;

import com.swlc.ScrumPepperCPU6001.constant.ApplicationConstant;
import com.swlc.ScrumPepperCPU6001.entity.AdminEntity;
import com.swlc.ScrumPepperCPU6001.entity.UserEntity;
import com.swlc.ScrumPepperCPU6001.enums.StatusType;
import com.swlc.ScrumPepperCPU6001.exception.AdminException;
import com.swlc.ScrumPepperCPU6001.exception.UserException;
import com.swlc.ScrumPepperCPU6001.repository.AdminRepository;
import com.swlc.ScrumPepperCPU6001.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author hp
 */
@Component
@Log4j2
public class TokenValidator {
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    @Autowired
    public TokenValidator(AdminRepository adminRepository, UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
    }

    public AdminEntity retrieveAdminInformationFromAuthentication() {
        log.info("Execute method retrieveAdminInformationFromAuthentication");
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                Optional<AdminEntity> optionalAdminEntity =
                        adminRepository.findByUsernameAndStatusType(authentication.getName(), StatusType.ACTIVE);
                if (!optionalAdminEntity.isPresent()) {
                    throw new AdminException(ApplicationConstant.RESOURCE_NOT_FOUND, "Invalid admin details");
                }
                return optionalAdminEntity.get();
            }
            throw new AdminException(ApplicationConstant.RESOURCE_NOT_FOUND, "Unable to find admin details");
        } catch (Exception e) {
            log.error("Method retrieveAdminInformationFromAuthentication : " + e.getMessage());
            throw new AdminException(ApplicationConstant.COMMON_ERROR_CODE, e.getMessage());
        }
    }

    public UserEntity retrieveUserInformationFromAuthentication() {
        log.info("Execute method retrieveUserInformationFromAuthentication");
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                Optional<UserEntity> userEntityOptional =
                        userRepository.findByEmailAndStatusType(authentication.getName(), StatusType.ACTIVE);
                if (!userEntityOptional.isPresent()) {
                    throw new UserException(ApplicationConstant.RESOURCE_NOT_FOUND, "User account not found");
                }
                if(userEntityOptional.get().getStatusType().equals(StatusType.DELETE)) {
                    throw new UserException(ApplicationConstant.RESOURCE_NOT_FOUND, "User account not found");
                }
                return userEntityOptional.get();
            }
            throw new UserException(ApplicationConstant.RESOURCE_NOT_FOUND, "Unable to find user details");
        } catch (Exception e) {
            log.error("Method retrieveUserInformationFromAuthentication : " + e.getMessage());
            throw new UserException(ApplicationConstant.COMMON_ERROR_CODE, e.getMessage());
        }
    }

}
