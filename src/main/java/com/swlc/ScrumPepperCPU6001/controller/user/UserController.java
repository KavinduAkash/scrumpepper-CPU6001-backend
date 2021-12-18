package com.swlc.ScrumPepperCPU6001.controller.user;

import com.swlc.ScrumPepperCPU6001.dto.request.AddUserRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateUserRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.CommonResponseDTO;
import com.swlc.ScrumPepperCPU6001.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author hp
 */
@RestController
@CrossOrigin
@RequestMapping("v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity registerNewUser(@RequestBody AddUserRequestDTO addUserRequestDTO) {
        boolean result = userService.registerNewUser(addUserRequestDTO);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Your account created successfully", null),
                HttpStatus.OK
        );
    }

    @PostMapping(value = "/check/{action}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity checkUserDetailsEligibility(@PathVariable String action, @RequestParam("val") String value) {
        boolean result = userService.checkDetailsEligibility(action, value);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "", null),
                HttpStatus.OK
        );
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateUser(@RequestBody UpdateUserRequestDTO updateUserRequestDTO) {
        boolean result = userService.updateUser(updateUserRequestDTO);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Your account updated successfully", null),
                HttpStatus.OK
        );
    }

}
