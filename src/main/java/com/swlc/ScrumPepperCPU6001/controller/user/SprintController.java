package com.swlc.ScrumPepperCPU6001.controller.user;

import com.swlc.ScrumPepperCPU6001.dto.SprintDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddSprintRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateSprintRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.CommonResponseDTO;
import com.swlc.ScrumPepperCPU6001.service.SprintService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author hp
 */
@RestController
@CrossOrigin
@RequestMapping("v1/sprint")
public class SprintController {

    private final SprintService sprintService;

    public SprintController(SprintService sprintService) {
        this.sprintService = sprintService;
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addSprint(@RequestBody AddSprintRequestDTO addSprintRequestDTO) {
        SprintDTO sprint = sprintService.createSprint(addSprintRequestDTO);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Sprint created successfully", sprint),
                HttpStatus.OK
        );
    }

    @PatchMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateSprint(@RequestBody UpdateSprintRequestDTO updateSprintRequestDTO) {
        SprintDTO sprint = sprintService.updateSprint(updateSprintRequestDTO);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Sprint updated successfully", sprint),
                HttpStatus.OK
        );
    }
}
