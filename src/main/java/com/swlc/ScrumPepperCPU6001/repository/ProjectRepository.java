package com.swlc.ScrumPepperCPU6001.repository;

import com.swlc.ScrumPepperCPU6001.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author hp
 */
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
}
