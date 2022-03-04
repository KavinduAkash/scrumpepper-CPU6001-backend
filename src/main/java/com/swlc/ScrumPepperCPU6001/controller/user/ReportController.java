package com.swlc.ScrumPepperCPU6001.controller.user;

import com.swlc.ScrumPepperCPU6001.dto.SprintVelocityDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.BurnDownChartResponseDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.CommonResponseDTO;
import com.swlc.ScrumPepperCPU6001.service.ReportService;
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
@RequestMapping("v1/project-report")
public class ReportController {
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(value = "/burndown/{sprintId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getBurnDown(@PathVariable long sprintId) {
        BurnDownChartResponseDTO sprintBurnDownChart = reportService.getSprintBurnDownChart(sprintId);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "SPPoker Rooms found successfully", sprintBurnDownChart),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/burnup/{sprintId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getBurnUp(@PathVariable long sprintId) {
        BurnDownChartResponseDTO sprintBurnDownChart = reportService.getSprintBurnUpChart(sprintId);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "SPPoker Rooms found successfully", sprintBurnDownChart),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/sprint-velocity/{projectId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getSpritVelocity(@PathVariable long projectId) {
        List<SprintVelocityDTO> result = reportService.getSprintVelocity(projectId);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "SPPoker Rooms found successfully", result),
                HttpStatus.OK
        );
    }
}
