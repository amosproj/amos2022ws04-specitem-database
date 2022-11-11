package amos.specitemdatabase.model;

import java.time.LocalDate;
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
    private final LocalDate commitTime;
    private final String commitAuthor;
}
