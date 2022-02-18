package com.swlc.ScrumPepperCPU6001.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swlc.ScrumPepperCPU6001.enums.ProjectStatusType;
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
@Table(name = "project")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="corporate_id")
    private CorporateEntity corporateEntity;
    @Column(nullable = false)
    private String projectName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedDate;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="created_corporate_employee_id")
    private CorporateEmployeeEntity created_CorporateEmployeeEntity;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="modified_corporate_employee_id")
    private CorporateEmployeeEntity modified_CorporateEmployeeEntity;
    @Enumerated(EnumType.STRING)
    private ProjectStatusType statusType;
    @Column(unique = true)
    private String ref;

    public ProjectEntity(CorporateEntity corporateEntity, String projectName, Date createdDate, Date modifiedDate, CorporateEmployeeEntity created_CorporateEmployeeEntity, CorporateEmployeeEntity modified_CorporateEmployeeEntity, ProjectStatusType statusType, String ref) {
        this.corporateEntity = corporateEntity;
        this.projectName = projectName;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.created_CorporateEmployeeEntity = created_CorporateEmployeeEntity;
        this.modified_CorporateEmployeeEntity = modified_CorporateEmployeeEntity;
        this.statusType = statusType;
        this.ref = ref;
    }

    @Override
    public String toString() {
        return "ProjectEntity{" +
                "id=" + id +
                ", corporateEntity=" + corporateEntity +
                ", projectName='" + projectName + '\'' +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                ", created_CorporateEmployeeEntity=" + created_CorporateEmployeeEntity +
                ", modified_CorporateEmployeeEntity=" + modified_CorporateEmployeeEntity +
                ", statusType=" + statusType +
                ", ref='" + ref + '\'' +
                '}';
    }
}
