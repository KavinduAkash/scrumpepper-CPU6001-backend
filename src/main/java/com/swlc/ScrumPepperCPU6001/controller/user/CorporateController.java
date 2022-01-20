package com.swlc.ScrumPepperCPU6001.controller.user;

import com.swlc.ScrumPepperCPU6001.dto.MyCorporateDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.*;
import com.swlc.ScrumPepperCPU6001.dto.response.CommonResponseDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.GetCorporateDetailsResponseDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.UploadImageResponseDTO;
import com.swlc.ScrumPepperCPU6001.service.CorporateService;
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
@RequestMapping("v1/corporate")
public class CorporateController {

    private final CorporateService corporateService;

    public CorporateController(CorporateService corporateService) {
        this.corporateService = corporateService;
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createCorporate(@RequestBody AddCorporateRequestDTO addCorporateRequestDTO) {
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

    @DeleteMapping(value = "/remove", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteCorporate(@RequestBody DeleteCorporateRequestDTO deleteCorporateRequestDTO) {
        boolean result = corporateService.deleteCorporate(deleteCorporateRequestDTO);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Your corporate account deleted successfully", null),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/my-corporates", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getMyCorporates() {
        List<MyCorporateDTO> result = corporateService.getMyCorporates();
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Found your corporates successfully", result),
                HttpStatus.OK
        );
    }

    @PostMapping(value = "/A2b4-8a486269971ed2326920", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity uploadCorporateLogo(@ModelAttribute UploadImageRequestDTO uploadImageRequestDTO) {
        String path = corporateService.uploadCorporateLogo(uploadImageRequestDTO.getFile());
        return new ResponseEntity<>(
                new UploadImageResponseDTO(
                        "xxx.png",
                        "done",
                        "https://cdn.arstechnica.net/wp-content/uploads/2016/02/5718897981_10faa45ac3_b-640x624.jpg",
                        path
                ),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/corporates-details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCorporateDetails(@RequestBody GetCorporateDetailsRequestDTO getCorporateDetailsRequestDTO) {
        GetCorporateDetailsResponseDTO result = corporateService.getCorporateDetails(getCorporateDetailsRequestDTO);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Found your corporates successfully", result),
                HttpStatus.OK
        );
    }

}
