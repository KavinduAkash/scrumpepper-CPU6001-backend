package com.swlc.ScrumPepperCPU6001.controller.user;

import com.swlc.ScrumPepperCPU6001.dto.CorporateEmployeeDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddCorporateEmployeeRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddCorporateRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.ApproveRejectInvitationRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.SearchEmployeeRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.CommonResponseDTO;
import com.swlc.ScrumPepperCPU6001.service.CorporateEmployeeService;
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
@RequestMapping("v1/corporate/employee")
public class CorporateEmployeeController {

    private final CorporateEmployeeService corporateEmployeeService;

    public CorporateEmployeeController(CorporateEmployeeService corporateEmployeeService) {
        this.corporateEmployeeService = corporateEmployeeService;
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createCorporate(@RequestBody AddCorporateEmployeeRequestDTO addCorporateEmployeeRequestDTO) {
        boolean b = corporateEmployeeService.addCorporateEmployee(addCorporateEmployeeRequestDTO);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Your corporate employee added successfully", null),
                HttpStatus.OK
        );
    }

    @PostMapping(value = "/approve", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity approveRejectCorporateEmployeeInvitations(@RequestBody ApproveRejectInvitationRequestDTO approveRejectInvitationRequestDTO) {
        boolean b = corporateEmployeeService.approveRejectCorporateEmployeeInvitation(approveRejectInvitationRequestDTO);
        String msg = "rejected";
        if(approveRejectInvitationRequestDTO.getInvitationStatus().equals("ACCEPTED"))
            msg = "accepted";
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "You " + msg + " the invitation successfully", null),
                HttpStatus.OK
        );
    }

    @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity searchCorporateEmployee(@RequestBody SearchEmployeeRequestDTO searchEmployeeRequestDTO) {
        List<CorporateEmployeeDTO> result = corporateEmployeeService.searchCorporateEmployee(searchEmployeeRequestDTO);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Found corporate employee successfully", result),
                HttpStatus.OK
        );
    }
}
