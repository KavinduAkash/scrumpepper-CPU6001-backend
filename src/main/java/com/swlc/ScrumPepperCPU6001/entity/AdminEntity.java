package com.swlc.ScrumPepperCPU6001.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swlc.ScrumPepperCPU6001.enums.AdminType;
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
@Table(name = "admin")
public class AdminEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    private String employeeId;
    @Column(length = 255, nullable = false, unique = true)
    private String email;
    @Column(length = 255, nullable = false, unique = true)
    private String username;
    @Column(length = 255, nullable = false)
    private String password;
    private String contactNumber;
    @Enumerated(EnumType.STRING)
    private AdminType adminType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate;

    public AdminEntity(String firstName, String lastName, String employeeId, String email, String username,
                       String password, String contactNumber, AdminType adminType, Date createdDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeId = employeeId;
        this.email = email;
        this.username = username;
        this.password = password;
        this.contactNumber = contactNumber;
        this.adminType = adminType;
        this.createdDate = createdDate;
    }
}
