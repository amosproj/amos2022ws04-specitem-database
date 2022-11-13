package amos.specitemdatabase.filegenerator;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SpecItemProviderImpl implements SpecItemProvider {

    private final ContentCreator contentCreator;

    public SpecItemProviderImpl(final ContentCreator contentCreator) {
        this.contentCreator = contentCreator;
    }

    @Override
    public List<Map<String, String>> generateSpecItems(final boolean complete, final short numberOfSpecItems) {
        return null;
    }

    private Map.Entry<String, String> generateSpecItem(final boolean complete) {
         return complete ? generateCompleteSpecItem() : generateUpdatedSpecItem();

    }

    private Map.Entry<String, String> generateUpdatedSpecItem() {
    }

    private Map.Entry<String, String> generateCompleteSpecItem() {



    }


}
