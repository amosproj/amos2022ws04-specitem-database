package amos.specitemdatabase.model;

import java.util.List;

public class ProcessedDocument {

    private final Commit commit;
    private final List<String> specItems; // or List<Map<String, String>

    public ProcessedDocument(final Commit commit, final List<String> specItems) {
        this.commit = commit;
        this.specItems = specItems;
    }
}
