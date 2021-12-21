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
public class AddCorporateEmployeeRequestDTO {
    private long userId;
    private long corporateId;
    private String email;

    @Override
    public String toString() {
        return "AddCorporateEmployeeRequestDTO{" +
                "userId=" + userId +
                ", corporateId=" + corporateId +
                ", email='" + email + '\'' +
                '}';
    }
}
