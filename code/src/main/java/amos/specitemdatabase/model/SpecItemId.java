package amos.specitemdatabase.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class SpecItemId implements Serializable {

    private String shortName;
    private LocalDateTime commitTime;

    public SpecItemId(final String shortName, final LocalDateTime commitTime) {
        this.shortName = shortName;
        this.commitTime = commitTime;
    }

    public SpecItemId() {
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SpecItemId that = (SpecItemId) o;
        return shortName.equals(that.shortName) && commitTime.equals(that.commitTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shortName, commitTime);
    }
}
