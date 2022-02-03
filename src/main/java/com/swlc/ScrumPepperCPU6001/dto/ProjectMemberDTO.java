package com.swlc.ScrumPepperCPU6001.dto;

import com.swlc.ScrumPepperCPU6001.enums.ProjectMemberStatusType;
import com.swlc.ScrumPepperCPU6001.enums.ScrumRoles;
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
public class ProjectMemberDTO {
    private long id;
    private CorporateEmployeeDTO corporateEmployee;
    private Date assignedDate;
    private Date modifiedDate;
    private ScrumRoles scrumRole;
    private ProjectMemberStatusType statusType;

    @Override
    public String toString() {
        return "ProjectMemberDTO{" +
                "id=" + id +
                ", corporateEmployee=" + corporateEmployee +
                ", assignedDate=" + assignedDate +
                ", modifiedDate=" + modifiedDate +
                ", scrumRole=" + scrumRole +
                ", statusType=" + statusType +
                '}';
    }
}
