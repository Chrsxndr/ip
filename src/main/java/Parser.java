import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Parses and validates commands entered by the user.
 */
public class Parser {
    /**
     * Returns the type of the command entered by the user.
     *
     * @param input complete command input
     * @return recognized command type, or {@code UNKNOWN}
     */
    public CommandType parseCommandType(String input) {
        int firstSpace = input.indexOf(' ');
        String commandWord = firstSpace == -1 ? input : input.substring(0, firstSpace);
        return CommandType.fromWord(commandWord);
    }

    /**
     * Ensures that a command has no arguments or extra text.
     *
     * @param input complete command input
     * @param command expected command word
     * @throws ClarryException if the input is not exactly the command word
     */
    public void requireExactCommand(String input, String command) throws ClarryException {
        if (!input.equals(command)) {
            throwUnknownCommand();
        }
    }

    /**
     * Parses a todo task from a command input.
     *
     * @param input complete todo command
     * @return parsed todo task
     * @throws ClarryException if the description is empty
     */
    public Todo parseTodo(String input) throws ClarryException {
        String description = input.length() > 4 ? input.substring(5).trim() : "";
        if (description.isEmpty()) {
            throw new ClarryException("OOPS!!! The description of a todo cannot be empty.");
        }
        return new Todo(description);
    }

    /**
     * Parses a deadline task from a command input.
     *
     * @param input complete deadline command
     * @return parsed deadline task
     * @throws ClarryException if the command is incomplete or the date is invalid
     */
    public Deadline parseDeadline(String input) throws ClarryException {
        String details = input.length() > 8 ? input.substring(9).trim() : "";
        if (details.isEmpty()) {
            throw new ClarryException("OOPS!!! The description of a deadline cannot be empty.");
        }
        if (!details.contains(" /by ")) {
            throw new ClarryException(
                    "OOPS!!! A deadline needs a '/by' date, e.g. deadline return book /by 2019-10-15");
        }
        String[] parts = details.split(" /by ", 2);
        if (parts.length != 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            throw new ClarryException("OOPS!!! A deadline needs both a description and a '/by' date.");
        }
        return new Deadline(parts[0].trim(), parts[1].trim());
    }

    /**
     * Parses an event task from a command input.
     *
     * @param input complete event command
     * @return parsed event task
     * @throws ClarryException if the command is incomplete or either date and time is invalid
     */
    public Event parseEvent(String input) throws ClarryException {
        String details = input.length() > 5 ? input.substring(6).trim() : "";
        String[] fromSplit = details.split(" /from ", 2);
        String[] toSplit = fromSplit.length == 2 ? fromSplit[1].split(" /to ", 2) : new String[0];
        if (fromSplit.length != 2 || toSplit.length != 2 || fromSplit[0].trim().isEmpty()
                || toSplit[0].trim().isEmpty() || toSplit[1].trim().isEmpty()) {
            throw new ClarryException("OOPS!!! An event needs a description, '/from', and '/to' date and time.");
        }
        return new Event(fromSplit[0].trim(), toSplit[0].trim(), toSplit[1].trim());
    }

    /**
     * Parses a task number as a zero-based index.
     *
     * @param input complete command input
     * @param command command word
     * @param taskCount number of available tasks
     * @return zero-based task index
     * @throws ClarryException if no valid existing task number is supplied
     */
    public int parseIndex(String input, String command, int taskCount) throws ClarryException {
        String numberPart = input.length() > command.length() ? input.substring(command.length()).trim() : "";
        if (numberPart.isEmpty()) {
            throw new ClarryException("OOPS!!! Please specify which task number to " + command + ".");
        }
        int index = Integer.parseInt(numberPart) - 1;
        if (index < 0 || index >= taskCount) {
            throw new ClarryException("OOPS!!! That task number doesn't exist.");
        }
        return index;
    }

    /**
     * Parses the ISO date supplied to the {@code on} command.
     *
     * @param input complete on command
     * @return parsed date
     * @throws ClarryException if the command does not contain one valid ISO date
     */
    public LocalDate parseDate(String input) throws ClarryException {
        String dateText = input.length() > 2 ? input.substring(2).trim() : "";
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException e) {
            throw new ClarryException("OOPS!!! Please use yyyy-mm-dd for the date, e.g. on 2019-10-15.");
        }
    }

    /** Throws Clarry's standard error for an unsupported command. */
    public void throwUnknownCommand() throws ClarryException {
        throw new ClarryException("OOPS!!! I'm sorry, but I don't know what that means :-(");
    }
}
