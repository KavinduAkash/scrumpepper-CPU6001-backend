package com.swlc.ScrumPepperCPU6001.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swlc.ScrumPepperCPU6001.enums.SprintStatusType;
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
@Table(name = "project_sprint")
public class ProjectSprintEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="project_id")
    private ProjectEntity projectEntity;
    @Column
    private String sprintName;
    @Column
    private String description;
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
    private SprintStatusType statusType;

    public ProjectSprintEntity(ProjectEntity projectEntity, String sprintName, String description, Date createdDate,
                               Date modifiedDate, CorporateEmployeeEntity assignedBy, CorporateEmployeeEntity modifiedBy,
                               SprintStatusType statusType) {
        this.projectEntity = projectEntity;
        this.sprintName = sprintName;
        this.description = description;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.assignedBy = assignedBy;
        this.modifiedBy = modifiedBy;
        this.statusType = statusType;
    }
}
