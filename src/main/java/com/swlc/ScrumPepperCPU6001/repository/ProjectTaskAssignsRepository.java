package com.swlc.ScrumPepperCPU6001.repository;

import com.swlc.ScrumPepperCPU6001.entity.ProjectTaskAssignsEntity;
import com.swlc.ScrumPepperCPU6001.entity.ProjectTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author hp
 */
public interface ProjectTaskAssignsRepository extends JpaRepository<ProjectTaskAssignsEntity, Long> {
    List<ProjectTaskAssignsEntity> findByProjectTaskEntity(ProjectTaskEntity projectTaskEntity);
}
