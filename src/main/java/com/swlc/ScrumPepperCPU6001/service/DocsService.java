package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.ProjectDocDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.CreateDocRequestDTO;

/**
 * @author hp
 */
public interface DocsService {
    ProjectDocDTO createDoc(CreateDocRequestDTO dto);
}
