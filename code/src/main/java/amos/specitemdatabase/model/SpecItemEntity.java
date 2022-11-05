package amos.specitemdatabase.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SpecItemEntity {
    private Long id;

    // TODO: we might need

    public void setId(final Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }
}
