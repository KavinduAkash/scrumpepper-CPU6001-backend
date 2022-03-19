package com.swlc.ScrumPepperCPU6001.dto.response;

import com.swlc.ScrumPepperCPU6001.dto.ProjectMemberDTO;
import com.swlc.ScrumPepperCPU6001.entity.ProjectMemberEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author hp
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberResponsibilityDTO {
    private ProjectMemberDTO projectMember;
    private int responsibility;
    private int totalResponsibility;
    private int points;
    private int totalPoints;
    private List<UserStoryResponsibilityDTO> userStories;
}
