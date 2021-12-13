package com.swlc.ScrumPepperCPU6001.controller.admin;

import com.swlc.ScrumPepperCPU6001.dto.request.AddAdminRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.CommonResponseDTO;
import com.swlc.ScrumPepperCPU6001.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                new CommonResponseDTO(true, "Admin user create successfully", null),
                HttpStatus.OK
        );
    }

}
