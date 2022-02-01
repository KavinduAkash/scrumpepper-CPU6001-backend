package com.swlc.ScrumPepperCPU6001.dto;

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
}
