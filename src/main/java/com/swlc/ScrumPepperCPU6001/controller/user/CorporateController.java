package com.swlc.ScrumPepperCPU6001.controller.user;

import com.swlc.ScrumPepperCPU6001.dto.request.AddCorporateRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateCorporateRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.CommonResponseDTO;
import com.swlc.ScrumPepperCPU6001.service.CorporateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author hp
 */
@RestController
@CrossOrigin
@RequestMapping("v1/corporate")
public class CorporateController {

    private final CorporateService corporateService;

    public CorporateController(CorporateService corporateService) {
        this.corporateService = corporateService;
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createCorporate(@ModelAttribute AddCorporateRequestDTO addCorporateRequestDTO) {
        boolean result = corporateService.createNewCorporate(addCorporateRequestDTO);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Your corporate account created successfully", null),
                HttpStatus.OK
        );
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createCorporate(@ModelAttribute UpdateCorporateRequestDTO updateCorporateRequestDTO) {
        boolean result = corporateService.updateCorporate(updateCorporateRequestDTO);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Your corporate account updated successfully", null),
                HttpStatus.OK
        );
    }

}
