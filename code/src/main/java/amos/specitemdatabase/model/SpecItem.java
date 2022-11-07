package amos.specitemdatabase.model;

import java.util.List;
import lombok.Getter;

/**
 * Represents a SpecItem together with the corresponding commit information.
 */
@Getter
public class SpecItem {

    private final String shortName;
    private final Category category;
    private final LcStatus lcStatus;
    // private final Object useInstead; TODO: Ask the client about this field
    // because it is always empty in the example data
    private final List<String> traceRefs;
    private final String longName;
    private final String content;
    private final Commit commit;

    private short version;

    // TODO: ask the client about the tags; where do they come from? shall they be included in the .txt file?
    public SpecItem(final SpecItemBuilder specItemBuilder) {
        this.shortName = specItemBuilder.getShortName();
        this.category = specItemBuilder.getCategory();
        this.lcStatus = specItemBuilder.getLcStatus();
        this.traceRefs = specItemBuilder.getTraceRefs();
        this.longName = specItemBuilder.getLongName();
        this.content = specItemBuilder.getContent();
        this.commit = specItemBuilder.getCommit();
    }

}
