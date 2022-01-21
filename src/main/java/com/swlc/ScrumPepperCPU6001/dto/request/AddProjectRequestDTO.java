package com.swlc.ScrumPepperCPU6001.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author hp
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddProjectRequestDTO {
    private long corporateId;
    private String projectName;
    private List<AddProjectMemberDTO> projectMembers;

    @Override
    public String toString() {
        return "AddProjectRequestDTO{" +
                "corporateId=" + corporateId +
                ", projectName='" + projectName + '\'' +
                ", projectMembers=" + projectMembers +
                '}';
    }
}
