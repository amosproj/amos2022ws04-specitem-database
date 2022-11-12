package amos.specitemdatabase.filegenerator;

import java.io.File;

public interface WriterService {

    File createOutputFile(final String name);

    void writeToFile(File file);
}
