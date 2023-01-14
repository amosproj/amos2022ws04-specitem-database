package amos.specitemdatabase.model;

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
    // Adding the @Version annotation activates the optimistic locking mechanism.
    // Having optimistic locking, the DB will mark the version when it reads the entity,
    // and when it writes the updated entity back, it will check if the version has been modified.
    // If yes, an exception will be thrown.
    @Version
    private Long version;
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
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TagInfo tagInfo = (TagInfo) o;
        return shortName.equals(tagInfo.shortName) && time.equals(tagInfo.time) &&
            Objects.equals(version, tagInfo.version) && Objects.equals(tags, tagInfo.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shortName, time, version, tags);
    }
}
