package com.swlc.ScrumPepperCPU6001.service.impl;

import com.swlc.ScrumPepperCPU6001.constant.ApplicationConstant;
import com.swlc.ScrumPepperCPU6001.dto.*;
import com.swlc.ScrumPepperCPU6001.dto.response.BurnDownChartResponseDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.MemberResponsibilityDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.UserStoryResponsibilityDTO;
import com.swlc.ScrumPepperCPU6001.entity.*;
import com.swlc.ScrumPepperCPU6001.enums.*;
import com.swlc.ScrumPepperCPU6001.exception.CorporateException;
import com.swlc.ScrumPepperCPU6001.exception.ProjectException;
import com.swlc.ScrumPepperCPU6001.repository.*;
import com.swlc.ScrumPepperCPU6001.service.ReportService;
import com.swlc.ScrumPepperCPU6001.util.DateManager;
import com.swlc.ScrumPepperCPU6001.util.TokenValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author hp
 */
@Log4j2
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private TokenValidator tokenValidator;
    @Autowired
    private DateManager dateManager;
    private final CorporateRepository corporateRepository;
    private final CorporateEmployeeRepository corporateEmployeeRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final SprintRepository sprintRepository;
    private final ProjectSprintUserStoryRepository sprintUserStoryRepository;
    private final UserStoryTrackRepository userStoryTrackRepository;
    private final ProjectTaskAssignsRepository projectTaskAssignsRepository;

    public ReportServiceImpl(CorporateRepository corporateRepository,
                             CorporateEmployeeRepository corporateEmployeeRepository,
                             ProjectRepository projectRepository,
                             ProjectMemberRepository projectMemberRepository, SprintRepository sprintRepository,
                             ProjectSprintUserStoryRepository sprintUserStoryRepository, UserStoryTrackRepository userStoryTrackRepository, ProjectTaskAssignsRepository projectTaskAssignsRepository) {
        this.corporateRepository = corporateRepository;
        this.corporateEmployeeRepository = corporateEmployeeRepository;
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.sprintRepository = sprintRepository;
        this.sprintUserStoryRepository = sprintUserStoryRepository;
        this.userStoryTrackRepository = userStoryTrackRepository;
        this.projectTaskAssignsRepository = projectTaskAssignsRepository;
    }

    @Override
    public BurnDownChartResponseDTO getSprintBurnDownChart(long sprintId) {
        log.info("Execute method getSprintBurnDownChart: @Param{} " + sprintId);
        try {
            // check sprint
            Optional<ProjectSprintEntity> sprintById = sprintRepository.findById(sprintId);
            if(!sprintById.isPresent())
                throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "Sprint not found");
            // check auth
//            authProject(sprintById.get().getProjectEntity(), 1);
            Date startDate = sprintById.get().getStartDate();
            Date endDate = sprintById.get().getEndDate();
            LocalDate localDateStart = dateManager.convertToLocalDateViaMilisecond(startDate);
            LocalDate localDateEnd = dateManager.convertToLocalDateViaMilisecond(endDate);
            List<LocalDate> datesBetween = dateManager.getDatesBetween(localDateStart, localDateEnd);
            datesBetween.add(localDateEnd);

            log.info("Now: " + new Date());
            log.info("Sprint Start Date: " + localDateStart);
            log.info("Sprint End Date: " + localDateEnd);
            log.info("Between Dates: " + datesBetween);


            int totalPoints = sprintUserStoryRepository.getTotalPoints(sprintById.get().getId());
            log.info("Total points: " + totalPoints);
            // calculate ideals

            int weekendDays = 1;

            for (LocalDate date :datesBetween) {
                if(dateManager.isWeekend(date)) {
                    weekendDays = weekendDays + 1;
                }
            }


            int i = 0;
            boolean st = true;
            int remainingEffort = totalPoints;
            List<BurnDownDataDTO> burnDownData = new ArrayList<>();
            for (int x=0; x<datesBetween.size(); x++) {
                log.info("------------------------");
                LocalDate date = datesBetween.get(x);

                if(date==null) {
//                    if(st) {
//                        burnDownData.add(new BurnDownDataDTO(null, "start", totalPoints, totalPoints, true));
//                        st = false;
//                    } else {
//                        burnDownData.add(new BurnDownDataDTO(null, "end", 0, 0, true));
//                    }
                } else {
                    if(dateManager.isWeekend(date)) {
                        i = i - 1;
                    }
                    Double xxx = (Double.valueOf(totalPoints) / (datesBetween.size()-weekendDays))*i;
                    Double ideal = totalPoints - (xxx);


                    i = i + 1;
                    int dayTrackPoints = userStoryTrackRepository.getDayTrackPoints(sprintById.get().getId(), datesBetween.get(x).toString());
                    int remainingDayEffort = remainingEffort - dayTrackPoints;
                    remainingEffort = remainingDayEffort;
                    log.info("Date -> " + date.toString());
                    log.info("1. Ideal: " + ideal);
                    log.info("2. Points: " + dayTrackPoints);
                    log.info("3. Remain: " + remainingDayEffort);

                    burnDownData.add(new BurnDownDataDTO(date, date.toString(), remainingDayEffort, ideal, true));
                }
            }

            return new BurnDownChartResponseDTO(null, totalPoints, burnDownData);
        } catch (Exception e) {
            log.error("Method getSprintBurnDownChart: " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public BurnDownChartResponseDTO getSprintBurnUpChart(long sprintId) {
        log.info("Execute method getSprintBurnUpChart: @Param{} " + sprintId);
        try {
            // check sprint
            Optional<ProjectSprintEntity> sprintById = sprintRepository.findById(sprintId);
            if(!sprintById.isPresent())
                throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "Sprint not found");
            // check auth
//            authProject(sprintById.get().getProjectEntity(), 1);
            Date startDate = sprintById.get().getStartDate();
            Date endDate = sprintById.get().getEndDate();
            LocalDate localDateStart = dateManager.convertToLocalDateViaMilisecond(startDate);
            LocalDate localDateEnd = dateManager.convertToLocalDateViaMilisecond(endDate);
            List<LocalDate> datesBetween = dateManager.getDatesBetween(localDateStart, localDateEnd);
            datesBetween.add(localDateEnd);

            log.info("Now: " + new Date());
            log.info("Sprint Start Date: " + localDateStart);
            log.info("Sprint End Date: " + localDateEnd);
            log.info("Between Dates: " + datesBetween);


            int totalPoints = sprintUserStoryRepository.getTotalPoints(sprintById.get().getId());
            log.info("Total points: " + totalPoints);
            // calculate ideals

            int weekendDays = 1;

            for (LocalDate date :datesBetween) {
                if(dateManager.isWeekend(date)) {
                    weekendDays = weekendDays + 1;
                }
            }


            int i = 0;
            boolean st = true;
            int remainingEffort = totalPoints;
            List<BurnDownDataDTO> burnDownData = new ArrayList<>();
            for (int x=0; x<datesBetween.size(); x++) {
                log.info("------------------------");
                LocalDate date = datesBetween.get(x);

                if(date==null) {
//                    if(st) {
//                        burnDownData.add(new BurnDownDataDTO(null, "start", totalPoints, totalPoints, true));
//                        st = false;
//                    } else {
//                        burnDownData.add(new BurnDownDataDTO(null, "end", 0, 0, true));
//                    }
                } else {
                    if(dateManager.isWeekend(date)) {
                        i = i - 1;
                    }
                    Double xxx = (Double.valueOf(totalPoints) / (datesBetween.size()-weekendDays))*i;
                    Double ideal = xxx;


                    i = i + 1;
                    int dayTrackPoints = userStoryTrackRepository.getDayTrackPoints(sprintById.get().getId(), datesBetween.get(x).toString());
                    int remainingDayEffort = remainingEffort - dayTrackPoints;
                    remainingEffort = remainingDayEffort;
                    log.info("Date -> " + date.toString());
                    log.info("1. Ideal: " + ideal);
                    log.info("2. Points: " + dayTrackPoints);
                    log.info("3. Remain: " + remainingDayEffort);

                    burnDownData.add(new BurnDownDataDTO(date, date.toString(), dayTrackPoints, ideal, true));
                }
            }

            return new BurnDownChartResponseDTO(null, totalPoints, burnDownData);
        } catch (Exception e) {
            log.error("Method getSprintBurnUpChart: " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<SprintVelocityDTO> getSprintVelocity(long projectId) {
        log.info("Execute method getSprintVelocity: @Param{} " + projectId);
        List<SprintVelocityDTO> result =  new ArrayList<>();
        try {
            // check auth
//            authProject(sprintById.get().getProjectEntity(), 1);
            Optional<ProjectEntity> projectById = projectRepository.findById(projectId);
            if(!projectById.isPresent())
                throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "Project not found");
            List<ProjectSprintEntity> activeSprints = sprintRepository.getActiveSprints(projectById.get(), SprintStatusType.DELETE);
            for (ProjectSprintEntity s : activeSprints) {
                Date startDate = s.getStartDate();
                Date endDate = s.getEndDate();
                LocalDate localDateStart = dateManager.convertToLocalDateViaMilisecond(startDate);
                LocalDate localDateEnd = dateManager.convertToLocalDateViaMilisecond(endDate);
                List<LocalDate> datesBetween = dateManager.getDatesBetween(localDateStart, localDateEnd);
                datesBetween.add(localDateEnd);
                int totalPoints = sprintUserStoryRepository.getTotalPoints(s.getId());
                log.info("Total points: " + totalPoints);
                int totalDone = 0;
                for (int x=0; x<datesBetween.size(); x++) {
                    LocalDate date = datesBetween.get(x);
                    int dayTrackPoints = userStoryTrackRepository.getDayTrackPoints(s.getId(), datesBetween.get(x).toString());
                    totalDone = totalDone + dayTrackPoints;
                }
                result.add(new SprintVelocityDTO(this.prepareSprintDTO(s), totalPoints, totalDone));
            }
            return result;
        } catch (Exception e) {
            log.error("Method getSprintVelocity: " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<MemberResponsibilityDTO> getTeamPerformance(long sprintId) {
        log.info("Execute method getTeamPerformance: @Param{} " + sprintId);
        List<MemberResponsibilityDTO> result = new ArrayList<>();
        try {
            // check sprint
            Optional<ProjectSprintEntity> sprintById = sprintRepository.findById(sprintId);
            if(!sprintById.isPresent())
                throw new ProjectException(ApplicationConstant.RESOURCE_NOT_FOUND, "Sprint not found");

            // check auth
//            authProject(sprintById.get().getProjectEntity(), 1);

            ProjectEntity projectEntity = sprintById.get().getProjectEntity();

            List<ProjectMemberEntity> projectMembers = projectMemberRepository.findByProjectEntity(projectEntity);

            int projectPointCount = sprintUserStoryRepository.getProjectPointCount(sprintById.get());

            for (ProjectMemberEntity projectMember : projectMembers) {
                int totalPoints = 0;
                int doneTotalPoints = 0;

                List<ProjectUserStoryEntity> byProjectMemberEntityAndStatusType =
                        projectTaskAssignsRepository.getByProjectMemberEntityAndStatusType(
                                sprintById.get(),
                                projectMember,
                                SprintUserStoryStatus.DELETED
                        );


                List<ProjectUserStoryEntity> byCompleteProjectMemberEntityAndStatusType = projectTaskAssignsRepository.getByCompleteProjectMemberEntityAndStatusType(
                        sprintById.get(),
                        projectMember,
                        SprintUserStoryStatus.ACTIVE,
                        UserStoryStatusType.COMPLETED
                );

                List<UserStoryResponsibilityDTO> userStoryDTOList = new ArrayList<>();
                for (ProjectUserStoryEntity userStoryEntity : byProjectMemberEntityAndStatusType) {
                    int points = userStoryEntity.getPoints();
                    totalPoints = totalPoints + points;
                    double responsibility = (points * projectPointCount) / 100;
                    userStoryDTOList.add(
                            new UserStoryResponsibilityDTO(
                                new UserStoryDTO(
                                    userStoryEntity.getId(),
                                    null,
                                    userStoryEntity.getTitle(),
                                    userStoryEntity.getDescription(),
                                    userStoryEntity.getCreatedDate(),
                                    userStoryEntity.getModifiedDate(),
                                    null,
                                    null,
                                    userStoryEntity.getStatusType(),
                                    new ArrayList<>(),
                                    userStoryEntity.getPriority(),
                                    new ArrayList<>(),
                                    new ArrayList<>(),
                                    null,
                                    userStoryEntity.getPoints()
                                ),
                                 responsibility
                            )
                    );

                }

                int responsibility = (totalPoints * projectPointCount) / 100;

                for (ProjectUserStoryEntity userStoryEntity  : byCompleteProjectMemberEntityAndStatusType) {
                    int points = userStoryEntity.getPoints();
                    doneTotalPoints = doneTotalPoints + points;
                }

                int doneResponsibility = (doneTotalPoints * projectPointCount) / 100;

                result.add(
                        new MemberResponsibilityDTO(
                                new ProjectMemberDTO(
                                        projectMember.getId(),
                                        this.prepareCorporateEmployeeDTO(
                                                projectMember.getCorporateEmployeeEntity()
                                        ),
                                        projectMember.getAssignedDate(),
                                        projectMember.getModifiedDate(),
                                        projectMember.getScrumRole(),
                                        projectMember.getStatusType()
                                ),
                                doneResponsibility,
                                responsibility,
                                totalPoints,
                                projectPointCount,
                                userStoryDTOList
                        )
                );
            }

            return result;
        } catch (Exception e) {
            log.error("Method getTeamPerformance: " + e.getMessage(), e);
            throw e;
        }
    }

    private boolean authProject(ProjectEntity projectEntity, int level) {
        log.info("Execute method authProject: @Param{} " + projectEntity.toString());
        try {
            CorporateEntity corporateEntity = projectEntity.getCorporateEntity();
            UserEntity userAdminEntity = tokenValidator.retrieveUserInformationFromAuthentication();
            Optional<CorporateEmployeeEntity> auth_user_admin =
                    corporateEmployeeRepository.findByUserEntityAndCorporateEntityAndStatusType(
                            userAdminEntity,
                            corporateEntity,
                            CorporateAccessStatusType.ACTIVE
                    );
            if(!auth_user_admin.isPresent())
                throw new CorporateException(
                        ApplicationConstant.UN_AUTH_ACTION,
                        "Unauthorized action. You can't processed this action"
                );
            if(level==2) {
                if(!(auth_user_admin.get().getCorporateAccessType().equals(CorporateAccessType.CORPORATE_SUPER) ||
                        auth_user_admin.get().getCorporateAccessType().equals(CorporateAccessType.CORPORATE_ADMIN)))
                    throw new CorporateException(
                            ApplicationConstant.UN_AUTH_ACTION,
                            "Unauthorized action. You can't processed this action"
                    );
            }
            return true;
        } catch (Exception e) {
            log.error("Method authProject: " + e.getMessage(), e);
            throw e;
        }
    }

    private SprintDTO prepareSprintDTO(ProjectSprintEntity save) {
        try {
            return new SprintDTO(
                    save.getId(),
                    save.getProjectEntity().getId(),
                    save.getSprintName(),
                    save.getDescription(),
                    save.getCreatedDate(),
                    save.getModifiedDate(),
                   null,
                    null,
                    save.getStatusType()
            );
        } catch (Exception e) {
            throw e;
        }
    }

    private CorporateEmployeeDTO prepareCorporateEmployeeDTO(CorporateEmployeeEntity c) {
//        log.info("Execute method prepareCorporateEmployeeDTO : @Param {} " + c);
        try {
            return new CorporateEmployeeDTO(
                    c.getId(),
                    new UserDTO(
                            c.getUserEntity().getId(),
                            c.getUserEntity().getRefNo(),
                            c.getUserEntity().getFirstName(),
                            c.getUserEntity().getLastName(),
                            c.getUserEntity().getContactNumber(),
                            c.getUserEntity().getEmail(),
                            null,
                            c.getUserEntity().getCreatedDate(),
                            c.getUserEntity().getStatusType()
                    ),
                    null,
                    c.getCorporateAccessType(),
                    c.getCreatedDate(),
                    c.getModifiedDate(),
                    c.getAcceptedDate(),
                    c.getStatusType()
            );
        } catch (Exception e) {
            log.error("Method prepareUserStoryDTO : " + e.getMessage(), e);
            throw e;
        }
    }

}
