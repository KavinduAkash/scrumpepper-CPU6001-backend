package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.request.AddProjectRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.GetCorporateDetailsResponseDTO;

import java.util.List;

/**
 * @author hp
 */
public interface ProjectService {
    boolean createNewProject(AddProjectRequestDTO addProjectRequestDTO);
    List<GetCorporateDetailsResponseDTO> getAllMyProjects();
}
