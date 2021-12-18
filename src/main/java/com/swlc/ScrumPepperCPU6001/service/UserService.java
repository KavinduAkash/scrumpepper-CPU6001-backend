package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.request.AddUserRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateUserRequestDTO;

/**
 * @author hp
 */
public interface UserService {
    boolean registerNewUser(AddUserRequestDTO addUserRequestDTO);
    boolean checkDetailsEligibility(String action, String value);
    boolean updateUser(UpdateUserRequestDTO updateUserRequestDTO);
    boolean deleteUser();
}
