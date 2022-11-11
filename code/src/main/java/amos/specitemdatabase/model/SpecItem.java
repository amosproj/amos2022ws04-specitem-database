package amos.specitemdatabase.model;

import java.util.List;
import lombok.Getter;

/**
 * Represents a SpecItem together with the corresponding commit information.
 */
@Getter
public class SpecItem {

    private final String fingerprint;
    private final String shortName;
    private final Category category;
    private final LcStatus lcStatus;

    private final String useInstead;
    private final List<String> traceRefs;
    private final String longName;
    private final String content;
    private final Commit commit;

    private short version;

    public SpecItem(final SpecItemBuilder specItemBuilder) {
        this.fingerprint = specItemBuilder.getFingerprint();
        this.shortName = specItemBuilder.getShortName();
        this.category = specItemBuilder.getCategory();
        this.lcStatus = specItemBuilder.getLcStatus();
        this.traceRefs = specItemBuilder.getTraceRefs();
        this.useInstead = specItemBuilder.getUseInstead();
        this.longName = specItemBuilder.getLongName();
        this.content = specItemBuilder.getContent();
        this.commit = specItemBuilder.getCommit();
    }

}
