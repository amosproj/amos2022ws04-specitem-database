package amos.specitemdatabase.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class CommitTest {

    @Test
    public void testCommitToStr() {

        final LocalDateTime now = LocalDateTime.now();

        final Commit commit = new Commit(
            "hash1",
            "Msg1",
            now,
            "Max Mustermann"
        );

        final String expected =  "CommitHash: " + "hash1" + "\n" +
            "CommitData: " + now + "\n" +
            "CommitMsg: " + "Msg1" + "\n" +
            "CommitAuthor: " + "Max Mustermann" + "\n";
        Assertions.assertThat(commit.toString()).isEqualTo(expected);

    }
}
