package amos.specitemdatabase.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="document_name", length = 50, nullable=false, unique=false)
    private String name;

    @OneToMany
    private List<SpecItemEntity> specItemEntities;

    @ManyToOne
    private Commit commit;

    public DocumentEntity(String name, List<SpecItemEntity> specItemEntities, Commit commit) {
        this.name = name;
        this.specItemEntities = specItemEntities;
        this.commit = commit;
    }

    public DocumentEntity() {
    }

}
