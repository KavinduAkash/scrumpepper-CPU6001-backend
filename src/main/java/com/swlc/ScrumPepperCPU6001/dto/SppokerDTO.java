package com.swlc.ScrumPepperCPU6001.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swlc.ScrumPepperCPU6001.entity.ProjectEntity;
import com.swlc.ScrumPepperCPU6001.entity.ProjectSprintEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @author hp
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SppokerDTO {
    private long id;
    private String note;
    private ProjectDTO project;
    private SprintDTO sprint;
    private String roomRef;
    private Date startedDate;
    private Date closeDate;
    private int status;

    @Override
    public String toString() {
        return "SppokerDTO{" +
                "id=" + id +
                ", note='" + note + '\'' +
                ", project=" + project +
                ", sprint=" + sprint +
                ", roomRef='" + roomRef + '\'' +
                ", startedDate=" + startedDate +
                ", closeDate=" + closeDate +
                ", status=" + status +
                '}';
    }
}
