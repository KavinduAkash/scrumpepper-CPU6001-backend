package com.swlc.ScrumPepperCPU6001.repository;

import com.swlc.ScrumPepperCPU6001.entity.UserStoryTrackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author hp
 */
public interface UserStoryTrackRepository extends JpaRepository<UserStoryTrackEntity, Long> {
}
