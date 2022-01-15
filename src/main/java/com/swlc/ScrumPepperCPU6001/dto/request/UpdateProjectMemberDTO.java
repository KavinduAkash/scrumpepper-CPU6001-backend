package com.swlc.ScrumPepperCPU6001.dto.request;

import com.swlc.ScrumPepperCPU6001.enums.ScrumRoles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author hp
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectMemberDTO {
    private long projectMemberId;
    private ScrumRoles scrumRole;

    @Override
    public String toString() {
        return "UpdateProjectMemberDTO{" +
                "projectMemberId=" + projectMemberId +
                ", scrumRole=" + scrumRole +
                '}';
    }
}
