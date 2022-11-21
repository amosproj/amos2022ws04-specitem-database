package amos.specitemdatabase.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Represents a SpecItem together with the corresponding commit information.
 */
@Getter
@Setter
@Entity
public class SpecItem {

    private String fingerprint;
    @Id
    private String shortName;
    @Enumerated(EnumType.ORDINAL)
    private Category category;
    @Enumerated(EnumType.ORDINAL)
    private LcStatus lcStatus;
    private String useInstead;
    @ElementCollection
    private List<String> traceRefs;
    @Column(columnDefinition="TEXT")
    private String longName;
    @Column(columnDefinition="TEXT")
    private String content;
    @ManyToOne
    private Commit commit;

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

    public SpecItem() {

    }
}
