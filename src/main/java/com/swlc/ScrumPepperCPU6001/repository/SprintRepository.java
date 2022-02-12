package com.swlc.ScrumPepperCPU6001.repository;

import com.swlc.ScrumPepperCPU6001.entity.ProjectEntity;
import com.swlc.ScrumPepperCPU6001.entity.ProjectSprintEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author hp
 */
public interface SprintRepository extends JpaRepository<ProjectSprintEntity, Long> {
    Optional<ProjectSprintEntity> findByProjectEntityAndSprintName(ProjectEntity projectEntity, String sprintName);
    List<ProjectSprintEntity> findByProjectEntity(ProjectEntity projectEntity);
    @Query("SELECT s FROM ProjectSprintEntity s WHERE s<>:sprint")
    List<ProjectSprintEntity> getOtherSprints(@Param("sprint") ProjectSprintEntity sprintEntity);
}
