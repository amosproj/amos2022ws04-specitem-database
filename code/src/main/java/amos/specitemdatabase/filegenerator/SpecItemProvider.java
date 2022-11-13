package amos.specitemdatabase.filegenerator;

import java.util.LinkedHashMap;
import java.util.List;

public interface SpecItemProvider {
    List<LinkedHashMap<String, String>> generateSpecItems(boolean complete, int numberOfSpecItems);
}
