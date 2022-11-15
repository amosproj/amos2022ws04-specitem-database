package amos.specitemdatabase.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A set of constants to facilitate the work with text files.
 */
public class SpecItemConstants {

    public static final String FINGERPRINT = "Fingerprint";
    public static final String SHORT_NAME = "ShortName";
    public static final String CATEGORY = "Category";
    public static final String LC_STATUS = "LC-Status";
    public static final String USE_INSTEAD = "UseInstead";
    public static final String TRACE_REFS = "TraceRefs";
    public static final String LONG_NAME = "LongName";
    public static final String CONTENT = "Content";

    public static final List<String> SPEC_ITEM_UPDATEABLE_ATTRIBUTES
        = List.of(CATEGORY, LC_STATUS, USE_INSTEAD, TRACE_REFS, LONG_NAME, CONTENT);

    public static final int GENERATED_LONG_NAME_MIN_LEN = 3;
    public static final int GENERATED_LONG_NAME_MAX_LEN = 25;

    private SpecItemConstants() {

    }



}
