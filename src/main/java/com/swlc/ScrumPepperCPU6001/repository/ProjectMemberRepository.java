package com.swlc.ScrumPepperCPU6001.repository;

import com.swlc.ScrumPepperCPU6001.entity.CorporateEmployeeEntity;
import com.swlc.ScrumPepperCPU6001.entity.ProjectEntity;
import com.swlc.ScrumPepperCPU6001.entity.ProjectMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author hp
 */
public interface ProjectMemberRepository extends JpaRepository<ProjectMemberEntity, Long> {
    Optional<ProjectMemberEntity> findByProjectEntityAndCorporateEmployeeEntity(ProjectEntity projectEntity,
                                                                                CorporateEmployeeEntity corporateEmployeeEntity);
}
