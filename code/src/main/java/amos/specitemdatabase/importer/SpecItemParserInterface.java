package amos.specitemdatabase.importer;

import amos.specitemdatabase.model.ProcessedDocument;

import java.io.File;
import java.io.IOException;

public interface SpecItemParserInterface {
    ProcessedDocument processFile(File file) throws IOException;
}
