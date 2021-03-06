package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.CorporateEmployeeDTO;
import com.swlc.ScrumPepperCPU6001.dto.ProjectMemberDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddProjectMemberDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateProjectMemberDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.GetTaskEmployeeDTO;
import com.swlc.ScrumPepperCPU6001.enums.ScrumRoles;

import java.util.List;

/**
 * @author hp
 */
public interface ProjectMemberService {
    boolean addProjectMembers(AddProjectMemberDTO addProjectMemberDTO);
    boolean addProjectMembersAsUser(AddProjectMemberDTO addProjectMemberDTO);
    boolean updateProjectMember(UpdateProjectMemberDTO updateProjectMemberDTO);
    boolean removeProjectMember(long projectMemberId);
    List<CorporateEmployeeDTO> getProjectMember(long projectMemberId);
    GetTaskEmployeeDTO getTaskMembers(long taskId);
    List<ProjectMemberDTO> getProjectTeam(long projectId);
    ScrumRoles getProjectMyRole(long projectId);
}
