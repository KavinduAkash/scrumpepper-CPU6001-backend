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
    private StatusType statusType;

    public ProjectTaskAssignsEntity(ProjectTaskEntity projectTaskEntity, ProjectMemberEntity projectMemberEntity,
                                    Date createdDate, Date modifiedDate, StatusType statusType) {
        this.projectTaskEntity = projectTaskEntity;
        this.projectMemberEntity = projectMemberEntity;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
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
                ", statusType=" + statusType +
                '}';
    }
}
