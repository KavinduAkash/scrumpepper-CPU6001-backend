package com.swlc.ScrumPepperCPU6001.dto.response.ProjectMemberResponse;

import com.swlc.ScrumPepperCPU6001.dto.CorporateEmployeeDTO;
import com.swlc.ScrumPepperCPU6001.entity.CorporateEmployeeEntity;
import com.swlc.ScrumPepperCPU6001.enums.ProjectMemberStatusType;
import com.swlc.ScrumPepperCPU6001.enums.ScrumRoles;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * @author hp
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProjectMemberResponseDTO {
    private long id;
    private long corporateEmployeeId;
    private Date assignedDate;
    private Date modifiedDate;
    private CorporateEmployeeDTO assignedBy;
    private CorporateEmployeeDTO modifiedBy;
    private ScrumRoles scrumRole;
    private ProjectMemberStatusType statusType;
}
