package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.request.AddProjectTaskRequestDTO;

/**
 * @author hp
 */
public interface TaskService {
    boolean createNewTask(AddProjectTaskRequestDTO task);
    boolean addMemberToTask(long taskId, long id);
}
