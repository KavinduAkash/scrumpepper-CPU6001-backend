package com.swlc.ScrumPepperCPU6001.dto.response;

import com.swlc.ScrumPepperCPU6001.dto.ProjectDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.ProjectMemberResponse.ProjectMemberResponseDTO;
import com.swlc.ScrumPepperCPU6001.enums.ScrumRoles;
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
public class YourProjectResponseDTO {
    private ProjectDTO project;
    private List<ProjectMemberResponseDTO> projectMember;
    private ScrumRoles role;
}
