package com.swlc.ScrumPepperCPU6001.repository;

import com.swlc.ScrumPepperCPU6001.entity.ProjectEntity;
import com.swlc.ScrumPepperCPU6001.entity.ProjectSprintEntity;
import com.swlc.ScrumPepperCPU6001.entity.ProjectSprintUserStoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author hp
 */
public interface ProjectSprintUserStoryRepository extends JpaRepository<ProjectSprintUserStoryEntity, Long> {
    @Query(value = "SELECT * FROM project_sprint_user_story su WHERE su.project_user_story_id=?1 AND su.status<>'DELETED' ORDER BY su.added_date DESC LIMIT 1", nativeQuery = true)
    List<ProjectSprintUserStoryEntity> getByLatestSprintUserStoryRecord(long id);

    @Query(value = "SELECT * FROM project_sprint_user_story su WHERE su.project_sprint_id=?1 AND su.status='ACTIVE' ORDER BY su.added_date", nativeQuery = true)
    List<ProjectSprintUserStoryEntity> getByUserStoriesBySprint(long id);

    @Query(value = "SELECT SUM(u.points) FROM project_sprint_user_story su, project_user_story u WHERE su.project_user_story_id=u.id AND su.project_sprint_id=?1 AND su.status='ACTIVE' ORDER BY su.added_date", nativeQuery = true)
    Integer getTotalPoints(long id);

    @Query("SELECT SUM(p.projectUserStoryEntity.points) FROM ProjectSprintUserStoryEntity p WHERE p.projectSprintEntity=:sprint")
    int getProjectPointCount(@Param("sprint") ProjectSprintEntity sprintEntity);
}
