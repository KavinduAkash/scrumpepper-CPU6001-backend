package com.swlc.ScrumPepperCPU6001.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author hp
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchEmployeeRequestDTO {
    private long corporateId;
    private String search;

    @Override
    public String toString() {
        return "SearchEmployeeRequestDTO{" +
                "corporateId=" + corporateId +
                ", search='" + search + '\'' +
                '}';
    }
}
