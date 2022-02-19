package com.swlc.ScrumPepperCPU6001.controller.user;

import com.swlc.ScrumPepperCPU6001.dto.ProjectDocDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddProjectRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.CreateDocRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.CommonResponseDTO;
import com.swlc.ScrumPepperCPU6001.service.DocsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author hp
 */
@RestController
@CrossOrigin
@RequestMapping("v1/docs")
public class DocsController {

    private final DocsService docsService;

    @Autowired
    public DocsController(DocsService docsService) {
        this.docsService = docsService;
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createCorporate(@RequestBody CreateDocRequestDTO createDocRequestDTO) {
        ProjectDocDTO result = docsService.createDoc(createDocRequestDTO);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Document created successfully", result),
                HttpStatus.OK
        );
    }
}
