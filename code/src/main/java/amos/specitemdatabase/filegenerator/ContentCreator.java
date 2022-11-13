package amos.specitemdatabase.filegenerator;

import java.util.function.Supplier;

public interface ContentCreator extends Supplier<String> {

    String createContent();
}
