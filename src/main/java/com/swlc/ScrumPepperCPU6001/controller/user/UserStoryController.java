package com.swlc.ScrumPepperCPU6001.controller.user;

import com.swlc.ScrumPepperCPU6001.dto.UserStoryLblDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddUserStoryLblRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.HandleUserStoryRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateUserStoryStatusRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.CommonResponseDTO;
import com.swlc.ScrumPepperCPU6001.enums.UserStoryStatusType;
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
    private final UserStoryLblService addUserStoryRequestDTO;

    @Autowired
    public UserStoryController(UserStoryService userStoryService, UserStoryLblService addUserStoryRequestDTO) {
        this.userStoryService = userStoryService;
        this.addUserStoryRequestDTO = addUserStoryRequestDTO;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity handleUserStory(@RequestBody HandleUserStoryRequestDTO addUserStoryRequestDTO) {
        boolean result = userStoryService.handleUserStory(addUserStoryRequestDTO);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "User story created successfully", null),
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

    @PostMapping(value = "lbl" , consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addUserStoryLbl(@RequestBody AddUserStoryLblRequestDTO addUserStoryLblRequestDTO) {
        List<UserStoryLblDTO> result = addUserStoryRequestDTO.createNewUserStoryLbl(addUserStoryLblRequestDTO);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "User story label created successfully", result),
                HttpStatus.OK
        );
    }
}
