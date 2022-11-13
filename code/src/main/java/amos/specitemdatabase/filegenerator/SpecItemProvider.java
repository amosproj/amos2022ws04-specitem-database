package amos.specitemdatabase.filegenerator;

import java.util.List;
import java.util.Map;

public interface SpecItemProvider {


    List<Map<String, String>> generateSpecItems(boolean complete, int numberOfSpecItems);


}
