package com.swlc.ScrumPepperCPU6001.dto.request;

import com.swlc.ScrumPepperCPU6001.enums.UserStoryStatusType;
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
public class UpdateUserStoryStatusRequestDTO {
    private UserStoryStatusType status;

    @Override
    public String toString() {
        return "UpdateUserStoryStatusRequestDTO{" +
                "status=" + status +
                '}';
    }
}
