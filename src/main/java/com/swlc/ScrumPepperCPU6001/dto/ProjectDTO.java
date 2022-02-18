package com.swlc.ScrumPepperCPU6001.dto;

import com.swlc.ScrumPepperCPU6001.enums.ProjectStatusType;
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
public class ProjectDTO {
    private long id;
    private CorporateDTO corporate;
    private String projectName;
    private Date createdDate;
    private Date modifiedDate;
    private CorporateEmployeeDTO createdBy;
    private CorporateEmployeeDTO modifiedBy;
    private ProjectStatusType statusType;
    private String uuid;

    @Override
    public String toString() {
        return "ProjectDTO{" +
                "id=" + id +
                ", corporate=" + corporate +
                ", projectName='" + projectName + '\'' +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                ", createdBy=" + createdBy +
                ", modifiedBy=" + modifiedBy +
                ", statusType=" + statusType +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
