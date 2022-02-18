package com.swlc.ScrumPepperCPU6001.controller.user;

import com.swlc.ScrumPepperCPU6001.dto.SprintDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddSprintRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.HandleSppokerRoomRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.HandleUserStoryRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.CommonResponseDTO;
import com.swlc.ScrumPepperCPU6001.service.SppokerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author hp
 */
@RestController
@CrossOrigin
@RequestMapping("v1/sppoker")
public class SpppokerController {

    private final SppokerService sppokerService;

    public SpppokerController(SppokerService sppokerService) {
        this.sppokerService = sppokerService;
    }

    @PostMapping(value = "/room/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createRoom(@RequestBody HandleSppokerRoomRequestDTO handleSppokerRoomRequestDTO) {
        boolean result = sppokerService.createRoom(handleSppokerRoomRequestDTO);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "SPPoker Room created successfully", result),
                HttpStatus.OK
        );
    }

    @PutMapping(value = "/room/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateRoom(@RequestBody HandleSppokerRoomRequestDTO handleSppokerRoomRequestDTO) {
        boolean result = sppokerService.updateRoom(handleSppokerRoomRequestDTO);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "SPPoker Room created successfully", result),
                HttpStatus.OK
        );
    }
}
