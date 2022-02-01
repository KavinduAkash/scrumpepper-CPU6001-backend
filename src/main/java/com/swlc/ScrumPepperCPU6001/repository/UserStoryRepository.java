package com.swlc.ScrumPepperCPU6001.repository;

import com.swlc.ScrumPepperCPU6001.entity.ProjectEntity;
import com.swlc.ScrumPepperCPU6001.entity.ProjectUserStoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author hp
 */
public interface UserStoryRepository extends JpaRepository<ProjectUserStoryEntity, Long> {
    List<ProjectUserStoryEntity> findByProjectEntity(ProjectEntity projectEntity);
}
