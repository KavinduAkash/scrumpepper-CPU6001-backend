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
@Table(name = "user_story_track")
public class UserStoryTrackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_story_id")
    private ProjectUserStoryEntity projectUserStoryEntity;
    @Enumerated(EnumType.STRING)
    private UserStoryStatusType statusType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date trackedDate;

    public UserStoryTrackEntity(ProjectUserStoryEntity projectUserStoryEntity, UserStoryStatusType statusType, Date trackedDate) {
        this.projectUserStoryEntity = projectUserStoryEntity;
        this.statusType = statusType;
        this.trackedDate = trackedDate;
    }

    @Override
    public String toString() {
        return "UserStoryTrackEntity{" +
                "id=" + id +
                ", projectUserStoryEntity=" + projectUserStoryEntity +
                ", statusType=" + statusType +
                ", trackedDate=" + trackedDate +
                '}';
    }
}
