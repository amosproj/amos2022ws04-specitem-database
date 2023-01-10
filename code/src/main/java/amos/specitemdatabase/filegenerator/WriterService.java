package amos.specitemdatabase.filegenerator;

import amos.specitemdatabase.model.Commit;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;

public interface WriterService {

    File createOutputFile(final String name);

    void writeToFile(final File file, final Commit commit, final List<LinkedHashMap<String, String>> completeSpecItems,
                     final List<LinkedHashMap<String, String>> updatedSpecItems);
}
