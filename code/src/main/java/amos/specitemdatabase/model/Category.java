package amos.specitemdatabase.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum Category {

    SPECIFICATION_ITEM("SPECIFICATION_ITEM"), // Unstable
    CATEGORY1("Category1"),
    CATEGORY2("Category2"),
    CATEGORY3("Category3"),
    CATEGORY4("Category4"),
    EMPTY("");

    private final String name;

    private static final Map<String, Category> STRING_TO_CATEGORY;

    Category (final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    static {
        Map<String, Category> map = new HashMap<>();
        for (final Category instance : Category.values()) {
            map.put(instance.getName().toLowerCase(),instance);
        }
        STRING_TO_CATEGORY = Collections.unmodifiableMap(map);
    }

    public static Category get(final String name) {
        return STRING_TO_CATEGORY.get(name.toLowerCase());
    }
}
