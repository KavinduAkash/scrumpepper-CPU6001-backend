package com.swlc.ScrumPepperCPU6001.dto;

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
public class UserStoryLblDTO {
    private long id;
    private ProjectDTO project;
    private String lbl;

    @Override
    public String toString() {
        return "UserStoryLblDTO{" +
                "id=" + id +
                ", project=" + project +
                ", lbl='" + lbl + '\'' +
                '}';
    }
}
