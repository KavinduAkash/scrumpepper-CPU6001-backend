package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.request.AddUserRequestDTO;

/**
 * @author hp
 */
public interface UserService {
    boolean registerNewUser(AddUserRequestDTO addUserRequestDTO);
}
