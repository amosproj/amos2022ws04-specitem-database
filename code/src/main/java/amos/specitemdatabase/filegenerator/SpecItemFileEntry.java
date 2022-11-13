package amos.specitemdatabase.filegenerator;

import amos.specitemdatabase.model.Category;
import amos.specitemdatabase.model.LcStatus;
import java.util.List;
import lombok.Data;

public class SpecItemFileEntry {

    private final String fingerprint;
    private final String shortName;
    private final Category category;
    private final LcStatus lcStatus;
    private final String useInstead;
    private final List<String> traceRefs;
    private final String longName;
    private final String content;

    public SpecItemFileEntry(final String fingerprint, final String shortName, final Category category,
                             final LcStatus lcStatus, final String useInstead,
                             final List<String> traceRefs, final String longName, final String content) {
        this.fingerprint = fingerprint;
        this.shortName = shortName;
        this.category = category;
        this.lcStatus = lcStatus;
        this.useInstead = useInstead;
        this.traceRefs = traceRefs;
        this.longName = longName;
        this.content = content;
    }
}
