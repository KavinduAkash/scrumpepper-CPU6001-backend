package com.swlc.ScrumPepperCPU6001.dto;

import com.swlc.ScrumPepperCPU6001.entity.CorporateEmployeeEntity;
import com.swlc.ScrumPepperCPU6001.enums.StatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author hp
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignsDTO {
    private long id;
    private UserStoryDTO userStory;
    private String title;
    private Date modifiedDate;
    private CorporateEmployeeEntity createdBy;
    private CorporateEmployeeEntity modifiedBy;
    private StatusType statusType;
    private List<ProjectMemberDTO> assigns;

    @Override
    public String toString() {
        return "TaskAssignsDTO{" +
                "id=" + id +
                ", userStory=" + userStory +
                ", title='" + title + '\'' +
                ", modifiedDate=" + modifiedDate +
                ", createdBy=" + createdBy +
                ", modifiedBy=" + modifiedBy +
                ", statusType=" + statusType +
                ", assigns=" + assigns +
                '}';
    }
}
