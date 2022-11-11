package amos.specitemdatabase.importer;

import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.model.SpecItemEntity;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface SpecItemParserInterface {
    List<SpecItem> fileToSpecItems(File file) throws IOException;
    SpecItemEntity transformSpecItem(SpecItem specItem);
}
