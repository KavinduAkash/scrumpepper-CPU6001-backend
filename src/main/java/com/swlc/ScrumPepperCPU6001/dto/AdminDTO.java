package com.swlc.ScrumPepperCPU6001.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swlc.ScrumPepperCPU6001.enums.AdminType;
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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminDTO {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String contactNumber;
    private String employeeId;
    private AdminType adminType;
    private Date createdDate;
    private StatusType statusType;




    @Override
    public String toString() {
        return "AdminDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", adminType=" + adminType +
                ", createdDate=" + createdDate +
                ", statusType=" + statusType +
                '}';
    }
}
