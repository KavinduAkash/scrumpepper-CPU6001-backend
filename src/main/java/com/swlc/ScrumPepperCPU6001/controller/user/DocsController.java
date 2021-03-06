package com.swlc.ScrumPepperCPU6001.controller.user;

import com.swlc.ScrumPepperCPU6001.dto.ProjectDocDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddProjectRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.CreateDocRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateDocRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.CommonResponseDTO;
import com.swlc.ScrumPepperCPU6001.service.DocsService;
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
@RequestMapping("v1/docs")
public class DocsController {

    private final DocsService docsService;

    @Autowired
    public DocsController(DocsService docsService) {
        this.docsService = docsService;
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createDoc(@RequestBody CreateDocRequestDTO createDocRequestDTO) {
        ProjectDocDTO result = docsService.createDoc(createDocRequestDTO);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Document created successfully", result),
                HttpStatus.OK
        );
    }

    @PatchMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateDoc(@RequestBody UpdateDocRequestDTO updateDocRequestDTO) {
        ProjectDocDTO result = docsService.updateDoc(updateDocRequestDTO);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Document updated successfully", result),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getDocs(@RequestParam long id) {
        List<ProjectDocDTO> result = docsService.getDocs(id);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Documents found successfully", result),
                HttpStatus.OK
        );
    }
}
