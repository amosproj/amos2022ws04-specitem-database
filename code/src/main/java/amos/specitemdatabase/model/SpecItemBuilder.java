package amos.specitemdatabase.model;

import static amos.specitemdatabase.utils.SpecItemConstants.CATEGORY;
import static amos.specitemdatabase.utils.SpecItemConstants.CONTENT;
import static amos.specitemdatabase.utils.SpecItemConstants.LC_STATUS;
import static amos.specitemdatabase.utils.SpecItemConstants.LONG_NAME;
import static amos.specitemdatabase.utils.SpecItemConstants.SHORT_NAME;

import java.util.List;
import java.util.Map;
import lombok.Getter;

/**
 * This classes uses the builder pattern which is late used to construct
 * an instance of the SpecItem class.
 */
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

    /**
     * The builder for setting mandatory fields of a spec item
     * TODO: Ask the industry partner about the mandatory and optional fields
     * @param fieldToValue a mapping between attributes and their values
     * @return a builder with mandatory fields set
     */
    public SpecItemBuilder fromMap(final Map<String, String> fieldToValue) {
        this.shortName = fieldToValue.get(SHORT_NAME);
        this.category = Category.get(fieldToValue.get(CATEGORY));
        this.lcStatus = LcStatus.get(fieldToValue.get(LC_STATUS));
        this.longName = fieldToValue.get(LONG_NAME);
        this.content = fieldToValue.get(CONTENT);
        return this;
    }

    public SpecItemBuilder setTraceRefs(final String traceRefs) {
        if (!traceRefs.isEmpty()) {
            this.traceRefs = List.of(traceRefs.split(" *, *"));
        }
        return this;
    }

    public SpecItemBuilder setCommit(final Commit commit) {
        // TODO: set a commit
        this.commit = commit;
        return this;
    }




}
