package com.swlc.ScrumPepperCPU6001.controller.user;

import com.swlc.ScrumPepperCPU6001.dto.TaskDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddProjectTaskRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.AddProjectTasksAssignsRequestDTO;
import com.swlc.ScrumPepperCPU6001.dto.response.CommonResponseDTO;
import com.swlc.ScrumPepperCPU6001.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hp
 */
@RestController
@CrossOrigin
@RequestMapping("v1/task")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity handleTask(@RequestBody AddProjectTaskRequestDTO addProjectTaskRequestDTO) {
        TaskDTO result = taskService.createNewTask(addProjectTaskRequestDTO);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Task created successfully", result),
                HttpStatus.OK
        );
    }

    @PatchMapping(value = "/status",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity handleTaskStatus(@RequestParam("id") long taskId, @RequestParam("status") String status) {
        List<TaskDTO> result = taskService.changeTaskStatus(taskId, status);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Task status changed successfully", result),
                HttpStatus.OK
        );
    }

    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity assignMembersToTask(@RequestBody AddProjectTasksAssignsRequestDTO addProjectTasksAssignsRequestDTO) {
        TaskDTO result = taskService.addMemberToTask(addProjectTasksAssignsRequestDTO.getTaskId(), addProjectTasksAssignsRequestDTO.getCorporateEmployeeId());
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Task created successfully", result),
                HttpStatus.OK
        );
    }

    @PatchMapping(value = "/remove", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity assignMembersRemove(@RequestBody AddProjectTasksAssignsRequestDTO addProjectTasksAssignsRequestDTO) {
        TaskDTO result = taskService.removedMemberFromTask(addProjectTasksAssignsRequestDTO.getTaskId(), addProjectTasksAssignsRequestDTO.getCorporateEmployeeId());
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Task created successfully", result),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllTasks(@PathVariable long id) {
        List<TaskDTO> results = taskService.getAllTasksOfProject(id);
        return new ResponseEntity<>(
                new CommonResponseDTO(true, "Task created successfully", results),
                HttpStatus.OK
        );
    }
}
