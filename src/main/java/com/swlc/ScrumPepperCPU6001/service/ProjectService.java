package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.request.AddProjectRequestDTO;

/**
 * @author hp
 */
public interface ProjectService {
    boolean createNewProject(AddProjectRequestDTO addProjectRequestDTO);
}
