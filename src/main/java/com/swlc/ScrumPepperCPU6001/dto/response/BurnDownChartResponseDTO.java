package com.swlc.ScrumPepperCPU6001.dto.response;

import com.swlc.ScrumPepperCPU6001.dto.BurnDownDataDTO;
import com.swlc.ScrumPepperCPU6001.dto.SprintDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author hp
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BurnDownChartResponseDTO {
    private SprintDTO sprint;
    private List<BurnDownDataDTO> data;

    @Override
    public String toString() {
        return "BurnDownChartResponseDTO{" +
                "sprint=" + sprint +
                ", data=" + data +
                '}';
    }
}
