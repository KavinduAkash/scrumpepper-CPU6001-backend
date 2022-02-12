package com.swlc.ScrumPepperCPU6001.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swlc.ScrumPepperCPU6001.enums.SprintUserStoryStatus;
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
@Table(name = "project_sprint_user_story")
public class ProjectSprintUserStoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="project_user_story_id")
    private ProjectUserStoryEntity projectUserStoryEntity;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addedDate;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="project_sprint_id")
    private ProjectSprintEntity projectSprintEntity;
    @Enumerated(EnumType.STRING)
    private SprintUserStoryStatus status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date removedDate;

    public ProjectSprintUserStoryEntity(ProjectUserStoryEntity projectUserStoryEntity, Date addedDate,
                                         ProjectSprintEntity projectSprintEntity, SprintUserStoryStatus status,
                                         Date removedDate) {
        this.projectUserStoryEntity = projectUserStoryEntity;
        this.addedDate = addedDate;
        this.projectSprintEntity = projectSprintEntity;
        this.status = status;
        this.removedDate = removedDate;
    }

    @Override
    public String toString() {
        return "ProjectSprintUserStroryEntity{" +
                "id=" + id +
                ", projectUserStoryEntity=" + projectUserStoryEntity +
                ", addedDate=" + addedDate +
                ", projectSprintEntity=" + projectSprintEntity +
                ", status=" + status +
                ", removedDate=" + removedDate +
                '}';
    }
}
