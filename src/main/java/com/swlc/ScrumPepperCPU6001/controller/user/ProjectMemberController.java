package com.swlc.ScrumPepperCPU6001.controller.user;

import com.swlc.ScrumPepperCPU6001.dto.CorporateEmployeeDTO;
import com.swlc.ScrumPepperCPU6001.dto.ProjectMemberDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddProjectMemberDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateProjectMemberDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.CommonResponseDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.GetTaskEmployeeDTO;
import com.swlc.ScrumPepperCPU6001.service.ProjectMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PatchMapping(value = "/add/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addProjectMemberAsUser(@RequestBody AddProjectMemberDTO addProjectMemberDTO) {
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

    @GetMapping(value = "/project/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getProjectMember(@PathVariable long id) {
        List<CorporateEmployeeDTO> result = projectMemberService.getProjectMember(id);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Project members found successfully", result),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/task/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTaskMember(@PathVariable long id) {
        GetTaskEmployeeDTO result = projectMemberService.getTaskMembers(id);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Project members found successfully", result),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/get-team", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getProjectTeam(@RequestParam("id") long projectId) {
        List<ProjectMemberDTO> result = projectMemberService.getProjectTeam(projectId);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Project team found successfully", result),
                HttpStatus.OK
        );
    }

}
