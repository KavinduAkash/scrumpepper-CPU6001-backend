package com.swlc.ScrumPepperCPU6001.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swlc.ScrumPepperCPU6001.enums.StatusType;
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
@Table(name = "corporate")
public class CorporateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(length = 1000, nullable = false)
    private String address;
    @Column(nullable = false)
    private String contactNumber1;
    @Column
    private String contactNumber2;
    @Column(nullable = false)
    private String email;
    @Column
    private String corporateLogo;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registeredDate;
    @Enumerated(EnumType.STRING)
    private StatusType statusType;

    public CorporateEntity(String name, String address, String contactNumber1, String contactNumber2, String email,
                           String corporateLogo, Date registeredDate, StatusType statusType) {
        this.name = name;
        this.address = address;
        this.contactNumber1 = contactNumber1;
        this.contactNumber2 = contactNumber2;
        this.email = email;
        this.corporateLogo = corporateLogo;
        this.registeredDate = registeredDate;
        this.statusType = statusType;
    }

    @Override
    public String toString() {
        return "CorporateEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contactNumber1='" + contactNumber1 + '\'' +
                ", contactNumber2='" + contactNumber2 + '\'' +
                ", email='" + email + '\'' +
                ", corporateLogo='" + corporateLogo + '\'' +
                ", registeredDate=" + registeredDate +
                ", statusType=" + statusType +
                '}';
    }
}
