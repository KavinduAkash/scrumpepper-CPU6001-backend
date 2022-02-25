package com.swlc.ScrumPepperCPU6001.dto.response;

import com.swlc.ScrumPepperCPU6001.enums.StatusType;
import lombok.*;

import java.util.Date;

/**
 * @author hp
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class GetUserSearchResponseDTO {
    private long id;
    private String refNo;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String email;
    private Date createdDate;
    private StatusType statusType;
    private boolean isYourCorporate;
}
