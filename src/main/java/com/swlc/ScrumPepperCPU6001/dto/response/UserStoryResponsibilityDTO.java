package com.swlc.ScrumPepperCPU6001.dto.response;

import com.swlc.ScrumPepperCPU6001.dto.UserStoryDTO;
import lombok.*;

/**
 * @author hp
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserStoryResponsibilityDTO {
    private UserStoryDTO userStory;
    private double responsibility;
}
