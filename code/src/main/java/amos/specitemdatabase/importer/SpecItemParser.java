package amos.specitemdatabase.importer;
import amos.specitemdatabase.model.Commit;
import amos.specitemdatabase.model.SpecItem;
import amos.specitemdatabase.model.SpecItemBuilder;
import amos.specitemdatabase.model.SpecItemEntity;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpecItemParser implements SpecItemParserInterface{

    /**
     * Splits a text file into String pieces and then transform them into SpecItems.
     *
     * @param  textFile   the text file that will be split
     * @return         list of SpecItems
     */
    @Override
    public List<SpecItem> fileToSpecItems(File textFile) throws IOException {

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

            if (currentLine.contains("Content:")) {
                delimiter = "--||--";
            }

            stringBuilder.append(currentLine);
            stringBuilder.append(delimiter);
        }
        //appends last item of the file to the list
        stringBuilder.delete(stringBuilder.length() - 4 * delimiter.length(), stringBuilder.length());
        specItemsList.add(stringBuilder.toString());

        Commit commit = getCommitFromString(specItemsList.get(0));
        specItemsList.remove(0);

        return getSpecItemsFromString(specItemsList, commit);
    }

    private Commit getCommitFromString(String commitText) {
        final String regex = "(CommitHash: (?<CommitHash>\\S+)\\r\\n)(CommitDate: (?<CommitDate>\\S+)\\r\\n)(CommitMsg: (?<CommitMsg>[\\S\\s]+)\\r\\n)(CommitAuthor: (?<CommitAuthor>\\S+))";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(commitText);
        matcher.find();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        return new Commit(
                matcher.group("CommitHash"),
                matcher.group("CommitMsg"),
                LocalDate.parse(matcher.group("CommitDate"),formatter),
                matcher.group("CommitAuthor"));
    }

    private List<SpecItem> getSpecItemsFromString(List<String> specItemsList, Commit commit){
        final String regex = "(Fingerprint: (?<Fingerprint>\\w+)\\r\\n)(ShortName: (?<ShortName>\\w+)\\r\\n)(Category:  (?<Category>\\w+)\\r\\n)(LC-Status: (?<LCStatus>\\w+)\\r\\n)(UseInstead:(?<UseInstead>\\w*)\\r\\n)(TraceRefs: (?<TraceRefs>[\\S ]+)\\r\\n)(LongName:  (?<LongName>[\\S ]+)\\r\\n)(Content:(?<Content>[\\S ]+))";
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

            specItems.add(new SpecItem(specItemBuilder));
        }

        return specItems;
    }

    @Override
    public SpecItemEntity transformSpecItem(SpecItem specItem) {
        return null;
    }
}
