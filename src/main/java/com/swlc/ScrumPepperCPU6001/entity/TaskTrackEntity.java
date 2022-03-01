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
@Table(name = "task_track")
public class TaskTrackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="project_task_id")
    private ProjectTaskEntity projectTaskEntity;
    @Enumerated(EnumType.STRING)
    private UserStoryStatusType statusType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date trackedDate;

    public TaskTrackEntity(ProjectTaskEntity projectTaskEntity, UserStoryStatusType statusType, Date trackedDate) {
        this.projectTaskEntity = projectTaskEntity;
        this.statusType = statusType;
        this.trackedDate = trackedDate;
    }

    @Override
    public String toString() {
        return "TaskTrackEntity{" +
                "id=" + id +
                ", projectTaskEntity=" + projectTaskEntity +
                ", statusType=" + statusType +
                ", trackedDate=" + trackedDate +
                '}';
    }
}
