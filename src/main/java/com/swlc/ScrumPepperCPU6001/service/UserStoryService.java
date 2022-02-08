package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.UserStoryDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.HandleUserStoryRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateUserStoryStatusRequestDTO;

import java.util.List;

/**
 * @author hp
 */
public interface UserStoryService {
    UserStoryDTO handleUserStory(HandleUserStoryRequestDTO addUserStoryRequestDTO);
    boolean updateUserStoryStatus(UpdateUserStoryStatusRequestDTO updateUserStoryStatusRequestDTO);
    List<UserStoryDTO> getProjectBacklog(long id, long corporateId);
}
