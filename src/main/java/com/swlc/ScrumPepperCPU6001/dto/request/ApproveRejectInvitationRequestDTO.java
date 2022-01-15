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
public class ApproveRejectInvitationRequestDTO {
    private long invitationId;
    private String invitationStatus;

    @Override
    public String toString() {
        return "ApproveRejectInvitationRequestDTO{" +
                "invitationId=" + invitationId +
                ", invitationStatus='" + invitationStatus + '\'' +
                '}';
    }
}
