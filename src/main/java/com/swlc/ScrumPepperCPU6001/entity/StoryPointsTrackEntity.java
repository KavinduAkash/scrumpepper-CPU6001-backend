package com.swlc.ScrumPepperCPU6001.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "story_points_track")
public class StoryPointsTrackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_story_id")
    private ProjectUserStoryEntity projectUserStoryEntity;
    @Column
    private int points;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date trackedDate;

    public StoryPointsTrackEntity(ProjectUserStoryEntity projectUserStoryEntity, int points, Date trackedDate) {
        this.projectUserStoryEntity = projectUserStoryEntity;
        this.points = points;
        this.trackedDate = trackedDate;
    }

    @Override
    public String toString() {
        return "StoryPointsTrackEntity{" +
                "id=" + id +
                ", projectUserStoryEntity=" + projectUserStoryEntity +
                ", points=" + points +
                ", trackedDate=" + trackedDate +
                '}';
    }
}
