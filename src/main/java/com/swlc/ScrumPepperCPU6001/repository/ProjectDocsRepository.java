package com.swlc.ScrumPepperCPU6001.repository;

import com.swlc.ScrumPepperCPU6001.entity.ProjectDocsEntity;
import com.swlc.ScrumPepperCPU6001.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author hp
 */
public interface ProjectDocsRepository extends JpaRepository<ProjectDocsEntity, Long> {
    List<ProjectDocsEntity> findAllByProjectEntity(ProjectEntity projectEntity);
}
