package com.swlc.ScrumPepperCPU6001.service.impl;

import com.swlc.ScrumPepperCPU6001.constant.ApplicationConstant;
import com.swlc.ScrumPepperCPU6001.dto.AdminDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddAdminRequestDTO;
import com.swlc.ScrumPepperCPU6001.entity.AdminEntity;
import com.swlc.ScrumPepperCPU6001.enums.AdminType;
import com.swlc.ScrumPepperCPU6001.exception.AdminException;
import com.swlc.ScrumPepperCPU6001.repository.AdminRepository;
import com.swlc.ScrumPepperCPU6001.service.AdminService;
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
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public AdminDTO getAdminDetailsByUserName(String username) {
        log.info("Execute method getAdminDetailsByUserName : username : " + username);
        try {
            Optional<AdminEntity> byUsername = adminRepository.findByUsername(username);
            if(!byUsername.isPresent())
                throw new AdminException(ApplicationConstant.RESOURCE_NOT_FOUND, "Admin not found");
            return new AdminDTO(
                    byUsername.get().getId(),
                    byUsername.get().getFirstName(),
                    byUsername.get().getLastName(),
                    byUsername.get().getEmail(),
                    byUsername.get().getUsername(),
                    byUsername.get().getContactNumber(),
                    byUsername.get().getEmployeeId(),
                    byUsername.get().getAdminType(),
                    byUsername.get().getCreatedDate()
            );
        } catch (Exception e) {
            log.error("Method getAdminDetailsByUserName : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean addAdmin(AddAdminRequestDTO addAdminRequestDTO) {
        log.info("Execute method addAdmin : addAdminRequestDTO : " + addAdminRequestDTO.toString());
        try {
            Optional<AdminEntity> byUsername = adminRepository.findByUsername(addAdminRequestDTO.getUsername());
            Optional<AdminEntity> byEmail = adminRepository.findByEmail(addAdminRequestDTO.getEmail());
            Optional<AdminEntity> byContactNumber = adminRepository.findByContactNumber(addAdminRequestDTO.getContactNumber());
            if(byUsername.isPresent())
                throw new AdminException(ApplicationConstant.RESOURCE_ALREADY_EXIST, "Username already exist");
            if(byEmail.isPresent())
                throw new AdminException(ApplicationConstant.RESOURCE_ALREADY_EXIST, "Email already exist");
            if(byContactNumber.isPresent())
                throw new AdminException(ApplicationConstant.RESOURCE_ALREADY_EXIST, "Contact already exist");
            adminRepository.save(
                    new AdminEntity(
                            addAdminRequestDTO.getFirstName(),
                            addAdminRequestDTO.getLastName(),
                            addAdminRequestDTO.getEmployeeId(),
                            addAdminRequestDTO.getEmail(),
                            addAdminRequestDTO.getUsername(),
                            passwordEncoder.encode(addAdminRequestDTO.getPassword()),
                            addAdminRequestDTO.getContactNumber(),
                            addAdminRequestDTO.getAdminType(),
                            addAdminRequestDTO.getCreatedDate()
                    )
            );
            return true;
        } catch (Exception e) {
            log.error("Method addAdmin : " + e.getMessage(), e);
            throw e;
        }
    }
}
