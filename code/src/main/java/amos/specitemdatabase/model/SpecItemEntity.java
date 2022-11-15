
package amos.specitemdatabase.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SpecItemEntity {
    private Long id;
    private String category;
    private String lcStatus;
    private String longName;
    private String content;
    private String commitHash;
    private int version;

    public void setId(final Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    public String getLcStatus() {
        return lcStatus;
    }

    public void setLcStatus(final String lcStatus) {
        this.lcStatus = lcStatus;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(final String longName) {
        this.longName = longName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public String getCommitHash() {
        return commitHash;
    }

    public void setCommitHash(final String commitHash) {
        this.commitHash = commitHash;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    @Id
    public Long getId() {
        return id;
    }
}
