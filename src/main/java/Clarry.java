import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Provides the command-line interface for the Clarry task manager.
 */
public class Clarry {
    private static final Path DATA_FILE = Paths.get("data", "clarry.txt");
    private final Ui ui;
    private final Storage storage;

    /** Creates Clarry with its console user interface. */
    public Clarry() {
        ui = new Ui();
        storage = new Storage(DATA_FILE);
    }

    /** Starts Clarry's command loop. */
    public void run() {
        ui.showWelcome();
        TaskList tasks = loadTasks();

        commandLoop:
        while (true) {
            String input = ui.readCommand();

            try {
                CommandType commandType = CommandType.fromWord(getCommandWord(input));
                switch (commandType) {
                case BYE:
                    if (!input.equals("bye")) {
                        throwUnknownCommand();
                    }
                    ui.showGoodbye();
                    break commandLoop;
                case LIST:
                    if (!input.equals("list")) {
                        throwUnknownCommand();
                    }
                    ui.showList(tasks.getTasks());
                    break;
                case ON:
                    LocalDate date = parseDate(input, "on");
                    ui.showTasksOnDate(date, tasks.getTasksOnDate(date));
                    break;
                case MARK:
                    int markIndex = parseIndex(input, "mark", tasks.size());
                    tasks.get(markIndex).markAsDone();
                    saveTasks(tasks);
                    ui.showMarked(tasks.get(markIndex));
                    break;
                case UNMARK:
                    int unmarkIndex = parseIndex(input, "unmark", tasks.size());
                    tasks.get(unmarkIndex).markAsNotDone();
                    saveTasks(tasks);
                    ui.showUnmarked(tasks.get(unmarkIndex));
                    break;
                case DELETE:
                    int deleteIndex = parseIndex(input, "delete", tasks.size());
                    Task deletedTask = tasks.delete(deleteIndex);
                    saveTasks(tasks);
                    ui.showDeleted(deletedTask, tasks.size());
                    break;
                case TODO:
                    String description = input.length() > 4 ? input.substring(5).trim() : "";
                    if (description.isEmpty()) {
                        throw new ClarryException("OOPS!!! The description of a todo cannot be empty.");
                    }
                    Task todoTask = new Todo(description);
                    tasks.add(todoTask);
                    saveTasks(tasks);
                    ui.showAdded(todoTask, tasks.size());
                    break;
                case DEADLINE:
                    String deadlineDetails = input.length() > 8 ? input.substring(9).trim() : "";
                    if (deadlineDetails.isEmpty()) {
                        throw new ClarryException("OOPS!!! The description of a deadline cannot be empty.");
                    }
                    if (!deadlineDetails.contains(" /by ")) {
                        throw new ClarryException(
                                "OOPS!!! A deadline needs a '/by' date, e.g. deadline return book /by 2019-10-15");
                    }
                    String[] deadlineParts = deadlineDetails.split(" /by ", 2);
                    if (deadlineParts.length != 2 || deadlineParts[0].trim().isEmpty()
                            || deadlineParts[1].trim().isEmpty()) {
                        throw new ClarryException("OOPS!!! A deadline needs both a description and a '/by' date.");
                    }
                    Task deadlineTask = new Deadline(deadlineParts[0].trim(), deadlineParts[1].trim());
                    tasks.add(deadlineTask);
                    saveTasks(tasks);
                    ui.showAdded(deadlineTask, tasks.size());
                    break;
                case EVENT:
                    String eventDetails = input.length() > 5 ? input.substring(6).trim() : "";
                    String[] fromSplit = eventDetails.split(" /from ", 2);
                    String[] toSplit = fromSplit.length == 2 ? fromSplit[1].split(" /to ", 2) : new String[0];
                    if (fromSplit.length != 2 || toSplit.length != 2 || fromSplit[0].trim().isEmpty()
                            || toSplit[0].trim().isEmpty() || toSplit[1].trim().isEmpty()) {
                        throw new ClarryException("OOPS!!! An event needs a description, '/from', and '/to' date and time.");
                    }
                    Task eventTask = new Event(fromSplit[0].trim(), toSplit[0].trim(), toSplit[1].trim());
                    tasks.add(eventTask);
                    saveTasks(tasks);
                    ui.showAdded(eventTask, tasks.size());
                    break;
                case UNKNOWN:
                    throwUnknownCommand();
                    break;
                }
            } catch (ClarryException e) {
                ui.showError(e.getMessage());
            } catch (NumberFormatException e) {
                ui.showError("OOPS!!! Please provide a valid task number.");
            }
        }

    }

    /** Starts Clarry from the command line. */
    public static void main(String[] args) {
        new Clarry().run();
    }

    /**
     * Saves all tasks to Clarry's relative data file.
     *
     * @param tasks tasks to save
     */
    private void saveTasks(TaskList tasks) {
        try {
            storage.save(tasks);
        } catch (IOException e) {
            ui.showSaveError();
        }
    }

    /**
     * Loads the saved tasks, skipping any corrupted save-file lines.
     *
     * @return tasks reconstructed from the save file, or an empty list if it is absent
     */
    private TaskList loadTasks() {
        try {
            Storage.LoadResult loadResult = storage.load();
            for (int i = 0; i < loadResult.getCorruptedLineCount(); i++) {
                ui.showCorruptedLineError();
            }
            return new TaskList(loadResult.getTasks());
        } catch (IOException e) {
            ui.showLoadError();
            return new TaskList();
        }
    }

    /**
     * Extracts the first space-separated word from a command.
     *
     * @param input complete user command
     * @return first command word
     */
    private static String getCommandWord(String input) {
        int firstSpace = input.indexOf(' ');
        return firstSpace == -1 ? input : input.substring(0, firstSpace);
    }

    /**
     * Throws Clarry's standard error for an unsupported command.
     *
     * @throws ClarryException always
     */
    private static void throwUnknownCommand() throws ClarryException {
        throw new ClarryException("OOPS!!! I'm sorry, but I don't know what that means :-(");
    }

    /**
     * Converts a task number in a task-index command to a zero-based index.
     *
     * @param input complete user command
     * @param command command name
     * @param taskCount number of stored tasks
     * @return zero-based task index
     * @throws ClarryException if no valid existing task number is supplied
     */
    private static int parseIndex(String input, String command, int taskCount) throws ClarryException {
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
     * Parses the ISO date supplied to a date-filter command.
     *
     * @param input complete command input
     * @param command date-filter command name
     * @return parsed date
     * @throws ClarryException if the command does not contain one valid ISO date
     */
    private static LocalDate parseDate(String input, String command) throws ClarryException {
        String dateText = input.length() > command.length() ? input.substring(command.length()).trim() : "";
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException e) {
            throw new ClarryException("OOPS!!! Please use yyyy-mm-dd for the date, e.g. on 2019-10-15.");
        }
    }
}
