package amos.specitemdatabase.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private static final DateTimeFormatter FORMATTER
        = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String commitHash;
    private String commitMessage;
    private LocalDateTime commitTime;
    private String commitAuthor;

    public Commit(String commitHash, String commitMessage, LocalDateTime commitTime, String commitAuthor) {
        this.commitHash = commitHash;
        this.commitMessage = commitMessage;
        this.commitTime = commitTime;
        this.commitAuthor = commitAuthor;
    }

    public Commit() {

    }

    public static Commit getCommitFromString(String commitText) {
        final String regex = "(CommitHash: (?<CommitHash>\\S+)\\R)(CommitDate: (?<CommitDate>[\\S ]+)\\R)(CommitMsg: (?<CommitMsg>[\\S ]+)\\R)(CommitAuthor: (?<CommitAuthor>[\\S ]+))";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(commitText);
        matcher.find();
        return new Commit(
                matcher.group("CommitHash"),
                restoreWholeText(matcher.group("CommitMsg")),
                LocalDateTime.parse(matcher.group("CommitDate"), FORMATTER),
                matcher.group("CommitAuthor"));
    }

    @Override
    public String toString() {
        final String time =  this.commitTime.format(FORMATTER);
        return
            "CommitHash: " + this.commitHash + "\n" +
            "CommitDate: " + time + "\n" +
            "CommitMsg: " + this.commitMessage + "\n" +
            "CommitAuthor: " + this.commitAuthor + "\n";
    }


}
