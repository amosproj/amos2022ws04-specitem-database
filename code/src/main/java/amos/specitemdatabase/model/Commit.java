package amos.specitemdatabase.model;

import java.time.ZonedDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Represent a single SVN commit.
 */
@Data
public class Commit {

    private final String commitHash;
    private final String commitMessage;
    private final ZonedDateTime commitTime;
    private final String commitAuthor;

    public static Commit fromString(final String commit) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
