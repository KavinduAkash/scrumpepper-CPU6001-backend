package com.swlc.ScrumPepperCPU6001.dto.response;

import com.swlc.ScrumPepperCPU6001.dto.CorporateEmployeeDTO;
import com.swlc.ScrumPepperCPU6001.dto.UserStoryDTO;
import com.swlc.ScrumPepperCPU6001.enums.SprintStatusType;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * @author hp
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SprintResponseDTO {
    private long id;
    private long projectId;
    private String sprintName;
    private String description;
    private String startDate;
    private String endDate;
    private Date createdDate;
    private Date modifiedDate;
    private CorporateEmployeeDTO createdBy;
    private CorporateEmployeeDTO modifiedBy;
    private SprintStatusType statusType;
    private List<UserStoryDTO> userStory;
}
