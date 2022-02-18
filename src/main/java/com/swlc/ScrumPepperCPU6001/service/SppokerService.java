package com.swlc.ScrumPepperCPU6001.service;

import com.swlc.ScrumPepperCPU6001.dto.request.HandleSppokerRoomRequestDTO;

/**
 * @author hp
 */
public interface SppokerService {
    boolean createRoom(HandleSppokerRoomRequestDTO handleSppokerRoomRequestDTO);
    boolean updateRoom(HandleSppokerRoomRequestDTO handleSppokerRoomRequestDTO);
}
