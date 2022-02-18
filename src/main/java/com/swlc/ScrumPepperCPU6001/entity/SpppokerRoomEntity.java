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
@Table(name = "spppoker_room")
public class SpppokerRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String note;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="project_id")
    private ProjectEntity projectEntity;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="project_sprint_id")
    private ProjectSprintEntity projectSprintEntity;
    @Column(unique = true)
    private String roomRef;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startedDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date closeDate;
    @Column
    private int status;

    public SpppokerRoomEntity(String note, ProjectEntity projectEntity, ProjectSprintEntity projectSprintEntity, String roomRef, Date startedDate, Date closeDate, int status) {
        this.note = note;
        this.projectEntity = projectEntity;
        this.projectSprintEntity = projectSprintEntity;
        this.roomRef = roomRef;
        this.startedDate = startedDate;
        this.closeDate = closeDate;
        this.status = status;
    }

    @Override
    public String toString() {
        return "SpppokerRoomEntity{" +
                "id=" + id +
                ", note='" + note + '\'' +
                ", projectEntity=" + projectEntity +
                ", projectSprintEntity=" + projectSprintEntity +
                ", roomRef=" + roomRef +
                ", startedDate=" + startedDate +
                ", closeDate=" + closeDate +
                ", status=" + status +
                '}';
    }
}
