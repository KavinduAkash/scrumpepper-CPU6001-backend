package com.swlc.ScrumPepperCPU6001.dto.request;

import com.swlc.ScrumPepperCPU6001.enums.UserStoryStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author hp
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HandleUserStoryRequestDTO {
    private long projectId;
    private long userStoryId;
    private String title;
    private String description;
    private List<Long> userStoryLabels;
    private UserStoryStatusType statusType;

    @Override
    public String toString() {
        return "HandleUserStoryRequestDTO{" +
                "projectId=" + projectId +
                ", userStoryId=" + userStoryId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", userStoryLabels=" + userStoryLabels +
                '}';
    }
}
