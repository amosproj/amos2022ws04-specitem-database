package amos.specitemdatabase.filegenerator;

import amos.specitemdatabase.model.Commit;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

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
        final LocalDateTime now = LocalDateTime.now();
        return new Commit(commitHash, faker.regexify("[a-z1-9]{"+COMMIT_MSG_LEN+"}"),
            now, faker.name().name());
    }
}
