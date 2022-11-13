package amos.specitemdatabase.filegenerator;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FileGeneratorServiceTest {

    @Autowired
    private FileGenerator fileGenerator;

    @Test
    public void testIfFileIsGenerated() {

        final String fileName = "generatorTestFile.txt";
        final Path path = Paths.get(fileName);
        // First, assert that the file does not exist
        Assertions.assertThat(Files.notExists(path));
        // Create a file using the file generator service
        this.fileGenerator.generateFile(fileName, 2, 3);
        // Now, check if the file was created
        Assertions.assertThat(Files.exists(path));
        //final File file = path.toFile();
        //file.delete();
    }


}
