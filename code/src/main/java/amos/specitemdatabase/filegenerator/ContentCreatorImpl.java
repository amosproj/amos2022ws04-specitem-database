package amos.specitemdatabase.filegenerator;

import com.github.javafaker.Faker;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;

@Component
public class ContentCreatorImpl implements ContentCreator {

    private static final int CONTENT_MAX_WORDS_LINE = 20;
    private static final int CONTENT_MAX_SIZE = 10;
    private static final List<String> CONTENT_DELIMITERS = List.of(
        System.lineSeparator(), "\\s+", "\\t", ",", ";", "!", " ",
        "|", "||", "-", "*", "[", "]", "{", "}", "(", ")"
    );


    private static final Random RANDOM = new Random();
    private static final Faker FAKER = new Faker();

    @Override
    public String createContent() {
        // Step 1: Randomly find the content size
        int contentSize = RANDOM.nextInt(CONTENT_MAX_SIZE) + 1;
        // Step 2: Mix words and delimiters
        final List<String> lines = new ArrayList<>();
        List<String> randomWords;
        for (int i = 0; i < contentSize; i++) {
            randomWords = FAKER.lorem().words(RANDOM.nextInt(CONTENT_MAX_WORDS_LINE) + 1);
            List<String> mixOfWordsAndDelims =  Stream.concat(randomWords.stream(), CONTENT_DELIMITERS.stream())
                .collect(Collectors.toList());
            Collections.shuffle(mixOfWordsAndDelims);
            String randomDelim = CONTENT_DELIMITERS.get(RANDOM.nextInt(CONTENT_DELIMITERS.size()));
            lines.add(String.join(randomDelim, mixOfWordsAndDelims));
        }
        return String.join(System.lineSeparator(), lines);
    }

    @Override
    public String get() {
        return this.createContent();
    }
}
