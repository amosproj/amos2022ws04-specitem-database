package amos.specitemdatabase.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum Category {

    // TODO: find out what are the values
    CONSTRAINT_ITEM("CONSTRAINT_ITEM"),
    UNCONSTRAINED_ITEM("UNCONSTRAINED_ITEM");

    private final String name;

    private static final Map<String, Category> STRING_TO_CATEGORY;

    Category (String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    static {
        Map<String, Category> map = new HashMap<>();
        for (Category instance : Category.values()) {
            map.put(instance.getName().toLowerCase(),instance);
        }
        STRING_TO_CATEGORY = Collections.unmodifiableMap(map);
    }

    public static Category get(String name) {
        return STRING_TO_CATEGORY.get(name.toLowerCase());
    }
}
