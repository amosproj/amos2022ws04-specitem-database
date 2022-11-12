package amos.specitemdatabase.filegenerator;

import amos.specitemdatabase.model.Commit;
import java.io.File;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for generating text file. See {@link FileGenerator} for more details.
 */
@Slf4j
@Service
public class FileGeneratorService implements FileGenerator {

    private final WriterService writerService;
    private final SpecItemProvider specItemProvider;
    private final CommitProvider commitProvider;

    @Autowired
    public FileGeneratorService(final WriterService writerService, final SpecItemProvider specItemProvider,
                                final CommitProvider commitProvider) {
        this.writerService = writerService;
        this.specItemProvider = specItemProvider;
        this.commitProvider = commitProvider;
    }

    @Override
    public void generateFile(final String name, final short numberOfCompleteSpecItems, final short numberOfUpdatedSpecItems,
                             final short contentNewLines, final short contentMaxLineLen, final char[] contentDelimiters) {

        validateInput(name, numberOfCompleteSpecItems, numberOfUpdatedSpecItems, contentNewLines,
            contentMaxLineLen, contentDelimiters);

        // Step 1: Create a target text file in the resources folder.
        // SpecItems will be written to this file.
        final File targetFile = this.writerService.createOutputFile(name);
        // Step 2: Create a given number of complete spec items
        final List<Map<String, String>> completeSpecItems =
            this.specItemProvider.generateSpecItems(true, numberOfCompleteSpecItems);
        // Step 3: Create a given number of updated (incomplete) spec items
        final List<Map<String, String>> updatedSpecItems =
            this.specItemProvider.generateSpecItems(false, numberOfUpdatedSpecItems);
        final Commit commit = this.commitProvider.generateCommit();




    }

    private void validateInput(final String name, final int numberOfCompleteSpecItems,
                               final int numberOfUpdatedSpecItems, final int contentNewLines,
                               final int maxContentLineLen,
                               final char[] contentDelimiters) {

        log.debug("Validating the provided input. See the docs for the range of allowed values.");
        assert name.length() < 50;
        assert numberOfCompleteSpecItems <= 100;
        assert numberOfUpdatedSpecItems <= 100;
        assert contentNewLines < 30;
        assert maxContentLineLen < 120;
        if (!String.valueOf(contentDelimiters).matches("^[\\t\\n\\r\\f\\v!@#$%^&*]+$")) {
            String errorMessage = "The list of allowed delimiters is " + "^[\\t\\n\\r\\f\\v!@#$%^&*]+$";
            throw new AssertionError(errorMessage);
        }
    }


}
