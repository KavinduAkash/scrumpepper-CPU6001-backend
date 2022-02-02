package com.swlc.ScrumPepperCPU6001.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swlc.ScrumPepperCPU6001.enums.UserStoryStatusType;
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
@Table(name = "project_task")
public class ProjectTaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="project_id")
    private ProjectEntity projectEntity;
    @Column
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedDate;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="created_by")
    private CorporateEmployeeEntity createdBy;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="modified_by")
    private CorporateEmployeeEntity modifiedBy;
    @Enumerated(EnumType.STRING)
    private UserStoryStatusType statusType;

    public ProjectTaskEntity(ProjectEntity projectEntity, String title, Date modifiedDate, CorporateEmployeeEntity createdBy,
                             CorporateEmployeeEntity modifiedBy, UserStoryStatusType statusType) {
        this.projectEntity = projectEntity;
        this.title = title;
        this.modifiedDate = modifiedDate;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.statusType = statusType;
    }

    @Override
    public String toString() {
        return "ProjectTaskEntity{" +
                "id=" + id +
                ", projectEntity=" + projectEntity +
                ", title='" + title + '\'' +
                ", modifiedDate=" + modifiedDate +
                ", createdBy=" + createdBy +
                ", modifiedBy=" + modifiedBy +
                ", statusType=" + statusType +
                '}';
    }
}
