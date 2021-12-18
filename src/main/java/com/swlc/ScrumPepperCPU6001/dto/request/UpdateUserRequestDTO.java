package com.swlc.ScrumPepperCPU6001.dto.request;

import com.swlc.ScrumPepperCPU6001.enums.StatusType;
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
public class UpdateUserRequestDTO {
    private long id;
    private String refNo;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String password;
    private StatusType statusType;

    @Override
    public String toString() {
        return "UpdateUserRequestDTO{" +
                "id=" + id +
                ", refNo='" + refNo + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", password='" + password + '\'' +
                ", statusType=" + statusType +
                '}';
    }
}
