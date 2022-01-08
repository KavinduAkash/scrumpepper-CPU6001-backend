package com.swlc.ScrumPepperCPU6001.repository;

import com.swlc.ScrumPepperCPU6001.entity.ProjectEntity;
import com.swlc.ScrumPepperCPU6001.entity.ProjectSprintEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author hp
 */
public interface SprintRepository extends JpaRepository<ProjectSprintEntity, Long> {
    Optional<ProjectSprintEntity> findByProjectEntityAndSprintName(ProjectEntity projectEntity, String sprintName);
}
