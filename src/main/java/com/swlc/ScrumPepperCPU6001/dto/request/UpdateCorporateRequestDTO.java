package com.swlc.ScrumPepperCPU6001.dto.request;

import com.swlc.ScrumPepperCPU6001.enums.StatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * @author hp
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCorporateRequestDTO {
    private long id;
    private String name;
    private String address;
    private String contactNumber1;
    private String contactNumber2;
    private String email;
    private MultipartFile corporateLogo;
    private StatusType statusType;

    @Override
    public String toString() {
        return "UpdateCorporateRequestDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contactNumber1='" + contactNumber1 + '\'' +
                ", contactNumber2='" + contactNumber2 + '\'' +
                ", email='" + email + '\'' +
                ", corporateLogo=" + corporateLogo +
                ", statusType=" + statusType +
                '}';
    }
}
