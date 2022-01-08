package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.SprintDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddSprintRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateSprintRequestDTO;

/**
 * @author hp
 */
public interface SprintService {
    SprintDTO createSprint(AddSprintRequestDTO addSprintRequestDTO);
    SprintDTO updateSprint(UpdateSprintRequestDTO updateSprintRequestDTO);
}
