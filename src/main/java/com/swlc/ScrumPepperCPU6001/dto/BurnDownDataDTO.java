package com.swlc.ScrumPepperCPU6001.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author hp
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BurnDownDataDTO {
    private LocalDate date;
    private String label;
    private int remainingEffortPoints;
    private int idealPoints;

    @Override
    public String toString() {
        return "BurnDownDataDTO{" +
                "date=" + date +
                ", label='" + label + '\'' +
                ", remainingEffortPoints=" + remainingEffortPoints +
                ", idealPoints=" + idealPoints +
                '}';
    }
}
