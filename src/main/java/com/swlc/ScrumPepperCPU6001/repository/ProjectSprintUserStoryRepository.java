package com.swlc.ScrumPepperCPU6001.repository;

import com.swlc.ScrumPepperCPU6001.entity.ProjectSprintUserStoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author hp
 */
public interface ProjectSprintUserStoryRepository extends JpaRepository<ProjectSprintUserStoryEntity, Long> {
    @Query(value = "SELECT * FROM project_sprint_user_story su WHERE su.project_user_story_id=?1 AND su.status<>'DELETED' ORDER BY su.added_date DESC LIMIT 1", nativeQuery = true)
    List<ProjectSprintUserStoryEntity> getByLatestSprintUserStoryRecord(long id);
}
