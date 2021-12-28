package com.swlc.ScrumPepperCPU6001.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swlc.ScrumPepperCPU6001.enums.ProjectMemberStatusType;
import com.swlc.ScrumPepperCPU6001.enums.ScrumRoles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @author hp
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project_member")
public class ProjectMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="project_id")
    private ProjectEntity projectEntity;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="corporate_employee_id")
    private CorporateEmployeeEntity corporateEmployeeEntity;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date assignedDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedDate;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="assigned_by")
    private CorporateEmployeeEntity assignedBy;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="modified_by")
    private CorporateEmployeeEntity modifiedBy;
    @Enumerated(EnumType.STRING)
    private ScrumRoles scrumRole;
    @Enumerated(EnumType.STRING)
    private ProjectMemberStatusType statusType;

    public ProjectMemberEntity(ProjectEntity projectEntity, CorporateEmployeeEntity corporateEmployeeEntity,
                               Date assignedDate, Date modifiedDate, CorporateEmployeeEntity assignedBy,
                               CorporateEmployeeEntity modifiedBy, ScrumRoles scrumRole,
                               ProjectMemberStatusType statusType) {
        this.projectEntity = projectEntity;
        this.corporateEmployeeEntity = corporateEmployeeEntity;
        this.assignedDate = assignedDate;
        this.modifiedDate = modifiedDate;
        this.assignedBy = assignedBy;
        this.modifiedBy = modifiedBy;
        this.scrumRole = scrumRole;
        this.statusType = statusType;
    }

    @Override
    public String toString() {
        return "ProjectMemberEntity{" +
                "id=" + id +
                ", projectEntity=" + projectEntity +
                ", corporateEmployeeEntity=" + corporateEmployeeEntity +
                ", assignedDate=" + assignedDate +
                ", modifiedDate=" + modifiedDate +
                ", assignedBy=" + assignedBy +
                ", modifiedBy=" + modifiedBy +
                ", scrumRole=" + scrumRole +
                ", statusType=" + statusType +
                '}';
    }
}
