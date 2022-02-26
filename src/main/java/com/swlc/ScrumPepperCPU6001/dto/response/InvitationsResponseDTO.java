package com.swlc.ScrumPepperCPU6001.dto.response;

import com.swlc.ScrumPepperCPU6001.dto.CorporateDTO;
import com.swlc.ScrumPepperCPU6001.enums.CorporateEmployeeInvitationStatusType;
import lombok.*;

import java.util.Date;

/**
 * @author hp
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InvitationsResponseDTO {
    private long id;
    private Date invitationDate;
    private CorporateDTO corporate;
    private CorporateEmployeeInvitationStatusType status;

    @Override
    public String toString() {
        return "InvitationsResponseDTO{" +
                "id=" + id +
                ", invitationDate=" + invitationDate +
                ", corporate=" + corporate +
                ", status=" + status +
                '}';
    }
}
