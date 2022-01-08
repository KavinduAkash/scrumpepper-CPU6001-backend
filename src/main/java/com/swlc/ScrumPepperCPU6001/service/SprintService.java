package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.SprintDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddSprintRequestDTO;

/**
 * @author hp
 */
public interface SprintService {
    SprintDTO createSprint(AddSprintRequestDTO addSprintRequestDTO);
}
