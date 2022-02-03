package com.swlc.ScrumPepperCPU6001.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swlc.ScrumPepperCPU6001.entity.CorporateEmployeeEntity;
import com.swlc.ScrumPepperCPU6001.entity.ProjectEntity;
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
public class AddProjectTaskRequestDTO {
    private long userStoryId;
    private long taskId;
    private String title;
    private UserStoryStatusType statusType;
    private List<Long> employees;

    @Override
    public String toString() {
        return "AddProjectTaskRequestDTO{" +
                "userStoryId=" + userStoryId +
                ", title='" + title + '\'' +
                ", statusType=" + statusType +
                ", employees=" + employees +
                '}';
    }
}
