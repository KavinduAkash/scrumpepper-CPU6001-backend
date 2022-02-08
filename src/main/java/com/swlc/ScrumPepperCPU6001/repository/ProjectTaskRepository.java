package com.swlc.ScrumPepperCPU6001.repository;

import com.swlc.ScrumPepperCPU6001.entity.ProjectTaskEntity;
import com.swlc.ScrumPepperCPU6001.entity.ProjectUserStoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author hp
 */
public interface ProjectTaskRepository extends JpaRepository<ProjectTaskEntity, Long> {
    List<ProjectTaskEntity> findAllByProjectUserStoryEntity(ProjectUserStoryEntity projectUserStoryEntity);
}
