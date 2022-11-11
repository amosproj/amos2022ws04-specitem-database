package amos.specitemdatabase.model;

import java.time.ZonedDateTime;
import lombok.Data;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Represent a single SVN commit.
 */
@Getter
@Entity
public class Commit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String commitHash;
    private String commitMessage;
    private ZonedDateTime commitTime;
    private String commitAuthor;

    public Commit(String commitHash, String commitMessage, ZonedDateTime commitTime, String commitAuthor) {
        this.commitHash = commitHash;
        this.commitMessage = commitMessage;
        this.commitTime = commitTime;

        this.commitAuthor = commitAuthor;
    }

    public Commit() {

    }

    public static Commit fromString(final String commit) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
