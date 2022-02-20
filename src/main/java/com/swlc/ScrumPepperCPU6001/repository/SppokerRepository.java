package com.swlc.ScrumPepperCPU6001.repository;

import com.swlc.ScrumPepperCPU6001.entity.ProjectEntity;
import com.swlc.ScrumPepperCPU6001.entity.SpppokerRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author hp
 */
public interface SppokerRepository extends JpaRepository<SpppokerRoomEntity, Long> {
    List<SpppokerRoomEntity> findByProjectEntity(ProjectEntity projectEntity);
}
