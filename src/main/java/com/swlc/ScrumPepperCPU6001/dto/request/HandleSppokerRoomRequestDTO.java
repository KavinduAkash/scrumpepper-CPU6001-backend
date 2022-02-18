package com.swlc.ScrumPepperCPU6001.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author hp
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HandleSppokerRoomRequestDTO {
    private long id;
    private long projectId;
    private long sprintId;
    private String description;
    private int status;

    @Override
    public String toString() {
        return "HandleSppokerRoomRequestDTO{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", sprintId=" + sprintId +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
