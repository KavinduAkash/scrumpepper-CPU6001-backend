package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.CorporateEmployeeDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddCorporateEmployeeRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.ApproveRejectInvitationRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.SearchEmployeeRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.InvitationsResponseDTO;

import java.util.List;

/**
 * @author hp
 */
public interface CorporateEmployeeService {
    boolean addCorporateEmployee(AddCorporateEmployeeRequestDTO addCorporateEmployeeRequestDTO);
    boolean approveRejectCorporateEmployeeInvitation(ApproveRejectInvitationRequestDTO approveRejectInvitationRequestDTO);
    List<CorporateEmployeeDTO> searchCorporateEmployee(SearchEmployeeRequestDTO searchEmployeeRequestDTO);
    List<InvitationsResponseDTO> getInvitations();
}
