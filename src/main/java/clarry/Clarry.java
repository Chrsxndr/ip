package clarry;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import clarry.parser.CommandType;
import clarry.parser.Parser;
import clarry.storage.Storage;
import clarry.task.Task;
import clarry.task.TaskList;
import clarry.ui.Ui;

/**
 * Provides the command-line interface for the Clarry task manager.
 */
public class Clarry {
    private static final Path DATA_FILE = Paths.get("data", "clarry.txt");
    private final Ui ui;
    private final Storage storage;
    private final Parser parser;
    private TaskList tasks;
    private boolean isExitRequested;

    /** Creates Clarry with its console user interface. */
    public Clarry() {
        ui = new Ui();
        storage = new Storage(DATA_FILE);
        parser = new Parser();
        isExitRequested = false;
    }

    /** Starts Clarry's command loop. */
    public void run() {
        ui.showWelcome();
        initialiseTasks();

        while (!isExitRequested) {
            String input = ui.readCommand();
            ui.showResponse(getResponse(input));
        }
    }

    /**
     * Executes one command and returns the response for a console or GUI frontend.
     *
     * @param input command entered by the user
     * @return response produced by Clarry
     */
    public String getResponse(String input) {
        initialiseTasks();
        try {
            return executeCommand(input);
        } catch (ClarryException e) {
            return ui.getErrorMessage(e.getMessage());
        } catch (NumberFormatException e) {
            return ui.getErrorMessage("OOPS!!! Please provide a valid task number.");
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

    /** Loads tasks once before either frontend processes its first command. */
    private void initialiseTasks() {
        if (tasks == null) {
            tasks = loadTasks();
        }
    }

    /**
     * Executes a validated command and returns its response text.
     *
     * @param input command entered by the user
     * @return successful command response
     * @throws ClarryException if the command is invalid
     */
    private String executeCommand(String input) throws ClarryException {
        assert tasks != null : "Tasks must be initialized before executing a command";
        CommandType commandType = parser.parseCommandType(input);
        switch (commandType) {
            case BYE:
                parser.requireExactCommand(input, "bye");
                isExitRequested = true;
                return ui.getGoodbyeMessage();
            case LIST:
                parser.requireExactCommand(input, "list");
                return ui.getListMessage(tasks.getTasks());
            case ON:
                LocalDate date = parser.parseDate(input);
                return ui.getTasksOnDateMessage(date, tasks.getTasksOnDate(date));
            case FIND:
                String keyword = parser.parseFindKeyword(input);
                return ui.getFoundTasksMessage(tasks.find(keyword));
            case MARK:
                int markIndex = parser.parseIndex(input, "mark", tasks.size());
                assert markIndex >= 0 && markIndex < tasks.size()
                        : "Parser must return an index within the task list";
                tasks.get(markIndex).markAsDone();
                saveTasks(tasks);
                return ui.getMarkedMessage(tasks.get(markIndex));
            case UNMARK:
                int unmarkIndex = parser.parseIndex(input, "unmark", tasks.size());
                assert unmarkIndex >= 0 && unmarkIndex < tasks.size()
                        : "Parser must return an index within the task list";
                tasks.get(unmarkIndex).markAsNotDone();
                saveTasks(tasks);
                return ui.getUnmarkedMessage(tasks.get(unmarkIndex));
            case DELETE:
                int deleteIndex = parser.parseIndex(input, "delete", tasks.size());
                assert deleteIndex >= 0 && deleteIndex < tasks.size()
                        : "Parser must return an index within the task list";
                Task deletedTask = tasks.delete(deleteIndex);
                saveTasks(tasks);
                return ui.getDeletedMessage(deletedTask, tasks.size());
            case TODO:
                Task todoTask = parser.parseTodo(input);
                tasks.add(todoTask);
                saveTasks(tasks);
                return ui.getAddedMessage(todoTask, tasks.size());
            case DEADLINE:
                Task deadlineTask = parser.parseDeadline(input);
                tasks.add(deadlineTask);
                saveTasks(tasks);
                return ui.getAddedMessage(deadlineTask, tasks.size());
            case EVENT:
                Task eventTask = parser.parseEvent(input);
                tasks.add(eventTask);
                saveTasks(tasks);
                return ui.getAddedMessage(eventTask, tasks.size());
            case UNKNOWN:
                parser.throwUnknownCommand();
                throw new AssertionError("Unknown-command parser did not throw an exception");
            default:
                throw new AssertionError("Unhandled command type: " + commandType);
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

}
