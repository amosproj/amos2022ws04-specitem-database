// package amos.specitemdatabase.importer;

// import amos.specitemdatabase.model.Category;
// import amos.specitemdatabase.model.LcStatus;
// import amos.specitemdatabase.model.ProcessedDocument;
// import java.io.File;
// import java.io.IOException;
// import org.junit.jupiter.api.Assertions;
// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.annotation.DirtiesContext;
// import org.springframework.util.ResourceUtils;

// @SpringBootTest
// @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
// public class SpecItemEntityParserTest {

//     @Test
//     public void testParser() throws IOException {
//         SpecItemParser specItemParser = new SpecItemParser();

//         File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "testfile.txt");
//         ProcessedDocument specItemsDoc = specItemParser.processFile(file);
//         String systemNewLine = System.lineSeparator();

//         Assertions.assertEquals("#asdf", specItemsDoc.getCommit().getCommitHash(), "CommitHash is incorrect");
//         Assertions.assertEquals("2022-02-12T21:49:13", specItemsDoc.getCommit().getCommitTime().toString(), "CommitDate is incorrect");
//         Assertions.assertEquals("bla bla prank", specItemsDoc.getCommit().getCommitMessage(), "CommitMessage is incorrect");
//         Assertions.assertEquals("Mister Wallace", specItemsDoc.getCommit().getCommitAuthor(), "CommitAuthor is incorrect");

//         Assertions.assertEquals(2, specItemsDoc.getSpecItems().size(), "SpecItems were not split correctly");
//         Assertions.assertEquals("ID1", specItemsDoc.getSpecItems().get(0).getShortName(), "ShortName is incorrect");
//         Assertions.assertEquals(Category.CATEGORY1, specItemsDoc.getSpecItems().get(0).getCategory(), "Category is incorrect");
//         Assertions.assertEquals(LcStatus.STATUS1, specItemsDoc.getSpecItems().get(0).getLcStatus(), "LC-Status is incorrect");
//         Assertions.assertEquals("bla bla bla", specItemsDoc.getSpecItems().get(0).getLongName(), "LongName is incorrect");
//         Assertions.assertEquals("fdasfasdfdskjakldsajaflsaldsafkjlds;alfjds dsahf:g" + systemNewLine +
//                 "dsalhfjakdlfkdslajf;l j,, ,,,dafkdsajf j;", specItemsDoc.getSpecItems().get(0).getContent(), "Content is incorrect");

//     }
// }
