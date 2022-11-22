package amos.specitemdatabase.model;

import java.time.format.DateTimeFormatter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class CommitTest {

    private static final DateTimeFormatter FORMATTER
        = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    public void testCommitToStr() {

        final LocalDateTime now = LocalDateTime.now();
        final String formattedTime = now.format(FORMATTER);

        final Commit commit = new Commit(
            "hash1",
            "Msg1",
            now,
            "Max Mustermann"
        );

        final String expected =  "CommitHash: " + "hash1" + "\n" +
            "CommitDate: " + formattedTime + "\n" +
            "CommitMsg: " + "Msg1" + "\n" +
            "CommitAuthor: " + "Max Mustermann" + "\n";
        Assertions.assertThat(commit.toString()).isEqualTo(expected);

    }
}
