package com.swlc.ScrumPepperCPU6001.dto.response;

import com.swlc.ScrumPepperCPU6001.dto.CorporateEmployeeDTO;
import lombok.*;

import java.util.List;

/**
 * @author hp
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class GetTaskEmployeeDTO {
    private List<CorporateEmployeeDTO> taskAssigns;
    private List<CorporateEmployeeDTO> projectMembers;
    private List<CorporateEmployeeDTO> corporateEmployees;

    @Override
    public String toString() {
        return "GetTaskEmployeeDTO{" +
                "taskAssigns=" + taskAssigns +
                ", projectMembers=" + projectMembers +
                ", corporateEmployees=" + corporateEmployees +
                '}';
    }
}
