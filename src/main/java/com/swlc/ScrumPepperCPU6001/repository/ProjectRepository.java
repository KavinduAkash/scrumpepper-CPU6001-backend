package com.swlc.ScrumPepperCPU6001.repository;

import com.swlc.ScrumPepperCPU6001.entity.CorporateEntity;
import com.swlc.ScrumPepperCPU6001.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author hp
 */
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    List<ProjectEntity> findByCorporateEntity(CorporateEntity corporateEntity);
    Optional<ProjectEntity> findByRef(String ref);
}
