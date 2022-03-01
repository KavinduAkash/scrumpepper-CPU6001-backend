package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.TaskDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddProjectTaskRequestDTO;

import java.util.List;

/**
 * @author hp
 */
public interface TaskService {
    TaskDTO createNewTask(AddProjectTaskRequestDTO task);
    TaskDTO addMemberToTask(long taskId, long id);
    TaskDTO removedMemberFromTask(long taskId, long id);
    List<TaskDTO> getAllTasksOfProject(long userStoryId);
    List<TaskDTO> changeTaskStatus(long taskId, String status);
}
