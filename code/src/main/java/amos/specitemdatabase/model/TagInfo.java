package amos.specitemdatabase.model;

import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;

@IdClass(SpecItemId.class)
@Entity
@Cache(usage = READ_WRITE)
@Getter
@Setter
public class TagInfo {

    @Id
    @Column(name = "short_name")
    private String shortName;
    @Column(columnDefinition = "TIMESTAMP", name = "commit_time")
    @Id
    private LocalDateTime commitTime;
    // Adding the @Version annotation activates the optimistic locking mechanism.
    // Having optimistic locking, the DB will mark the version when it reads the entity,
    // and when it writes the updated entity back, it will check if the version has been modified.
    // If yes, an exception will be thrown.
    @Column(name = "version", columnDefinition = "integer DEFAULT 0", nullable = false)
    @Version
    private Long version = 0L;
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

    @Override
    public int hashCode() {
        return Objects.hash(shortName, commitTime, version, tags);
    }


}
