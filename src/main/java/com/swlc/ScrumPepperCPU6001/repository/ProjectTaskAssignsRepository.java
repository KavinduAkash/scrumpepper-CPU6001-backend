package com.swlc.ScrumPepperCPU6001.repository;

import com.swlc.ScrumPepperCPU6001.entity.*;
import com.swlc.ScrumPepperCPU6001.enums.*;
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

//    @Query("SELECT t.projectTaskEntity.projectUserStoryEntity FROM ProjectTaskAssignsEntity t, ProjectSprintUserStoryEntity su WHERE t.projectTaskEntity.projectUserStoryEntity = su.projectUserStoryEntity AND su.projectSprintEntity=:sprint AND t.projectMemberEntity=:member AND t.statusType=:tsstatus GROUP BY t.projectTaskEntity.projectUserStoryEntity")
//    List<ProjectUserStoryEntity> getByProjectMemberEntityAndStatusType(@Param("sprint") ProjectSprintEntity sprintEntity , @Param("member") ProjectMemberEntity projectMemberEntity, @Param("tsstatus") StatusType statusType);

    @Query("SELECT t.projectTaskEntity.projectUserStoryEntity \n" +
            "FROM \n" +
            "ProjectTaskAssignsEntity t, ProjectSprintUserStoryEntity su \n" +
            "WHERE \n" +
            "t.projectTaskEntity.projectUserStoryEntity = su.projectUserStoryEntity AND \n" +
            "su.projectSprintEntity=:sprint AND \n" +
            "t.projectMemberEntity=:member AND \n" +
            "(su.removedDate IS NULL OR su.removedDate>su.projectSprintEntity.endDate) AND \n" +
            "su.status<>:tsstatus \n" +
            "GROUP BY t.projectTaskEntity.projectUserStoryEntity")
    List<ProjectUserStoryEntity> getByProjectMemberEntityAndStatusType(@Param("sprint") ProjectSprintEntity sprintEntity , @Param("member") ProjectMemberEntity projectMemberEntity, @Param("tsstatus") SprintUserStoryStatus statusType);


    @Query("SELECT t.projectTaskEntity.projectUserStoryEntity \n" +
            "FROM \n" +
            "ProjectTaskAssignsEntity t, ProjectSprintUserStoryEntity su \n" +
            "WHERE \n" +
            "t.projectTaskEntity.projectUserStoryEntity = su.projectUserStoryEntity AND \n" +
            "su.projectSprintEntity=:sprint AND \n" +
            "t.projectMemberEntity=:member AND \n" +
            "(su.removedDate IS NULL) AND \n" +
            "su.status=:tsstatus AND \n" +
            "t.projectTaskEntity.projectUserStoryEntity.statusType=:ustatus \n" +
            "GROUP BY t.projectTaskEntity.projectUserStoryEntity")
    List<ProjectUserStoryEntity> getByCompleteProjectMemberEntityAndStatusType(@Param("sprint") ProjectSprintEntity sprintEntity , @Param("member") ProjectMemberEntity projectMemberEntity, @Param("tsstatus") SprintUserStoryStatus statusType, @Param("ustatus") UserStoryStatusType userStoryStatusType);
}