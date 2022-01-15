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
@Table(name = "user_story_label")
public class UserStoryLabelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="project_id")
    private ProjectEntity projectEntity;
    @Column(nullable = false)
    private String label;

    public UserStoryLabelEntity(ProjectEntity projectEntity, String label) {
        this.projectEntity = projectEntity;
        this.label = label;
    }

    @Override
    public String toString() {
        return "UserStoryLabelEntity{" +
                "id=" + id +
                ", projectEntity=" + projectEntity +
                ", label='" + label + '\'' +
                '}';
    }
}
