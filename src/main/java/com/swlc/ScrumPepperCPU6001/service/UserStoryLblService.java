package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.UserStoryLblDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddUserStoryLblRequestDTO;
import com.swlc.ScrumPepperCPU6001.entity.UserStoryLabelEntity;

import java.util.List;

/**
 * @author hp
 */
public interface UserStoryLblService {
    List<UserStoryLblDTO> createNewUserStoryLbl(AddUserStoryLblRequestDTO addUserStoryLblRequestDTO);
    List<UserStoryLblDTO> getAllProjectUserStoryLbl(long id);
}
