package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.request.HandleUserStoryRequestDTO;

/**
 * @author hp
 */
public interface UserStoryService {
    boolean handleUserStory(HandleUserStoryRequestDTO addUserStoryRequestDTO);
}
