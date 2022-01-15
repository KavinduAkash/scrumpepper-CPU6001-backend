package com.swlc.ScrumPepperCPU6001.controller.user;

import com.swlc.ScrumPepperCPU6001.dto.request.AddCorporateEmployeeRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddProjectRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.CommonResponseDTO;
import com.swlc.ScrumPepperCPU6001.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author hp
 */
@RestController
@CrossOrigin
@RequestMapping("v1/project")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createCorporate(@RequestBody AddProjectRequestDTO addProjectRequestDTO) {
        boolean b = projectService.createNewProject(addProjectRequestDTO);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Project created successfully", null),
                HttpStatus.OK
        );
    }

}
