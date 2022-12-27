package amos.specitemdatabase.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@IdClass(SpecItemId.class)
@Entity
@Getter
@Setter
public class TagInfo {

    @Id
    private String shortName;
    @Column(columnDefinition = "TIMESTAMP")
    @Id
    private LocalDateTime time;
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    private String tags;

    @OneToOne
    private SpecItem specItem;

    public TagInfo() {
    }

    @Override
    public String toString() {
        return tags;
    }
}
