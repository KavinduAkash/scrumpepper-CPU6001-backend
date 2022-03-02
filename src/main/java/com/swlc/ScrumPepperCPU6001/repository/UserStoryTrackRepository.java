package com.swlc.ScrumPepperCPU6001.repository;

import com.swlc.ScrumPepperCPU6001.entity.UserStoryTrackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

/**
 * @author hp
 */
public interface UserStoryTrackRepository extends JpaRepository<UserStoryTrackEntity, Long> {
    @Query(value = "SELECT IFNULL(SUM(u.points), 0) FROM user_story_track ut, project_sprint_user_story su, project_user_story u WHERE su.project_user_story_id=u.id AND ut.user_story_id=u.id AND su.project_sprint_id=?1 AND su.status='ACTIVE' AND DATE(ut.tracked_date)=?2 AND ut.status_type='COMPLETED'", nativeQuery = true)
    Integer getDayTrackPoints(long sprintId, String trackDate);
}
