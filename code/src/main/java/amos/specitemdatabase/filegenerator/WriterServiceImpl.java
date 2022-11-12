package amos.specitemdatabase.filegenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WriterServiceImpl implements WriterService {

    private static final String RESULTS_FOLDER_RELATIVE = "./specitemfiles";

    public void createFile(final String name) {

    }

    @Override
    public File createOutputFile(final String name) {
        File outputFile = null;
        // get the current directory
        final Path currentDir = Paths.get(RESULTS_FOLDER_RELATIVE);
        final String relativeForFile = currentDir.toString().concat(name);
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
            throw new
            log.error("Failed to create a file with name: {}", name);
        }
    }

    @Override
    public void writeToFile(final File file) {

    }
}
