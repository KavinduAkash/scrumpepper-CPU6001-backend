package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.SprintVelocityDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.BurnDownChartResponseDTO;

import java.util.List;

/**
 * @author hp
 */
public interface ReportService {
    BurnDownChartResponseDTO getSprintBurnDownChart(long sprintId);
    BurnDownChartResponseDTO getSprintBurnUpChart(long sprintId);
    List<SprintVelocityDTO> getSprintVelocity(long projectId);
}
