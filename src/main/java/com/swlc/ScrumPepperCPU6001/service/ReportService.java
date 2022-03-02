package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.response.BurnDownChartResponseDTO;

/**
 * @author hp
 */
public interface ReportService {
    BurnDownChartResponseDTO getSprintBurnDownChart(long sprintId);
}
