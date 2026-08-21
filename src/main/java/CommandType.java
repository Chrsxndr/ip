/**
 * Represents the commands understood by Clarry.
 */
public enum CommandType {
    BYE, LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, UNKNOWN;

    /**
     * Converts the first word of a user command to its command type.
     *
     * @param word first word entered by the user
     * @return corresponding command type, or {@code UNKNOWN} if it is unsupported
     */
    public static CommandType fromWord(String word) {
        switch (word) {
        case "bye":
            return BYE;
        case "list":
            return LIST;
        case "mark":
            return MARK;
        case "unmark":
            return UNMARK;
        case "delete":
            return DELETE;
        case "todo":
            return TODO;
        case "deadline":
            return DEADLINE;
        case "event":
            return EVENT;
        default:
            return UNKNOWN;
        }
    }
}
