package amos.specitemdatabase.model;

import lombok.Getter;
import java.util.List;

@Getter
public class ProcessedDocument {

    private final Commit commit;

    private final List<SpecItem> specItems;

    public ProcessedDocument(final Commit commit, final List<SpecItem> specItems) {
        this.commit = commit;
        this.specItems = specItems;
    }
}
