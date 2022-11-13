package amos.specitemdatabase.model;

import java.time.ZonedDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class CommitTest {

    @Test
    public void testCommitToStr() {

        final ZonedDateTime now = ZonedDateTime.now();

        final Commit commit = new Commit(
            "hash1",
            "Msg1",
            now,
            "Max Mustermann"
        );

        final String expected =  "CommitHash: " + "hash1" + "\n" +
            "CommitData: " + now.toInstant().toString() + "\n" +
            "CommitMsg: " + "Msg1" + "\n" +
            "CommitAuthor: " + "Max Mustermann" + "\n";
        Assertions.assertThat(commit.toString()).isEqualTo(expected);

    }
}
