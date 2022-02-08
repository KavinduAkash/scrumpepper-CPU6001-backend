package com.swlc.ScrumPepperCPU6001.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swlc.ScrumPepperCPU6001.enums.StatusType;
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
@Table(name = "project_task_assigns")
public class ProjectTaskAssignsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="project_task_id")
    private ProjectTaskEntity projectTaskEntity;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="project_member_id")
    private ProjectMemberEntity projectMemberEntity;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedDate;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="assigned_by")
    private CorporateEmployeeEntity assignedBy;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="modified_by")
    private CorporateEmployeeEntity modifiedBy;
    @Enumerated(EnumType.STRING)
    private StatusType statusType;

    public ProjectTaskAssignsEntity(ProjectTaskEntity projectTaskEntity, ProjectMemberEntity projectMemberEntity,
                                    Date createdDate, Date modifiedDate, CorporateEmployeeEntity assignedBy,
                                    CorporateEmployeeEntity modifiedBy, StatusType statusType) {
        this.projectTaskEntity = projectTaskEntity;
        this.projectMemberEntity = projectMemberEntity;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.assignedBy = assignedBy;
        this.modifiedBy = modifiedBy;
        this.statusType = statusType;
    }

    @Override
    public String toString() {
        return "ProjectTaskAssignsEntity{" +
                "id=" + id +
                ", projectTaskEntity=" + projectTaskEntity +
                ", projectMemberEntity=" + projectMemberEntity +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                ", assignedBy=" + assignedBy +
                ", modifiedBy=" + modifiedBy +
                ", statusType=" + statusType +
                '}';
    }
}
