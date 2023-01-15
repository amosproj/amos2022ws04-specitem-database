package amos.specitemdatabase.model;

import static amos.specitemdatabase.importer.SpecItemParser.restoreWholeText;

import java.util.List;
import lombok.Getter;

/**
 * This classes uses the builder pattern which is late used to construct
 * an instance of the SpecItem class.
 */
@Getter
public class SpecItemBuilder {

    private String fingerprint;
    private String shortName;
    private Category category;
    private LcStatus lcStatus;

    private String useInstead;
    private List<String> traceRefs;
    private String longName;
    private String content;

    private Commit commit;

    public SpecItemBuilder fromStringRepresentation(final String shortName, final String category,
                                                    final String lcStatus, final String longName,
                                                    final String content) {
        this.shortName = shortName;
        this.category = Category.get(category);
        this.lcStatus = LcStatus.get(lcStatus);
        this.longName = longName;
        this.content = setContent(content);
        return this;
    }

    public SpecItemBuilder setTraceRefs(final String traceRefs) {
        String editedTraceRefs = traceRefs.replace("\"", "");
        if (!editedTraceRefs.isEmpty()) {
            this.traceRefs = List.of(editedTraceRefs.split(" *, *"));
        }
        return this;
    }

    public SpecItemBuilder setCommit(final Commit commit) {
        this.commit = commit;
        return this;
    }

    private String setContent(String content) {
        return restoreWholeText(content);
    }
}
