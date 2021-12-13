package com.swlc.ScrumPepperCPU6001.service.impl;

import com.swlc.ScrumPepperCPU6001.constant.ApplicationConstant;
import com.swlc.ScrumPepperCPU6001.dto.AdminDTO;
import com.swlc.ScrumPepperCPU6001.entity.AdminEntity;
import com.swlc.ScrumPepperCPU6001.exception.AdminException;
import com.swlc.ScrumPepperCPU6001.repository.AdminRepository;
import com.swlc.ScrumPepperCPU6001.service.AdminService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author hp
 */
@Service
@Log4j2
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

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
                    byUsername.get().getCreatedDate()
            );
        } catch (Exception e) {
            log.error("Method getAdminDetailsByUserName : " + e.getMessage(), e);
            throw e;
        }
    }
}
