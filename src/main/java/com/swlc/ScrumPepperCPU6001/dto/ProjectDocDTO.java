package com.swlc.ScrumPepperCPU6001.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author hp
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDocDTO {
    private long id;
    private ProjectDTO project;
    private String name;
    private String doc;
    private Date createdDate;
    private Date modifiedDate;

    @Override
    public String toString() {
        return "ProjectDocDTO{" +
                "id=" + id +
                ", project=" + project +
                ", name='" + name + '\'' +
                ", doc='" + doc + '\'' +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                '}';
    }
}
