package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.UserDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddUserRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateUserRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.GetUserSearchResponseDTO;

import java.util.List;

/**
 * @author hp
 */
public interface UserService {
    UserDTO getUserDetailsByEmail(String email);
    UserDTO getUserDetailsByToken();
    boolean registerNewUser(AddUserRequestDTO addUserRequestDTO);
    boolean checkDetailsEligibility(String action, String value);
    boolean updateUser(UpdateUserRequestDTO updateUserRequestDTO);
    boolean deleteUser();
    List<GetUserSearchResponseDTO> searchUser(String keyword, long corporateId, long projectId);
}
