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
public class CreateDocRequestDTO {
    private long projectId;
    private String name;

    @Override
    public String toString() {
        return "CreateDocRequestDTO{" +
                "projectId=" + projectId +
                ", name='" + name + '\'' +
                '}';
    }
}
