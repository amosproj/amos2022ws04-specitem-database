package amos.specitemdatabase.filegenerator;

import org.springframework.stereotype.Component;

@Component
public class ContentCreatorImpl implements ContentCreator {

    private static final short CONTENT_MAX_LINE_LEN = 120;
    private static final char[] CONTENT_DELIMITERS = {};

    @Override
    public String createContent() {
        return null;
    }

    @Override
    public String get() {
        return this.createContent();
    }
}
