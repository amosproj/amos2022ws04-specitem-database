package amos.specitemdatabase.filegenerator;

import amos.specitemdatabase.model.Commit;
import java.io.File;
import java.util.List;
import java.util.Map;

public interface WriterService {

    File createOutputFile(final String name);

    void writeToFile(final File file, final Commit commit, final List<Map<String, String>> completeSpecItems,
                     final List<Map<String, String>> updatedSpecItems);
}
