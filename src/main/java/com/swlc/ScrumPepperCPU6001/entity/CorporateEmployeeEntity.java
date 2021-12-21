package com.swlc.ScrumPepperCPU6001.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swlc.ScrumPepperCPU6001.enums.CorporateAccessStatusType;
import com.swlc.ScrumPepperCPU6001.enums.CorporateAccessType;
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
@Table(name = "corporate_employee")
public class CorporateEmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    private UserEntity userEntity;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="corporate_id")
    private CorporateEntity corporateEntity;
    @Enumerated(EnumType.STRING)
    private CorporateAccessType corporateAccessType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date acceptedDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedDate;
    @Enumerated(EnumType.STRING)
    private CorporateAccessStatusType statusType;

    public CorporateEmployeeEntity(UserEntity userEntity, CorporateEntity corporateEntity,
                                   CorporateAccessType corporateAccessType, Date createdDate, Date acceptedDate,
                                   Date modifiedDate, CorporateAccessStatusType statusType) {
        this.userEntity = userEntity;
        this.corporateEntity = corporateEntity;
        this.corporateAccessType = corporateAccessType;
        this.createdDate = createdDate;
        this.acceptedDate = acceptedDate;
        this.modifiedDate = modifiedDate;
        this.statusType = statusType;
    }

    @Override
    public String toString() {
        return "CorporateEmployeeEntity{" +
                "id=" + id +
                ", userEntity=" + userEntity +
                ", corporateEntity=" + corporateEntity +
                ", corporateAccessType=" + corporateAccessType +
                ", createdDate=" + createdDate +
                ", acceptedDate=" + acceptedDate +
                ", modifiedDate=" + modifiedDate +
                ", statusType=" + statusType +
                '}';
    }
}
