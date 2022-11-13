package amos.specitemdatabase.filegenerator;

import amos.specitemdatabase.exception.InternalException;
import amos.specitemdatabase.model.Commit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Component
public class WriterServiceImpl implements WriterService {

    private static final String RESULTS_FOLDER_RELATIVE = "./specitemfiles/";
    private static final int NUMBER_OF_LINES_BETWEEN_ENTRIES = 4;

    @Override
    public File createOutputFile(final String name) {
        File outputFile;
        // get the current directory
        final Path currentDir = Paths.get(RESULTS_FOLDER_RELATIVE);
        final String relativeForFile = currentDir.toString().concat("/"+name);
        final Path pathToFile = Paths.get(relativeForFile);
        try {
            // create a directory by combining and normalizing the relative path
            // doesn't throw an exception if a dir already exists
            Files.createDirectories(Paths.get(String.valueOf(currentDir.normalize().toAbsolutePath())));
            // Check if the file exists and create it if not
            if (!Files.exists(pathToFile.normalize().toAbsolutePath())) {
                outputFile = Files.createFile(pathToFile.normalize().toAbsolutePath()).toFile();
            } else {
                outputFile = Paths.get(pathToFile.normalize().toAbsolutePath().toUri()).toFile();
            }
        } catch (IOException e) {
            throw new InternalException("Couldn't create the file.");
        }
        return outputFile;
    }

    @Override
    public void writeToFile(final File file, final Commit commit,
                            final List<LinkedHashMap<String, String>> completeSpecItems,
                            final List<LinkedHashMap<String, String>> updatedSpecItems) {

        try (final BufferedWriter writer = Files.newBufferedWriter(Paths.get(file.toURI()))) {
            writer.write(commit.toString());
            writeNewLines(writer);
            writeSpecItems(writer, completeSpecItems);
            writeSpecItems(writer, updatedSpecItems);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeNewLines(final BufferedWriter writer) throws IOException {
        for (int i = 0; i < NUMBER_OF_LINES_BETWEEN_ENTRIES; i++) {
            writer.write(System.getProperty("line.separator"));
        }
    }

    public String convertMap(final LinkedHashMap<String, String> specItem) {
        final StringBuilder mapAsString = new StringBuilder();
        for (Map.Entry<String, String> entry : specItem.entrySet()) {
            mapAsString
                .append(entry.getKey())
                .append(": ")
                .append(entry.getValue())
                .append(System.getProperty("line.separator"));
        }
        //mapAsString.delete(mapAsString.length()-2, mapAsString.length()).append("}");
        return mapAsString.toString();
    }

    private void writeSpecItems(final BufferedWriter writer,
                                        final List<LinkedHashMap<String, String>> completeSpecItems) throws IOException {
        for (final LinkedHashMap<String, String> specItem: completeSpecItems) {
            writer.write(convertMap(specItem));
            writeNewLines(writer);
        }
    }
}
