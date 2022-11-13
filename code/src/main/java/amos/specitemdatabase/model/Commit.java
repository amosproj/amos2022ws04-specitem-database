package amos.specitemdatabase.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Data;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static amos.specitemdatabase.importer.SpecItemParser.restoreWholeText;

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
    private LocalDate commitTime;
    private String commitAuthor;

    public Commit(String commitHash, String commitMessage, LocalDate commitTime, String commitAuthor) {
        this.commitHash = commitHash;
        this.commitMessage = commitMessage;
        this.commitTime = commitTime;

        this.commitAuthor = commitAuthor;
    }

    public Commit() {

    }

    public static Commit getCommitFromString(String commitText) {
        final String regex = "(CommitHash: (?<CommitHash>\\S+)\\R)(CommitDate: (?<CommitDate>\\S+)\\R)(CommitMsg: (?<CommitMsg>[\\S ]+)\\R)(CommitAuthor: (?<CommitAuthor>\\S+))";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(commitText);
        matcher.find();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        return new Commit(
                matcher.group("CommitHash"),
                restoreWholeText(matcher.group("CommitMsg")),
                LocalDate.parse(matcher.group("CommitDate"),formatter),
                matcher.group("CommitAuthor"));
    }
}
