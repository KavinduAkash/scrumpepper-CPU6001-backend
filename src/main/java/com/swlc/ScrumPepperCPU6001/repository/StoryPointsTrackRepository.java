package com.swlc.ScrumPepperCPU6001.repository;

import com.swlc.ScrumPepperCPU6001.entity.StoryPointsTrackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author hp
 */
public interface StoryPointsTrackRepository extends JpaRepository<StoryPointsTrackEntity, Long> {
}
