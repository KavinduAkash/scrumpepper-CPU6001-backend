package com.swlc.ScrumPepperCPU6001.service.impl;

import com.swlc.ScrumPepperCPU6001.constant.ApplicationConstant;
import com.swlc.ScrumPepperCPU6001.dto.UserDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddUserRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateUserRequestDTO;
import com.swlc.ScrumPepperCPU6001.entity.UserEntity;
import com.swlc.ScrumPepperCPU6001.enums.StatusType;
import com.swlc.ScrumPepperCPU6001.exception.UserException;
import com.swlc.ScrumPepperCPU6001.repository.UserRepository;
import com.swlc.ScrumPepperCPU6001.service.UserService;
import com.swlc.ScrumPepperCPU6001.util.TokenValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * @author hp
 */
@Service
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenValidator tokenValidator;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO getUserDetailsByEmail(String email) {
        log.info("Execute method getUserDetailsByEmail : email : " + email);
        try {
            Optional<UserEntity> byEmail = userRepository.findByEmail(email);
            if(!byEmail.isPresent()) throw new UserException(ApplicationConstant.RESOURCE_NOT_FOUND, "User not found");
            return new UserDTO(
                    byEmail.get().getId(),
                    byEmail.get().getRefNo(),
                    byEmail.get().getFirstName(),
                    byEmail.get().getLastName(),
                    byEmail.get().getContactNumber(),
                    byEmail.get().getEmail(),
                    null,
                    null,
                    byEmail.get().getStatusType()
            );
        } catch (Exception e) {
            log.error("Method getUserDetailsByEmail : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean registerNewUser(AddUserRequestDTO addUserRequestDTO) {
        log.info("Execute method registerNewUser : addUserRequestDTO : " + addUserRequestDTO.toString());
        try {
            Optional<UserEntity> byEmail = userRepository.findByEmail(addUserRequestDTO.getEmail());
            if(byEmail.isPresent())
                throw new UserException(ApplicationConstant.RESOURCE_ALREADY_EXIST, "This email already taken");
            userRepository.save(
                    new UserEntity(
                            addUserRequestDTO.getRef(),
                            addUserRequestDTO.getFirstName(),
                            addUserRequestDTO.getLastName(),
                            addUserRequestDTO.getContactNumber(),
                            addUserRequestDTO.getEmail(),
                            passwordEncoder.encode(addUserRequestDTO.getPassword()),
                            new Date(),
                            StatusType.ACTIVE
                    ));
            return true;
        } catch (Exception e) {
            log.error("Method registerNewUser : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean checkDetailsEligibility(String action, String value) {
        log.info("Execute method checkDetailsEligibility : value : " + value);
        try {
            switch (action) {
                case "email":
                    Optional<UserEntity> byEmail = userRepository.findByEmail(value);
                    if(byEmail.isPresent())
                        throw new UserException(ApplicationConstant.RESOURCE_ALREADY_EXIST, "This email already taken");
                    break;
                case "ref":
                    Optional<UserEntity> byRefNo = userRepository.findByRefNo(value);
                    if(byRefNo.isPresent())
                        throw new UserException(ApplicationConstant.RESOURCE_ALREADY_EXIST, "This ref already taken");
                    break;
            }
            return true;
        } catch (Exception e) {
            log.error("Method checkDetailsEligibility : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean updateUser(UpdateUserRequestDTO updateUserRequestDTO) {
        log.info("Execute method UpdateUserRequestDTO : updateUserRequestDTO : " + updateUserRequestDTO.toString());
        try {
            Optional<UserEntity> byId = userRepository.findById(updateUserRequestDTO.getId());
            if(!byId.isPresent())
                throw new UserException(ApplicationConstant.RESOURCE_NOT_FOUND, "User account not found");
            UserEntity userEntity = byId.get();
            userEntity.setFirstName(updateUserRequestDTO.getFirstName());
            userEntity.setLastName(updateUserRequestDTO.getLastName());
            userEntity.setRefNo(updateUserRequestDTO.getRefNo());
            userEntity.setContactNumber(updateUserRequestDTO.getContactNumber());
            userEntity.setStatusType(updateUserRequestDTO.getStatusType());
            if(updateUserRequestDTO.getPassword()!=null)
                userEntity.setPassword( passwordEncoder.encode(updateUserRequestDTO.getPassword()));
            userRepository.save(userEntity);
            return true;
        } catch (Exception e) {
            log.error("Method UpdateUserRequestDTO : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean deleteUser() {
        log.info("Execute method deleteUser : ");
        try {
            UserEntity userEntity = tokenValidator.retrieveUserInformationFromAuthentication();
            userEntity.setStatusType(StatusType.DELETE);
            userRepository.save(userEntity);
            return true;
        } catch (Exception e) {
            log.error("Method deleteUser : " + e.getMessage(), e);
            throw e;
        }
    }
}
