package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.AdminDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddAdminRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateAdminRequestDTO;

import java.util.List;

/**
 * @author hp
 */
public interface AdminService {
    AdminDTO getAdminDetailsByUserName(String username);
    boolean addAdmin(AddAdminRequestDTO addAdminRequestDTO);
    boolean updateAdmin(UpdateAdminRequestDTO updateAdminRequestDTO);
}
