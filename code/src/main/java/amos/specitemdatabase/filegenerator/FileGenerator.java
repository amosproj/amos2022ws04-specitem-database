package amos.specitemdatabase.filegenerator;

/**
 * Generates a .txt file that represents a commit,
 * which might contain both complete and incomplete spec items.
 * The resulting file is saved in the resources folder.
 */
public interface FileGenerator {

    /**
     * Generates a file according to the specified configuration.
     *
     * @param name The name of the output file.
     * @param numberOfCompleteSpecItems The number of complete spec items, i.e.,
     *                                  items that have all attributes filled with values.
     * @param numberOfUpdatedSpecItems The number of incomplete spec items might imitate a commit
     *                                that only updates a part of the spec item, for example, its content.
     */
    void generateFile(final String name, final short numberOfCompleteSpecItems,
                      final short numberOfUpdatedSpecItems);





}
