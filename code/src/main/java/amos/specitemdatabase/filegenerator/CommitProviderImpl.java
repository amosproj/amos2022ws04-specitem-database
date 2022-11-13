package amos.specitemdatabase.filegenerator;

import amos.specitemdatabase.model.Commit;
import com.github.javafaker.Faker;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class CommitProviderImpl implements CommitProvider {

    private static final short COMMIT_HASH_LEN = 14;
    private static final short COMMIT_MSG_LEN = 20;

    @Override
    public Commit generateCommit() {
        final Faker faker = new Faker();
        final String commitHash = UUID.randomUUID()
            .toString()
            .substring(0, COMMIT_HASH_LEN);
        return new Commit(commitHash, faker.regexify("[a-z1-9]{"+COMMIT_MSG_LEN+"}"),
            ZonedDateTime.now(), faker.name().name());
    }
}
