package com.swlc.ScrumPepperCPU6001.repository;

import com.swlc.ScrumPepperCPU6001.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author hp
 */
public interface ProjectTaskAssignsRepository extends JpaRepository<ProjectTaskAssignsEntity, Long> {
    List<ProjectTaskAssignsEntity> findByProjectTaskEntity(ProjectTaskEntity projectTaskEntity);

    @Query("SELECT t.projectMemberEntity.corporateEmployeeEntity FROM ProjectTaskAssignsEntity t WHERE t.projectTaskEntity=:task AND t.projectMemberEntity.statusType=\"ACTIVE\"")
    List<CorporateEmployeeEntity> getTaskAssignsCorporateEmployees(@Param("task") ProjectTaskEntity projectTaskEntity);

    @Query("SELECT e.corporateEmployeeEntity FROM ProjectMemberEntity e WHERE e.statusType=\"ACTIVE\" AND e.projectEntity=:project AND e NOT IN (SELECT t.projectMemberEntity FROM ProjectTaskAssignsEntity t WHERE t.projectTaskEntity=:task AND t.projectMemberEntity.statusType=\"ACTIVE\")")
    List<CorporateEmployeeEntity> getTaskNotAssignsProjectCorporateEmployees(@Param("task") ProjectTaskEntity projectTaskEntity, @Param("project") ProjectEntity projectEntity);

    @Query("SELECT e FROM CorporateEmployeeEntity e WHERE e.statusType=\"ACTIVE\" AND e.corporateEntity=:corporate AND e NOT IN (SELECT e.corporateEmployeeEntity FROM ProjectMemberEntity e WHERE e.statusType=\"ACTIVE\" AND e.projectEntity=:project)")
    List<CorporateEmployeeEntity> getTaskNotAssignsProjectCorporateEmployees(@Param("corporate")CorporateEntity corporateEntity, @Param("project") ProjectEntity projectEntity);
}