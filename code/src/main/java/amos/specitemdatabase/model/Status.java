package amos.specitemdatabase.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum Status {
    LATEST("Latest"),
    PREVIOUS("Previous"),
    DELETED("Deleted"),
    EMPTY("");

    private final String name;

    private static final Map<String, Status> STRING_TO_STATUS;

    Status (final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    static {
        final Map<String, Status> map = new HashMap<>();
        for (final Status instance : Status.values()) {
            map.put(instance.getName().toLowerCase(),instance);
        }
        STRING_TO_STATUS = Collections.unmodifiableMap(map);
    }

    public static Status get(final String name) {
        return STRING_TO_STATUS.get(name.toLowerCase());
    }
}
