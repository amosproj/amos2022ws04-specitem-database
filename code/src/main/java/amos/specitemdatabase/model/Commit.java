package amos.specitemdatabase.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Data;

/**
 * Represent a single SVN commit.
 */
@Data
public class Commit {

    private final String commitHash;
    private final String commitMessage;
    private final LocalDate commitTime;
    private final String commitAuthor;

    public static Commit getCommitFromString(String commitText) {
        final String regex = "(CommitHash: (?<CommitHash>\\S+)\\r\\n)(CommitDate: (?<CommitDate>\\S+)\\r\\n)(CommitMsg: (?<CommitMsg>[\\S\\s]+)\\r\\n)(CommitAuthor: (?<CommitAuthor>\\S+))";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(commitText);
        matcher.find();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        return new Commit(
                matcher.group("CommitHash"),
                matcher.group("CommitMsg"),
                LocalDate.parse(matcher.group("CommitDate"),formatter),
                matcher.group("CommitAuthor"));
    }
}
