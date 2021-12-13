package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.AdminDTO;

/**
 * @author hp
 */
public interface AdminService {
    AdminDTO getAdminDetailsByUserName(String username);
}
