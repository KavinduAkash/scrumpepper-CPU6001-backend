package com.swlc.ScrumPepperCPU6001.dto;
import com.swlc.ScrumPepperCPU6001.enums.Priority;
import com.swlc.ScrumPepperCPU6001.enums.UserStoryStatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author hp
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserStoryDTO {
    private long id;
    private ProjectDTO project;
    private String title;
    private String description;
    private Date createdDate;
    private Date modifiedDate;
    private CorporateEmployeeDTO createdBy;
    private CorporateEmployeeDTO modifiedBy;
    private UserStoryStatusType statusType;
    private List<UserStoryLblDTO> userStoryLbl;
    private Priority priority;
    private List<TaskDTO> tasks;
    private List<SprintDTO> otherSprints;

    @Override
    public String toString() {
        return "UserStoryDTO{" +
                "id=" + id +
                ", project=" + project +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                ", createdBy=" + createdBy +
                ", modifiedBy=" + modifiedBy +
                ", statusType=" + statusType +
                ", userStoryLbl=" + userStoryLbl +
                ", priority=" + priority +
                ", tasks=" + tasks +
                '}';
    }
}
