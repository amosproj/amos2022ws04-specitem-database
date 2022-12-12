package amos.specitemdatabase.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompareResult {
    private String field;
    private String old;
    private String updated;

    public CompareResult(String field, String old, String updated) {
        this.field = field;
        this.old = old;
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "CompareResult{" +
                "field='" + field + '\'' +
                ", old='" + old + '\'' +
                ", updated='" + updated + '\'' +
                '}';
    }
}
