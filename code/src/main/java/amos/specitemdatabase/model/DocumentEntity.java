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
    private List<SpecItem> specItems;

    @ManyToOne(cascade = {CascadeType.ALL})
    private Commit commit;

    public DocumentEntity(String name, List<SpecItem> specItems, Commit commit) {
        this.name = name;
        this.specItems = specItems;
        this.commit = commit;
    }

    public DocumentEntity() {
    }

}
