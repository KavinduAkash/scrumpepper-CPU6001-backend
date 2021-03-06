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
public class AddUserStoryLblRequestDTO {
    private long projectId;
    private String lbl;

    @Override
    public String toString() {
        return "AddUserStoryLblRequestDTO{" +
                "projectId=" + projectId +
                ", lbl='" + lbl + '\'' +
                '}';
    }
}
