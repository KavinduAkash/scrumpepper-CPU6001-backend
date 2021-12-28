package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.request.AddProjectMemberDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateProjectMemberDTO;

/**
 * @author hp
 */
public interface ProjectMemberService {
    boolean addProjectMembers(AddProjectMemberDTO addProjectMemberDTO);
    boolean updateProjectMember(UpdateProjectMemberDTO updateProjectMemberDTO);
    boolean removeProjectMember(long projectMemberId);
}
