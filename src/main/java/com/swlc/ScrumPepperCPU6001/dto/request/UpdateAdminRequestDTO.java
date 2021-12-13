package com.swlc.ScrumPepperCPU6001.dto.request;

import com.swlc.ScrumPepperCPU6001.enums.AdminType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author hp
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAdminRequestDTO {
    private long id;
    private String employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private String contactNumber;
    private AdminType adminType;

    @Override
    public String toString() {
        return "UpdateAdminRequestDTO{" +
                "id=" + id +
                ", employeeId='" + employeeId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", adminType=" + adminType +
                '}';
    }
}
