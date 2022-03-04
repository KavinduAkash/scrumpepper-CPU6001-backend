package com.swlc.ScrumPepperCPU6001.service.impl;

import com.swlc.ScrumPepperCPU6001.constant.ApplicationConstant;
import com.swlc.ScrumPepperCPU6001.dto.BurnDownDataDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.BurnDownChartResponseDTO;
import com.swlc.ScrumPepperCPU6001.entity.*;
import com.swlc.ScrumPepperCPU6001.enums.CorporateAccessStatusType;
import com.swlc.ScrumPepperCPU6001.enums.CorporateAccessType;
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

    public ReportServiceImpl(CorporateRepository corporateRepository,
                             CorporateEmployeeRepository corporateEmployeeRepository,
                             ProjectRepository projectRepository,
                             ProjectMemberRepository projectMemberRepository, SprintRepository sprintRepository,
                             ProjectSprintUserStoryRepository sprintUserStoryRepository, UserStoryTrackRepository userStoryTrackRepository) {
        this.corporateRepository = corporateRepository;
        this.corporateEmployeeRepository = corporateEmployeeRepository;
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.sprintRepository = sprintRepository;
        this.sprintUserStoryRepository = sprintUserStoryRepository;
        this.userStoryTrackRepository = userStoryTrackRepository;
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

}
