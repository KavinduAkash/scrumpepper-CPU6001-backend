package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.SprintDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddSprintRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.MoveUserStoryRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateSprintRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.SprintResponseDTO;

import java.util.List;

/**
 * @author hp
 */
public interface SprintService {
    SprintDTO createSprint(AddSprintRequestDTO addSprintRequestDTO);
    SprintDTO updateSprint(UpdateSprintRequestDTO updateSprintRequestDTO);
    List<SprintResponseDTO> getProjectSprints(long projectId);
    boolean userStoryMove(MoveUserStoryRequestDTO moveUserStoryRequestDTO);
    boolean startSprint(long sprintId);
    boolean endSprint(long sprintId);
}
