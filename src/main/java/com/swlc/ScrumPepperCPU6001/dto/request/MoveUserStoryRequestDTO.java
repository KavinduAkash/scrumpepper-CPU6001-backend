package com.swlc.ScrumPepperCPU6001.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author hp
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MoveUserStoryRequestDTO {
    private long userStoryId;
    private long sprintId;

    @Override
    public String toString() {
        return "MoveUserStoryRequestDTO{" +
                "userStoryId=" + userStoryId +
                ", sprintId=" + sprintId +
                '}';
    }
}
