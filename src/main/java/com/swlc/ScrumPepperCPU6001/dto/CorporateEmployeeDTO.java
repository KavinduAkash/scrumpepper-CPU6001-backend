package com.swlc.ScrumPepperCPU6001.dto;

import com.swlc.ScrumPepperCPU6001.enums.CorporateAccessStatusType;
import com.swlc.ScrumPepperCPU6001.enums.CorporateAccessType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author hp
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CorporateEmployeeDTO {
    private long id;
    private UserDTO user;
    private CorporateDTO corporate;
    private CorporateAccessType corporateAccessType;
    private Date createdDate;
    private Date modifiedDate;
    private Date acceptedDate;
    private CorporateAccessStatusType statusType;

    @Override
    public String toString() {
        return "CorporateEmployeeDTO{" +
                "id=" + id +
                ", user=" + user +
                ", corporate=" + corporate +
                ", corporateAccessType=" + corporateAccessType +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                ", acceptedDate=" + acceptedDate +
                ", statusType=" + statusType +
                '}';
    }
}
