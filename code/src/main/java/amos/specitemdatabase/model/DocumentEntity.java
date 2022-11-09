package amos.specitemdatabase.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="document_name", length = 50, nullable=false, unique=false)
    private String name;

    @OneToMany
    private List<SpecItemEntity> specItemEntities;

    public DocumentEntity(String name, List<SpecItemEntity> specItemEntities) {
        this.name = name;
        this.specItemEntities = specItemEntities;
    }

    public DocumentEntity() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SpecItemEntity> getSpecItemEntities() {
        return specItemEntities;
    }

    public void setSpecItemEntities(List<SpecItemEntity> specItemEntities) {
        this.specItemEntities = specItemEntities;
    }
}
