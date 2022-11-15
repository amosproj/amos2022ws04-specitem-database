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

    @OneToMany(cascade = {CascadeType.ALL})
    private List<SpecItem> specItem;

    @ManyToOne(cascade = {CascadeType.ALL})
    private Commit commit;

    public DocumentEntity(String name, List<SpecItem> specItem, Commit commit) {
        this.name = name;
        this.specItem = specItem;
        this.commit = commit;
    }

    public DocumentEntity() {
    }

}
