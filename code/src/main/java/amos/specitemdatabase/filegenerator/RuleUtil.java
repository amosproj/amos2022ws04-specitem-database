//package amos.specitemdatabase.filegenerator;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.io.File;
//import java.io.FilenameFilter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Objects;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.tomcat.util.digester.Rule;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@Slf4j
//public class RuleUtil {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(RuleUtil.class);
//    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
//    public static final String NO_VALUE = "undefined";
//    public static final String RULES_FOLDER_RELATIVE = "./src/main/resources/rules";
//
//    protected RuleUtil(final String rulesFolderPath) {}
//
//    /**
//     * Finds what json files are available in resources/rules folder
//     * and converts them to a POJO.
//     * @return a list of rules
//     */
//    public FileGenerationRule getAllAvailableRules() {
//        final List<String> jsonFiles = getJsonFilesWithRules();
//        if (jsonFiles.size() > 1) {
//            log.debug("There is more than one rule. Only one will be considered: {}");
//        }
//        List<Rule> rules = new ArrayList<>();
//
//        jsonFiles.forEach(file -> rules.add(RuleUtils.getRulesFromJson(file)));
//        log.info("Created rules: {}", rules.size());
//        return rules;
//    }
//
//    private List<String> getJsonFilesWithRules() {
//        Path rulesFolderRelative = Paths.get(RULES_FOLDER_RELATIVE);
//        Path pathToRulesFolder = Paths.get(String.valueOf(
//            rulesFolderRelative.normalize().toAbsolutePath()));
//        return new ArrayList<>(Arrays.asList(Objects.requireNonNull(pathToRulesFolder.toFile().list(jsonFilter))));
//    }
//    private static final FilenameFilter jsonFilter =
//        (final File dir, final String name) -> name.toLowerCase().endsWith(".json");
//
//    private FileGenerationRule getRulesFromJson(final String file) throws Exception {
//
//        FileGenerationRule rule;
//        InputStream inputStream = RuleUtil.class.getClassLoader().getResourceAsStream(+file);
//        try {
//            rule = OBJECT_MAPPER.readValue(inputStream, FileGenerationRule.class);
//        } catch (IOException e) {
//            log.error("Error reading the file {}. The error message is {}", file, e.getMessage());
//            throw new Exception("No rule has been found.");
//        }
//        return rule;
//    }
//}
