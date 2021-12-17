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
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String refNo;
    @Column(nullable = false)
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String contactNumber;
    @Column(length = 255, nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate;
    @Enumerated(EnumType.STRING)
    private StatusType statusType;

    public UserEntity(String refNo, String firstName, String lastName, String contactNumber, String email,
                      String password, Date createdDate, StatusType statusType) {
        this.refNo = refNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
        this.email = email;
        this.password = password;
        this.createdDate = createdDate;
        this.statusType = statusType;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", refNo='" + refNo + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", createdDate=" + createdDate +
                ", statusType=" + statusType +
                '}';
    }
}
