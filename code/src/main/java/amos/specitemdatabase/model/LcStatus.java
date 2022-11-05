package amos.specitemdatabase.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum LcStatus {

    // TODO: find out what are the values
    CONSTRAINT_ITEM("CONSTRAINT_ITEM"),
    UNCONSTRAINED_ITEM("UNCONSTRAINED_ITEM");

    private final String name;

    private static final Map<String, LcStatus> STRING_TO_LC_STATUS;

    LcStatus (String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    static {
        Map<String, LcStatus> map = new HashMap<>();
        for (LcStatus instance : LcStatus.values()) {
            map.put(instance.getName().toLowerCase(),instance);
        }
        STRING_TO_LC_STATUS = Collections.unmodifiableMap(map);
    }

    public static LcStatus get(String name) {
        return STRING_TO_LC_STATUS.get(name.toLowerCase());
    }

}
