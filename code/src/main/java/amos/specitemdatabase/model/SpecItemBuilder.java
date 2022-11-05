package amos.specitemdatabase.model;

import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class SpecItemBuilder {

    private String shortName;
    private Category category;
    private LcStatus lcStatus;
    // TODO: use instead attribute
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
        this.content = content;
        return this;
    }

    public SpecItemBuilder fromMap(final Map<String, String> fieldToValue) {
        this.shortName = fieldToValue.get("ABC");
        return this;
    }

    public SpecItemBuilder setTraceRefs(final String traceRefs) {
        if (!traceRefs.isEmpty()) {
            this.traceRefs = List.of(traceRefs.split(" *, *"));
        }
        return this;
    }

    public SpecItemBuilder setCommit(final String commit) {
        // TODO: set a commit
        return this;
    }




}
