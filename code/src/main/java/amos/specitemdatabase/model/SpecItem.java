package amos.specitemdatabase.model;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a SpecItem together with the corresponding commit information.
 */
@Getter
@Setter
@Entity
@IdClass(SpecItemId.class)
public class SpecItem {

    private String fingerprint;
    @Id
    private String shortName;
    @Column(columnDefinition = "TIMESTAMP")
    @Id
    private LocalDateTime commitTime;
    private LocalDateTime creationTime;
    @Column(columnDefinition = "boolean")
    private boolean markedAsDeleted;
    @Enumerated(EnumType.ORDINAL)
    private Category category;
    @Enumerated(EnumType.ORDINAL)
    private LcStatus lcStatus;
    private String useInstead;
    @ElementCollection
    private List<String> traceRefs;
    @Column(columnDefinition = "text")
    private String longName;
    @Column(columnDefinition = "text")
    private String content;
    @ManyToOne
    private Commit commit;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns( {
        @JoinColumn(name="short_name", referencedColumnName="short_name"),
        @JoinColumn(name="commit_time", referencedColumnName="commit_time")
    } )
    private TagInfo tagInfo;
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    public SpecItem(final SpecItemBuilder specItemBuilder) {
        this(specItemBuilder, specItemBuilder.getCommit().getCommitTime());
    }

    public SpecItem(final SpecItemBuilder specItemBuilder, final LocalDateTime creationTime) {
        this.fingerprint = specItemBuilder.getFingerprint();
        this.shortName = specItemBuilder.getShortName();
        this.category = specItemBuilder.getCategory();
        this.lcStatus = specItemBuilder.getLcStatus();
        this.traceRefs = specItemBuilder.getTraceRefs();
        this.useInstead = specItemBuilder.getUseInstead();
        this.longName = specItemBuilder.getLongName();
        this.content = specItemBuilder.getContent();
        this.commit = specItemBuilder.getCommit();
        this.commitTime = this.commit.getCommitTime();
        this.creationTime = creationTime;
        this.status = Status.LATEST;
        this.markedAsDeleted = false;
    }

    public SpecItem() {

    }
}
