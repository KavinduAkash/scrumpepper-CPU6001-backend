package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.request.HandleUserStoryRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateUserStoryStatusRequestDTO;

/**
 * @author hp
 */
public interface UserStoryService {
    boolean handleUserStory(HandleUserStoryRequestDTO addUserStoryRequestDTO);
    boolean updateUserStoryStatus(UpdateUserStoryStatusRequestDTO updateUserStoryStatusRequestDTO);
}
