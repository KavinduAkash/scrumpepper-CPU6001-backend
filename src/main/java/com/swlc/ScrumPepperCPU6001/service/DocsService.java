package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.ProjectDocDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.CreateDocRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateDocRequestDTO;

import java.util.List;

/**
 * @author hp
 */
public interface DocsService {
    ProjectDocDTO createDoc(CreateDocRequestDTO dto);
    ProjectDocDTO updateDoc(UpdateDocRequestDTO dto);
    List<ProjectDocDTO> getDocs(long id);
}
