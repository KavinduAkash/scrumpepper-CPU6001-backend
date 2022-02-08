package com.swlc.ScrumPepperCPU6001.service.impl;

import com.swlc.ScrumPepperCPU6001.constant.ApplicationConstant;
import com.swlc.ScrumPepperCPU6001.dto.CorporateDTO;
import com.swlc.ScrumPepperCPU6001.dto.CorporateEmployeeDTO;
import com.swlc.ScrumPepperCPU6001.dto.ProjectDTO;
import com.swlc.ScrumPepperCPU6001.dto.UserStoryLblDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddUserStoryLblRequestDTO;
import com.swlc.ScrumPepperCPU6001.entity.ProjectEntity;
import com.swlc.ScrumPepperCPU6001.entity.UserStoryLabelEntity;
import com.swlc.ScrumPepperCPU6001.enums.ProjectStatusType;
import com.swlc.ScrumPepperCPU6001.exception.ProjectException;
import com.swlc.ScrumPepperCPU6001.repository.ProjectRepository;
import com.swlc.ScrumPepperCPU6001.repository.UserStoryLabelRepository;
import com.swlc.ScrumPepperCPU6001.service.UserStoryLblService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author hp
 */
@Service
@Log4j2
public class UserStoryLblServiceImpl implements UserStoryLblService {
    private final ProjectRepository projectRepository;
    private final UserStoryLabelRepository userStoryLabelRepository;

    public UserStoryLblServiceImpl(ProjectRepository projectRepository, UserStoryLabelRepository userStoryLabelRepository) {
        this.projectRepository = projectRepository;
        this.userStoryLabelRepository = userStoryLabelRepository;
    }

    @Override
    public List<UserStoryLblDTO> createNewUserStoryLbl(AddUserStoryLblRequestDTO addUserStoryLblRequestDTO) {
        log.info("Execute method createNewUserStoryLbl : addUserStoryLblRequestDTO : " + addUserStoryLblRequestDTO.toString());
        try {
            //check project validity
            Optional<ProjectEntity> byId = projectRepository.findById(addUserStoryLblRequestDTO.getProjectId());
            if(!byId.isPresent())
                throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "Project not found");

            //save user story
            userStoryLabelRepository.save(new UserStoryLabelEntity(byId.get(), addUserStoryLblRequestDTO.getLbl()));

            return prepareProjectUserStoryLbl(byId.get());

        } catch (Exception e) {
            log.error("Method createNewUserStoryLbl : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<UserStoryLblDTO> getAllProjectUserStoryLbl(long id) {
        log.info("Execute method getAllProjectUserStoryLbl : Params{} " + id);
        try {
            //check project validity
            Optional<ProjectEntity> byId = projectRepository.findById(id);
            if(!byId.isPresent())
                throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "Project not found");
            return prepareProjectUserStoryLbl(byId.get());
        } catch (Exception e) {
            log.error("Method getAllProjectUserStoryLbl : " + e.getMessage(), e);
            throw e;
        }
    }

    private List<UserStoryLblDTO> prepareProjectUserStoryLbl(ProjectEntity projectEntity) {
        log.info("Execute method prepareProjectUserStoryLbl : Params{} " + projectEntity);
        try {
            List<UserStoryLabelEntity> byProjectEntity = userStoryLabelRepository.findByProjectEntity(projectEntity);
            //create project dto
            ProjectDTO projectDTO = new ProjectDTO(
                    projectEntity.getId(),
                    null,
                    projectEntity.getProjectName(),
                    null,
                    projectEntity.getModifiedDate(),
                    null,
                    null,
                    projectEntity.getStatusType()
            );

            //prepare project user story lbl return list
            List<UserStoryLblDTO> lbl_list =  new ArrayList<>();
            for (UserStoryLabelEntity lbl : byProjectEntity) {
                lbl_list.add(new UserStoryLblDTO(lbl.getId(), projectDTO, lbl.getLabel()));
            }
            return lbl_list;
        } catch (Exception e) {
            log.error("Method prepareProjectUserStoryLbl : " + e.getMessage(), e);
            throw e;
        }
    }
}
