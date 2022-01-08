package com.swlc.ScrumPepperCPU6001.repository;

import com.swlc.ScrumPepperCPU6001.entity.ProjectUserStoryEntity;
import com.swlc.ScrumPepperCPU6001.entity.ProjectUserStoryLabelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author hp
 */
public interface ProjectUserStoryLabelRepository extends JpaRepository<ProjectUserStoryLabelEntity, Long> {
    @Query("SELECT l.userStoryLabelEntity.id FROM ProjectUserStoryLabelEntity l WHERE l.projectUserStoryEntity = :userStory")
    List<Long> getUserStoryLabelIdsByUserStoryId(@Param("userStory")ProjectUserStoryEntity projectUserStoryEntity);
}
