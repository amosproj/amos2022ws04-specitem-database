package amos.specitemdatabase.importer;

import amos.specitemdatabase.model.ProcessedDocument;
import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.model.SpecItemEntity;

import java.io.File;
import java.util.List;

public interface SpecItemParserInterface {
    ProcessedDocument fileToSpecItems(File file);
    SpecItemEntity transformSpecitem(SpecItem specItem);
}
