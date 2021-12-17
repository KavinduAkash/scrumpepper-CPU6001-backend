package com.swlc.ScrumPepperCPU6001.controller.admin;

import com.swlc.ScrumPepperCPU6001.dto.AdminDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddAdminRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateAdminRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.CommonResponseDTO;
import com.swlc.ScrumPepperCPU6001.service.AdminService;
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
@RequestMapping("v1/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addAdmin(@RequestBody AddAdminRequestDTO addAdminRequestDTO) {
        boolean result = adminService.addAdmin(addAdminRequestDTO);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Admin user created successfully", null),
                HttpStatus.OK
        );
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateAdmin(@RequestBody UpdateAdminRequestDTO updateAdminRequestDTO) {
        boolean result = adminService.updateAdmin(updateAdminRequestDTO);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Admin user updated successfully", null),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/get-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllAdmins() {
        List<AdminDTO> result = adminService.getAllAdmins();
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "", result),
                HttpStatus.OK
        );
    }



}
