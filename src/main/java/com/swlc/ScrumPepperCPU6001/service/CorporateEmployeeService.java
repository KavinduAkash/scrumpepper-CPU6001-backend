package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.request.AddCorporateEmployeeRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.ApproveRejectInvitationRequestDTO;

/**
 * @author hp
 */
public interface CorporateEmployeeService {
    boolean addCorporateEmployee(AddCorporateEmployeeRequestDTO addCorporateEmployeeRequestDTO);
    boolean approveRejectCorporateEmployeeInvitation(ApproveRejectInvitationRequestDTO approveRejectInvitationRequestDTO);
}
