package amos.specitemdatabase.importer;

import static amos.specitemdatabase.model.Commit.getCommitFromString;

import amos.specitemdatabase.model.Commit;
import amos.specitemdatabase.model.ProcessedDocument;
import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.model.SpecItemBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SpecItemParser implements SpecItemParserInterface{

    /**
     * Splits a text file into String pieces and then transforms them into a ProcessedDocument.
     *
     * @param  textFile   the text file that will be split
     * @return         ProcessedDocument
     */
    @Override
    public ProcessedDocument processFile(File textFile) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(textFile));
        List<String> specItemsList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        String currentLine;
        String delimiter = System.lineSeparator();

        while ((currentLine = br.readLine()) != null){
            if (currentLine.contains("Fingerprint:")) {
                stringBuilder.delete(stringBuilder.length() - 5 * delimiter.length(), stringBuilder.length());
                delimiter = System.lineSeparator();
                specItemsList.add(stringBuilder.toString());
                stringBuilder.delete(0, stringBuilder.length());
            }

            if (currentLine.contains("Content:") || currentLine.contains("CommitMsg:")) {
                delimiter = "--||--";
            }
            else if (currentLine.contains("CommitAuthor:")) {
                stringBuilder.delete(stringBuilder.length() - delimiter.length(), stringBuilder.length());
                delimiter = System.lineSeparator();
                stringBuilder.append(delimiter);
            }

            stringBuilder.append(currentLine);
            stringBuilder.append(delimiter);
        }
        //appends last item of the file to the list
        stringBuilder.delete(stringBuilder.length() - 4 * delimiter.length(), stringBuilder.length());
        specItemsList.add(stringBuilder.toString());

        Commit commit = getCommitFromString(specItemsList.get(0));
        specItemsList.remove(0);

        return new ProcessedDocument(commit, getSpecItemsFromString(specItemsList, commit));
    }

    private List<SpecItem> getSpecItemsFromString(List<String> specItemsList, Commit commit){
        final String regex = "(Fingerprint: (?<Fingerprint>\\w+)\\R)(ShortName: (?<ShortName>\\w+)\\R)(Category: (?<Category>\\w*)\\R)(LC-Status: (?<LCStatus>\\w*)\\R)(UseInstead: (?<UseInstead>[\\S ]*)\\R)(TraceRefs: (?<TraceRefs>[\\S ]*)\\R)(LongName: (?<LongName>[\\S ]*)\\R)(Content: (?<Content>[\\S ]+))";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher;
        SpecItemBuilder specItemBuilder = new SpecItemBuilder();
        specItemBuilder.setCommit(commit);
        List<SpecItem> specItems = new ArrayList<>();

        for (String item: specItemsList) {
            matcher = pattern.matcher(item);
            matcher.find();

            specItemBuilder.fromStringRepresentation(
                    matcher.group("ShortName"),
                    matcher.group("Category"),
                    matcher.group("LCStatus"),
                    matcher.group("LongName"),
                    matcher.group("Content")
            );
            specItemBuilder.setTraceRefs(matcher.group("TraceRefs"));
            specItems.add(new SpecItem(specItemBuilder));
        }

        return specItems;
    }

    public static String restoreWholeText(String content) {
        StringBuilder sb = new StringBuilder();
        if (!content.isEmpty()) {
            List<String> splitContent = Arrays.stream(content.split("(--\\|\\|--)")).filter(x -> !x.isEmpty()).collect(Collectors.toList());

            for (String part : splitContent) {
                sb.append(part);
                if (splitContent.indexOf(part) != splitContent.size() - 1) {
                    sb.append(System.lineSeparator());
                }
            }
        }

        return sb.toString();
    }
}
