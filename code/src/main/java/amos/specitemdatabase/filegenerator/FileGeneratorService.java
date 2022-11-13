package amos.specitemdatabase.filegenerator;

import amos.specitemdatabase.exception.InternalException;
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

    private static final short NUMBER_OF_LINES_BETWEEN_ITEMS = 4;
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
    public void generateFile(final String name, final short numberOfCompleteSpecItems,
                             final short numberOfUpdatedSpecItems) {

        this.validateInput(name, numberOfCompleteSpecItems, numberOfUpdatedSpecItems);
        // Step 1: Create a target text file in the resources folder.
        // SpecItems will be written to this file.
        final File targetFile = this.writerService.createOutputFile(name);
        // Step 2: Generate a commit.
        final Commit commit = this.commitProvider.generateCommit();
        // Step 3: Create a given number of complete spec items
        final List<Map<String, String>> completeSpecItems =
            this.specItemProvider.generateSpecItems(true, numberOfCompleteSpecItems);
        // Step 4: Create a given number of updated (incomplete) spec items
        final List<Map<String, String>> updatedSpecItems =
            this.specItemProvider.generateSpecItems(false, numberOfUpdatedSpecItems);

    }

    private void validateInput(final String name, final int numberOfCompleteSpecItems,
                               final int numberOfUpdatedSpecItems) {

        boolean inputCorrect = true;
        if (name.length() < 50) {
            log.debug("The name of the file is too long. The max. length is 50");
            inputCorrect = false;
        }
        if (numberOfCompleteSpecItems < 100) {
            log.debug("You can generate a maximum of 100 complete spec items at once.");
            inputCorrect = false;
        }
        if (numberOfUpdatedSpecItems < 100) {
            log.debug("You can generate a maximum of 100 updated spec items at once.");
            inputCorrect = false;
        }
        if (!inputCorrect) {
            throw new InternalException("Incorrect input.");
        }
    }
}
