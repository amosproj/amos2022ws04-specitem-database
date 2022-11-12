package amos.specitemdatabase.filegenerator;

/**
 * Generates a .txt file that represents a commit,
 * which might contain both complete and incomplete spec items.
 * The resulting file is saved in the resources folder.
 */
public interface FileGenerator {

    /**
     * Generates a file according to the specified configuration.
     * @param name The name of the output file.
     * @param numberOfCompleteSpecItems The number of complete spec items, i.e.,
     *                                  items that have all attributes filled with values.
     * @param numberOfUpdatedSpecItems The number of incomplete spec items might imitate a commit
     *                                that only updates a part of the spec item, for example, its content.
     * @param contentNewLines The content attribute might be complex and span over several lines.
     *                        This attribute provides the information about the content's size
     *                        of a spec item, i.e. how many lines it should take.
     * @param contentMaxLineLen The maximal length of a single line of the content.
     * @param contentDelimiters Special characters to be used in the content, for example: colon, semicolon,
     *                          dash, newline, etc.
     */
    void generateFile(final String name, final short numberOfCompleteSpecItems, final short numberOfUpdatedSpecItems,
                      final short contentNewLines, final short contentMaxLineLen, final char[] contentDelimiters);





}
