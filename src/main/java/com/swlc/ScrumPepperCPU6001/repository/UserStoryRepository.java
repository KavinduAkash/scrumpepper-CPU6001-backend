package com.swlc.ScrumPepperCPU6001.repository;

import com.swlc.ScrumPepperCPU6001.entity.ProjectUserStoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author hp
 */
public interface UserStoryRepository extends JpaRepository<ProjectUserStoryEntity, Long> {
}