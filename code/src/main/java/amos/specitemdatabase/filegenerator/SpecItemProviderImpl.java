package amos.specitemdatabase.filegenerator;

import static amos.specitemdatabase.utils.SpecItemConstants.CATEGORY;
import static amos.specitemdatabase.utils.SpecItemConstants.CONTENT;
import static amos.specitemdatabase.utils.SpecItemConstants.FINGERPRINT;
import static amos.specitemdatabase.utils.SpecItemConstants.GENERATED_LONG_NAME_MAX_LEN;
import static amos.specitemdatabase.utils.SpecItemConstants.GENERATED_LONG_NAME_MIN_LEN;
import static amos.specitemdatabase.utils.SpecItemConstants.LC_STATUS;
import static amos.specitemdatabase.utils.SpecItemConstants.LONG_NAME;
import static amos.specitemdatabase.utils.SpecItemConstants.SHORT_NAME;
import static amos.specitemdatabase.utils.SpecItemConstants.SPEC_ITEM_UPDATEABLE_ATTRIBUTES;
import static amos.specitemdatabase.utils.SpecItemConstants.TRACE_REFS;
import static amos.specitemdatabase.utils.SpecItemConstants.USE_INSTEAD;

import amos.specitemdatabase.model.Category;
import amos.specitemdatabase.model.LcStatus;
import amos.specitemdatabase.utils.Utils;
import com.github.javafaker.Faker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpecItemProviderImpl implements SpecItemProvider {

    private static final String SPEC_ITEM_PREFIX = "SpecItem_";
    private static final Utils.RandomEnumOfType<Category> CATEGORY_DRAWER =
        new Utils.RandomEnumOfType<>(Category.class);

    private static final Utils.RandomEnumOfType<LcStatus> LC_STATUS_DRAWER =
        new Utils.RandomEnumOfType<>(LcStatus.class);

    private static final Random RANDOM = new Random();
    private static final Faker FAKER = new Faker();

    private final Map<String, Supplier<String>> attributeToGenerationLogic = new HashMap<>();
    private final ContentCreator contentCreator;

    @PostConstruct
    public void initSuppliers() {
        attributeToGenerationLogic.put(FINGERPRINT, this::generateFingerprint);
        attributeToGenerationLogic.put(SHORT_NAME, this::generateShortNamePrefix);
        attributeToGenerationLogic.put(CATEGORY, this::generateCategory);
        attributeToGenerationLogic.put(LC_STATUS, this::generateLcStatus);
        attributeToGenerationLogic.put(USE_INSTEAD, this::generateUseInstead);
        attributeToGenerationLogic.put(TRACE_REFS, this::generateTraceRefs);
        attributeToGenerationLogic.put(LONG_NAME, this::generateLongName);
        attributeToGenerationLogic.put(CONTENT, this.contentCreator::createContent);
    }

    @Autowired
    public SpecItemProviderImpl(final ContentCreator contentCreator) {
        this.contentCreator = contentCreator;
    }

    @Override
    public List<Map<String, String>> generateSpecItems(final boolean complete, final int numberOfSpecItems) {
        List<Map<String, String>> generatedSpecItems = new ArrayList<>();
        IntStream.range(0, numberOfSpecItems).forEach(
            value -> SpecItemProviderImpl.this.generate(value, complete, generatedSpecItems));
        return generatedSpecItems;
    }

    private void generate(final int value, final boolean complete,
                          final List<Map<String, String>> generatedSpecItems) {
        if (complete) {
            generateCompleteSpecItem(value, generatedSpecItems);
        } else {
            generateUpdatedSpecItem(value, generatedSpecItems);
        }
    }

    private void generateCompleteSpecItem(final int value, final List<Map<String, String>> generatedSpecItems) {
        generatedSpecItems.add(Map.of(
            FINGERPRINT, attributeToGenerationLogic.get(FINGERPRINT).get(),
            SHORT_NAME, attributeToGenerationLogic.get(SHORT_NAME).get() + value,
            CATEGORY, attributeToGenerationLogic.get(CATEGORY).get(),
            LC_STATUS, attributeToGenerationLogic.get(LC_STATUS).get(),
            USE_INSTEAD, attributeToGenerationLogic.get(USE_INSTEAD).get(),
            TRACE_REFS, attributeToGenerationLogic.get(TRACE_REFS).get(),
            LONG_NAME, attributeToGenerationLogic.get(LONG_NAME).get(),
            CONTENT, attributeToGenerationLogic.get(CONTENT).get()
        ));
    }

    private void generateUpdatedSpecItem(final int value, final List<Map<String, String>> generatedSpecItems) {
        // Generate mandatory fields
        final String fingerprint;
        final String shortName;
        // Choose two fields at random that will be updated
        final List<Integer> twoRandomInts = Stream.generate(RANDOM::ints)
            .flatMap(IntStream::boxed)
            .distinct()
            .limit(SPEC_ITEM_UPDATEABLE_ATTRIBUTES.size()) // whatever limit you might need
            .collect(Collectors.toList());
    }

    private String generateFingerprint() {
        return FAKER.regexify("[a-z1-9]{16}");
    }

    private String generateShortNamePrefix() {
        return SPEC_ITEM_PREFIX;
    }

    private String generateCategory() {
        return CATEGORY_DRAWER.getRandomEnum().getName();
    }

    private String generateLcStatus() {
        return LC_STATUS_DRAWER.getRandomEnum().getName();
    }

    private String generateUseInstead() {
        final int numberOfUseInsteadRefs = RANDOM.nextInt(5);
        return generateRefs(numberOfUseInsteadRefs);
    }

    private String generateTraceRefs() {
        final int numberOfTraceRefs = RANDOM.nextInt(10);
        return generateRefs(numberOfTraceRefs);
    }

    private String generateRefs(int numberOfRefs) {
        final List<String> refs = new ArrayList<>();
        int refNum;
        for (int i = 0; i < numberOfRefs; i++) {
            refNum = RANDOM.nextInt(100);
            refs.add(SPEC_ITEM_PREFIX + refNum);
        }
        return String.join(",", refs);
    }

    private String generateLongName() {
        return String.join(",", FAKER.lorem().words(RANDOM.nextInt(
            GENERATED_LONG_NAME_MAX_LEN - GENERATED_LONG_NAME_MIN_LEN + 1)
            + GENERATED_LONG_NAME_MIN_LEN));
    }







}
