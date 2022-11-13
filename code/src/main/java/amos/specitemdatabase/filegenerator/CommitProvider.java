package amos.specitemdatabase.filegenerator;

import amos.specitemdatabase.model.Commit;

/**
 * Provides commit information.
 */
public interface CommitProvider {

    /**
     * Generates a commit.
     * @return an instance of {@link Commit}
     */
    Commit generateCommit();
}

