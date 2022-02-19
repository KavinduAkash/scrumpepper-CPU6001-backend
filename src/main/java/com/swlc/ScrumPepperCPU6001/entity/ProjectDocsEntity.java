package com.swlc.ScrumPepperCPU6001.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @author hp
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project_docs")
public class ProjectDocsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="project_id")
    private ProjectEntity projectEntity;
    @Column(nullable = false)
    private String name;
    @Column(length = 65535, columnDefinition = "text")
    private String doc;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedDate;

    public ProjectDocsEntity(ProjectEntity projectEntity, String name, String doc, Date createdDate, Date modifiedDate) {
        this.projectEntity = projectEntity;
        this.name = name;
        this.doc = doc;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    @Override
    public String toString() {
        return "ProjectDocsEntity{" +
                "id=" + id +
                ", projectEntity=" + projectEntity +
                ", name='" + name + '\'' +
                ", doc='" + doc + '\'' +
                ", createdDate=" + createdDate +
                ", modifiedDate=" + modifiedDate +
                '}';
    }
}
