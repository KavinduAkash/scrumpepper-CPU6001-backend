package com.swlc.ScrumPepperCPU6001.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String name;
    private String email;
    private String username;
    private String mobileNumber;
    private Date activatedDate;

    @Override
    public String toString() {
        return "AdminDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", activatedDate=" + activatedDate +
                '}';
    }
}
