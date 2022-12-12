package amos.specitemdatabase.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompareResultMarkup {
    private String field;
    private String markupText;

    public CompareResultMarkup(String field, String markupText) {
        this.field = field;
        this.markupText = markupText;
    }
}
