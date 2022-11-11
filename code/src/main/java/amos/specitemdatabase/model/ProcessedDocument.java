package amos.specitemdatabase.model;

import java.util.List;

public class ProcessedDocument {

    private final Commit commit;

    private final List<SpecItem> specItems;

    public ProcessedDocument(final Commit commit, final List<SpecItem> specItems) {
        this.commit = commit;
        this.specItems = specItems;
    }

    public Commit getCommit() {
        return this.commit;
    }

    public List<SpecItem> getSpecItems() {
        return specItems;
    }
}
