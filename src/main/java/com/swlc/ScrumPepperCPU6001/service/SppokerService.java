package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.SppokerDTO;
import com.swlc.ScrumPepperCPU6001.dto.request.HandleSppokerRoomRequestDTO;

import java.util.List;

/**
 * @author hp
 */
public interface SppokerService {
    boolean createRoom(HandleSppokerRoomRequestDTO handleSppokerRoomRequestDTO);
    boolean updateRoom(HandleSppokerRoomRequestDTO handleSppokerRoomRequestDTO);
    List<SppokerDTO> getRoom(long projectId);
}
