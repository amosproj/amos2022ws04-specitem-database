package amos.specitemdatabase.filegenerator;

import java.util.function.Supplier;

/**
 * Creates the filling for the content attribute of a spec item.
 */
public interface ContentCreator extends Supplier<String> {

    /**
     * Create the value for the content attribute.
     * @return a string that represent the content of a spec item.
     */
    String createContent();
}
