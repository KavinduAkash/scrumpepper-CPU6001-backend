package com.swlc.ScrumPepperCPU6001.service.impl;

import com.swlc.ScrumPepperCPU6001.constant.ApplicationConstant;
import com.swlc.ScrumPepperCPU6001.dto.ProjectDTO;
import com.swlc.ScrumPepperCPU6001.dto.SppokerDTO;
import com.swlc.ScrumPepperCPU6001.dto.SprintDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.HandleSppokerRoomRequestDTO;
import com.swlc.ScrumPepperCPU6001.entity.ProjectEntity;
import com.swlc.ScrumPepperCPU6001.entity.ProjectSprintEntity;
import com.swlc.ScrumPepperCPU6001.entity.SpppokerRoomEntity;
import com.swlc.ScrumPepperCPU6001.exception.SppokerException;
import com.swlc.ScrumPepperCPU6001.repository.ProjectRepository;
import com.swlc.ScrumPepperCPU6001.repository.SppokerRepository;
import com.swlc.ScrumPepperCPU6001.repository.SprintRepository;
import com.swlc.ScrumPepperCPU6001.service.SppokerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author hp
 */
@Log4j2
@Service
public class SppokerServiceImpl implements SppokerService {

    private final ProjectRepository projectRepository;
    private final SprintRepository sprintRepository;
    private final SppokerRepository sppokerRepository;

    @Autowired
    public SppokerServiceImpl(ProjectRepository projectRepository, SprintRepository sprintRepository, SppokerRepository sppokerRepository) {
        this.projectRepository = projectRepository;
        this.sprintRepository = sprintRepository;
        this.sppokerRepository = sppokerRepository;
    }

    @Override
    public boolean createRoom(HandleSppokerRoomRequestDTO room) {
        log.info("Execute method createRoom :  Param{} " + room.toString());
        try {
            ProjectEntity projectEntity = null;
            ProjectSprintEntity projectSprintEntity = null;
            if(room.getProjectId()==0 && room.getSprintId()==0)
                throw new SppokerException(ApplicationConstant.RESOURCE_NOT_FOUND, "Project or sprint not found");
            if(room.getSprintId()!=0) {
                Optional<ProjectSprintEntity> byId = sprintRepository.findById(room.getSprintId());
                if(!byId.isPresent())
                    throw new SppokerException(ApplicationConstant.RESOURCE_NOT_FOUND, "Sprint not found");
                projectSprintEntity = byId.get();
                projectEntity = projectSprintEntity.getProjectEntity();
            } else {
                Optional<ProjectEntity> byId = projectRepository.findById(room.getProjectId());
                if(!byId.isPresent())
                    throw new SppokerException(ApplicationConstant.RESOURCE_NOT_FOUND, "Project not found");
                projectEntity = byId.get();
            }
            sppokerRepository.save(
                    new SpppokerRoomEntity(
                            room.getDescription(),
                            projectEntity,
                            projectSprintEntity,
                            UUID.randomUUID().toString(),
                            new Date(),
                            null,
                            1)
            );
            return true;
        } catch (Exception e) {
            log.error("Method createRoom : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean updateRoom(HandleSppokerRoomRequestDTO room) {
        log.info("Execute method updateRoom :  Param{} " + room.toString());
        try {
            Optional<SpppokerRoomEntity> byId = sppokerRepository.findById(room.getId());
            if(!byId.isPresent())
                throw new SppokerException(ApplicationConstant.RESOURCE_NOT_FOUND, "SPPoker room not found");
            SpppokerRoomEntity spppokerRoomEntity = byId.get();
            spppokerRoomEntity.setNote(room.getDescription());
            spppokerRoomEntity.setStatus(room.getStatus());
            sppokerRepository.save(spppokerRoomEntity);
            return true;
        } catch (Exception e) {
            log.error("Method updateRoom : " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<SppokerDTO> getRoom(long projectId) {
        log.info("Execute method getRoom :  id " + projectId);
        try {
            List<SppokerDTO> list = new ArrayList<>();
            Optional<ProjectEntity> byId = projectRepository.findById(projectId);
            if(!byId.isPresent())
                throw new SppokerException(ApplicationConstant.RESOURCE_NOT_FOUND, "Project not found");
            List<SpppokerRoomEntity> byProjectEntity = sppokerRepository.findByProjectEntity(byId.get());
            for (SpppokerRoomEntity r : byProjectEntity) {
                list.add(this.prepareSppokerDTO(r));
            }
            return list;
        } catch (Exception e) {
            log.error("Method getRoom : " + e.getMessage(), e);
            throw e;
        }
    }

    private SppokerDTO prepareSppokerDTO(SpppokerRoomEntity r) {
        try {
            SprintDTO sprintDTO = null;
            ProjectSprintEntity projectSprintEntity = r.getProjectSprintEntity();
            if(projectSprintEntity!=null) {
                sprintDTO = this.prepareSprintDTO(projectSprintEntity);
            }
            ProjectDTO projectDTO = this.prepareProjectDTO(r.getProjectEntity());
            return new SppokerDTO(r.getId(), r.getNote(), projectDTO, sprintDTO, r.getRoomRef(), r.getStartedDate(), r.getCloseDate(), r.getStatus());
        } catch (Exception e) {
            throw e;
        }
    }


    private SprintDTO prepareSprintDTO(ProjectSprintEntity save) {
        try {
            return new SprintDTO(
                    save.getId(),
                    save.getProjectEntity().getId(),
                    save.getSprintName(),
                    save.getDescription(),
                    save.getCreatedDate(),
                    save.getModifiedDate(),
                    null,
                    null,
                    save.getStatusType()
            );
        } catch (Exception e) {
            throw e;
        }
    }

    private ProjectDTO prepareProjectDTO(ProjectEntity p) {
        try {
            return new ProjectDTO(
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
        } catch (Exception e) {
            throw e;
        }
    }


}
