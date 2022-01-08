package com.swlc.ScrumPepperCPU6001.repository;

import com.swlc.ScrumPepperCPU6001.entity.ProjectEntity;
import com.swlc.ScrumPepperCPU6001.entity.ProjectUserStoryEntity;
import com.swlc.ScrumPepperCPU6001.entity.UserStoryLabelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author hp
 */
public interface UserStoryLabelRepository extends JpaRepository<UserStoryLabelEntity, Long> {
    List<UserStoryLabelEntity> findByProjectEntity(ProjectEntity projectEntity);
}
