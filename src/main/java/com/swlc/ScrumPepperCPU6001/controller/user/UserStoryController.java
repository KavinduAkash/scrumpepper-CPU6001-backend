package com.swlc.ScrumPepperCPU6001.controller.user;

import com.swlc.ScrumPepperCPU6001.dto.UserStoryDTO;
import com.swlc.ScrumPepperCPU6001.dto.UserStoryLblDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddUserStoryLblRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.HandleUserStoryRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.MoveUserStoryRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateUserStoryStatusRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.CommonResponseDTO;
import com.swlc.ScrumPepperCPU6001.enums.UserStoryStatusType;
import com.swlc.ScrumPepperCPU6001.service.SprintService;
import com.swlc.ScrumPepperCPU6001.service.UserStoryLblService;
import com.swlc.ScrumPepperCPU6001.service.UserStoryService;
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
@RequestMapping("v1/user-story")
public class UserStoryController {

    private final UserStoryService userStoryService;
    private final UserStoryLblService userStoryLblService;
    private final SprintService sprintService;

    @Autowired
    public UserStoryController(UserStoryService userStoryService, UserStoryLblService userStoryLblService, SprintService sprintService) {
        this.userStoryService = userStoryService;
        this.userStoryLblService = userStoryLblService;
        this.sprintService = sprintService;
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity handleUserStory(@RequestBody HandleUserStoryRequestDTO addUserStoryRequestDTO, @RequestParam("id") long id) {
        UserStoryDTO result = userStoryService.handleUserStory(addUserStoryRequestDTO, id);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "User story created successfully", result),
                HttpStatus.OK
        );
    }

    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity handleUserStoryStatus(@RequestBody UpdateUserStoryStatusRequestDTO updateUserStoryStatusRequestDTO) {
        boolean result = userStoryService.updateUserStoryStatus(updateUserStoryStatusRequestDTO);
        String msg = "User story status changed successfully";
        if(updateUserStoryStatusRequestDTO.getStatus().equals(UserStoryStatusType.DELETE)) {
            msg = "User story deleted successfully";
        }
        return new ResponseEntity<>(
                new CommonResponseDTO(true, msg, null),
                HttpStatus.OK
        );
    }

    @PostMapping(value = "/lbl" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addUserStoryLbl(@RequestBody AddUserStoryLblRequestDTO addUserStoryLblRequestDTO) {
        List<UserStoryLblDTO> result = userStoryLblService.createNewUserStoryLbl(addUserStoryLblRequestDTO);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "User story label created successfully", result),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/get-project-lbl" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addUserStoryLbl(@RequestParam long id) {
        List<UserStoryLblDTO> result = userStoryLblService.getAllProjectUserStoryLbl(id);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Project user story labels found successfully", result),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/get-project" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addUserStoryLbl(@RequestParam String id, @RequestParam long corporate) {
        List<UserStoryDTO> result = userStoryService.getProjectBacklog(id, corporate);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "User stories found successfully", result),
                HttpStatus.OK
        );
    }

    @PatchMapping(value = "/move" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity moveUserStory(@RequestBody MoveUserStoryRequestDTO moveUserStoryRequestDTO) {
        boolean result = sprintService.userStoryMove(moveUserStoryRequestDTO);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "User stories moved successfully", result),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/get-sprint" , produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getSprintUserStories(@RequestParam long id) {
        List<UserStoryDTO> result = userStoryService.getSprintUseStories(id);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "User stories found successfully", result),
                HttpStatus.OK
        );
    }

}
