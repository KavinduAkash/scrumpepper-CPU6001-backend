package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.request.AddUserStoryRequestDTO;

/**
 * @author hp
 */
public interface UserStoryService {
    boolean createNewUserStory(AddUserStoryRequestDTO addUserStoryRequestDTO);
}
