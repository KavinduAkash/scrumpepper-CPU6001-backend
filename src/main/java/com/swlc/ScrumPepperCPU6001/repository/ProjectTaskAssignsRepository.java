package com.swlc.ScrumPepperCPU6001.repository;

import com.swlc.ScrumPepperCPU6001.entity.*;
import com.swlc.ScrumPepperCPU6001.enums.CorporateAccessStatusType;
import com.swlc.ScrumPepperCPU6001.enums.ProjectMemberStatusType;
import com.swlc.ScrumPepperCPU6001.enums.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author hp
 */
@EnableJpaRepositories
public interface ProjectTaskAssignsRepository extends JpaRepository<ProjectTaskAssignsEntity, Long> {
    List<ProjectTaskAssignsEntity> findByProjectTaskEntity(ProjectTaskEntity projectTaskEntity);

    @Query("SELECT t.projectMemberEntity.corporateEmployeeEntity FROM ProjectTaskAssignsEntity t WHERE t.statusType=:tsstatus AND t.projectTaskEntity=:task AND t.projectMemberEntity.statusType=:tassignstatus")
    List<CorporateEmployeeEntity> getTaskAssignsCorporateEmployees(@Param("tsstatus") StatusType statusType, @Param("task") ProjectTaskEntity projectTaskEntity, @Param("tassignstatus") ProjectMemberStatusType tassignstatus);

    @Query("SELECT e.corporateEmployeeEntity FROM ProjectMemberEntity e WHERE e.statusType=:pmstatus AND e.projectEntity=:project AND e NOT IN (SELECT t.projectMemberEntity FROM ProjectTaskAssignsEntity t WHERE t.statusType=:tsstatus AND t.projectTaskEntity=:task AND t.projectMemberEntity.statusType=:pmstatus2)")
    List<CorporateEmployeeEntity> getTaskNotAssignsProjectCorporateEmployees(@Param("tsstatus") StatusType statusType, @Param("pmstatus") ProjectMemberStatusType projectMemberStatusType,  @Param("task") ProjectTaskEntity projectTaskEntity, @Param("project") ProjectEntity projectEntity, @Param("pmstatus2") ProjectMemberStatusType projectMemberStatusType2);
//
    @Query("SELECT e FROM CorporateEmployeeEntity e WHERE e.statusType=:cstatus AND e.corporateEntity=:corporate AND e NOT IN (SELECT e.corporateEmployeeEntity FROM ProjectMemberEntity e WHERE e.statusType=:pmstatus AND e.projectEntity=:project)")
    List<CorporateEmployeeEntity> getTaskNotAssignsProjectCorporateEmployees(@Param("cstatus") CorporateAccessStatusType statusType,  @Param("corporate")CorporateEntity corporateEntity, @Param("project") ProjectEntity projectEntity, @Param("pmstatus")ProjectMemberStatusType tassignstatus);

    Optional<ProjectTaskAssignsEntity> findByProjectTaskEntityAndProjectMemberEntity(ProjectTaskEntity projectTaskEntity, ProjectMemberEntity projectMemberEntity);
}