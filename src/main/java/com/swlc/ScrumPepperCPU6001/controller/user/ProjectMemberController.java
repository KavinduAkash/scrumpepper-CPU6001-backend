package com.swlc.ScrumPepperCPU6001.controller.user;

import com.swlc.ScrumPepperCPU6001.dto.request.AddProjectMemberDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddProjectRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateProjectMemberDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.CommonResponseDTO;
import com.swlc.ScrumPepperCPU6001.service.ProjectMemberService;
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
@RequestMapping("v1/project-member")
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    @Autowired
    public ProjectMemberController(ProjectMemberService projectMemberService) {
        this.projectMemberService = projectMemberService;
    }

    @PatchMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addProjectMember(@RequestBody AddProjectMemberDTO addProjectMemberDTO) {
        boolean b = projectMemberService.addProjectMembers(addProjectMemberDTO);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Project member added successfully", null),
                HttpStatus.OK
        );
    }

    @PatchMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateProjectMember(@RequestBody UpdateProjectMemberDTO updateProjectMemberDTO) {
        boolean b = projectMemberService.updateProjectMember(updateProjectMemberDTO);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Project member updated successfully", null),
                HttpStatus.OK
        );
    }

    @DeleteMapping(value = "/remove", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity removeProjectMember(@RequestParam long id) {
        boolean b = projectMemberService.removeProjectMember(id);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Project member removed successfully", null),
                HttpStatus.OK
        );
    }
}