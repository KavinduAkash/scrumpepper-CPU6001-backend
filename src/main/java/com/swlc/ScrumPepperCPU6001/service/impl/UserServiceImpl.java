package com.swlc.ScrumPepperCPU6001.service.impl;

import com.swlc.ScrumPepperCPU6001.constant.ApplicationConstant;
import com.swlc.ScrumPepperCPU6001.dto.request.AddUserRequestDTO;
import com.swlc.ScrumPepperCPU6001.entity.UserEntity;
import com.swlc.ScrumPepperCPU6001.enums.StatusType;
import com.swlc.ScrumPepperCPU6001.exception.UserException;
import com.swlc.ScrumPepperCPU6001.repository.UserRepository;
import com.swlc.ScrumPepperCPU6001.service.UserService;
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

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
