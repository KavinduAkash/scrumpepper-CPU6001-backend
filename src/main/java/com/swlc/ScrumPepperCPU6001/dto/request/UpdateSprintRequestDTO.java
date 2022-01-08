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
public class UpdateSprintRequestDTO {
    private long id;
    private long projectId;
    private String sprintName;
    private String description;

    @Override
    public String toString() {
        return "UpdateSprintRequestDTO{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", sprintName='" + sprintName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
