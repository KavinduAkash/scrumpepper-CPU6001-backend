package com.swlc.ScrumPepperCPU6001.service.impl;

import com.swlc.ScrumPepperCPU6001.constant.ApplicationConstant;
import com.swlc.ScrumPepperCPU6001.dto.ProjectDTO;
import com.swlc.ScrumPepperCPU6001.dto.ProjectDocDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.CreateDocRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.UpdateDocRequestDTO;
import com.swlc.ScrumPepperCPU6001.entity.ProjectDocsEntity;
import com.swlc.ScrumPepperCPU6001.entity.ProjectEntity;
import com.swlc.ScrumPepperCPU6001.exception.ProjectException;
import com.swlc.ScrumPepperCPU6001.repository.ProjectDocsRepository;
import com.swlc.ScrumPepperCPU6001.repository.ProjectRepository;
import com.swlc.ScrumPepperCPU6001.service.DocsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * @author hp
 */
@Log4j2
@Service
public class DocsServiceImpl implements DocsService {
    private final ProjectDocsRepository projectDocsRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public DocsServiceImpl(ProjectDocsRepository projectDocsRepository, ProjectRepository projectRepository) {
        this.projectDocsRepository = projectDocsRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public ProjectDocDTO createDoc(CreateDocRequestDTO dto) {
        log.info("Execute method createDoc :  Param{} " + dto.toString());
        try {
            Optional<ProjectEntity> byId = projectRepository.findById(dto.getProjectId());
            if(!byId.isPresent())
                throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "Project not found");
            ProjectDocsEntity save = projectDocsRepository.save(new ProjectDocsEntity(byId.get(), dto.getName(), "", new Date(), new Date()));
            return this.prepareProjectDocDTO(save);
        } catch (Exception e) {
            log.error("Method createDoc : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ProjectDocDTO updateDoc(UpdateDocRequestDTO dto) {
        log.info("Execute method updateDoc :  Param{} " + dto.toString());
        try {
            Optional<ProjectDocsEntity> byId = projectDocsRepository.findById(dto.getId());
            if(!byId.isPresent())
                throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "Unable to update");
            ProjectDocsEntity projectDocsEntity = byId.get();
            projectDocsEntity.setName(dto.getName());
            projectDocsEntity.setDoc(dto.getDoc());
            projectDocsEntity.setModifiedDate(new Date());
            ProjectDocsEntity save = projectDocsRepository.save(projectDocsEntity);
            return this.prepareProjectDocDTO(save);
        } catch (Exception e) {
            log.error("Method updateDoc : " + e.getMessage(), e);
            throw e;
        }
    }

    private ProjectDocDTO prepareProjectDocDTO(ProjectDocsEntity save) {
        try {
            ProjectEntity p = save.getProjectEntity();
            ProjectDTO projectDTO = new ProjectDTO(
                    p.getId(),
                    null,
                    p.getProjectName(),
                    p.getCreatedDate(),
                    p.getModifiedDate(),
                    null,
                    null,
                    p.getStatusType(),
                    p.getRef()
            );
            return new ProjectDocDTO(save.getId(), projectDTO, save.getName(), save.getDoc(), save.getCreatedDate(), save.getModifiedDate());
        } catch (Exception e) {
            throw e;
        }
    }

}
