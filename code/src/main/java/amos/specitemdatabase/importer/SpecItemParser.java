package amos.specitemdatabase.importer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpecItemParser {

    /**
     * Splits a text file into SpecItems in a String format.
     *
     * @param  textFile   the text file that will be split
     * @return         list of SpecItems as String
     */
    public static List<String> splitFileIntoSpecItems(File textFile) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(textFile));
        List<String> specItemsList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        String currentLine;
        int lineSeparatorsNum = System.lineSeparator().toCharArray().length;

        while ((currentLine = br.readLine()) != null){
            if(currentLine.contains("Fingerprint:")) {
                stringBuilder.delete(stringBuilder.length() - 5 * lineSeparatorsNum, stringBuilder.length());
                specItemsList.add(stringBuilder.toString());
                stringBuilder.delete(0, stringBuilder.length());
            }

            stringBuilder.append(currentLine);
            stringBuilder.append(System.lineSeparator());
        }
        //appends last item of the file to the list
        stringBuilder.delete(stringBuilder.length() - 5 * lineSeparatorsNum, stringBuilder.length());
        specItemsList.add(stringBuilder.toString());

        return specItemsList;
    }
}
