package com.swlc.ScrumPepperCPU6001.dto;

import com.swlc.ScrumPepperCPU6001.enums.SprintStatusType;
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
public class SprintDTO {
    private long id;
    private long projectId;
    private String sprintName;
    private String description;
    private Date createdDate;
    private Date modifiedDate;
    private CorporateEmployeeDTO createdBy;
    private CorporateEmployeeDTO modifiedBy;
    private SprintStatusType statusType;

    @Override
    public String toString() {
        return "SprintDTO{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", sprintName='" + sprintName + '\'' +
                ", description='" + description + '\'' +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                ", createdBy=" + createdBy +
                ", modifiedBy=" + modifiedBy +
                ", statusType=" + statusType +
                '}';
    }
}
