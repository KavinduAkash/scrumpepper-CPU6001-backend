package com.swlc.ScrumPepperCPU6001.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swlc.ScrumPepperCPU6001.enums.CorporateEmployeeInvitationStatusType;
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
@Table(name = "corporate_employee_invitation")
public class CorporateEmployeeInvitationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="corporate_employee_id")
    private CorporateEmployeeEntity corporateEmployeeEntity;
    @Column(nullable = false)
    private String email;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="invited_by")
    private CorporateEmployeeEntity invitedBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;
    @Enumerated(EnumType.STRING)
    private CorporateEmployeeInvitationStatusType statusType;

    public CorporateEmployeeInvitationEntity(CorporateEmployeeEntity corporateEmployeeEntity, String email,
                                             CorporateEmployeeEntity invitedBy, Date date,
                                             CorporateEmployeeInvitationStatusType statusType) {
        this.corporateEmployeeEntity = corporateEmployeeEntity;
        this.email = email;
        this.invitedBy = invitedBy;
        this.date = date;
        this.statusType = statusType;
    }

    @Override
    public String toString() {
        return "CorporateEmployeeInvitationEntity{" +
                "id=" + id +
                ", corporateEmployeeEntity=" + corporateEmployeeEntity +
                ", email='" + email + '\'' +
                ", invitedBy=" + invitedBy +
                ", date=" + date +
                ", statusType=" + statusType +
                '}';
    }
}
