package com.swlc.ScrumPepperCPU6001.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author hp
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project_user_story_label")
public class ProjectUserStoryLabelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="project_user_story_id")
    private ProjectUserStoryEntity projectUserStoryEntity;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_story_label_id")
    private UserStoryLabelEntity userStoryLabelEntity;

    public ProjectUserStoryLabelEntity(ProjectUserStoryEntity projectUserStoryEntity, UserStoryLabelEntity userStoryLabelEntity) {
        this.projectUserStoryEntity = projectUserStoryEntity;
        this.userStoryLabelEntity = userStoryLabelEntity;
    }

    @Override
    public String toString() {
        return "ProjectUserStoryLabelEntity{" +
                "id=" + id +
                ", projectUserStoryEntity=" + projectUserStoryEntity +
                ", userStoryLabelEntity=" + userStoryLabelEntity +
                '}';
    }
}
