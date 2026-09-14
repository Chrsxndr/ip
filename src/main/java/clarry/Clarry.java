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
        return getCommandResponse(input).text();
    }

    /**
     * Executes a command and includes its status for GUI presentation.
     *
     * @param input command entered by the user
     * @return reply text and whether the command failed
     */
    public Response getCommandResponse(String input) {
        initialiseTasks();
        try {
            return new Response(executeCommand(input), false);
        } catch (ClarryException e) {
            return new Response(ui.getErrorMessage(e.getMessage()), true);
        } catch (NumberFormatException e) {
            return new Response(ui.getErrorMessage("OOPS!!! Please provide a valid task number."), true);
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
            case HELP:
                parser.requireExactCommand(input, "help");
                return ui.getHelpMessage();
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
                return markTask(input);
            case UNMARK:
                return unmarkTask(input);
            case DELETE:
                return deleteTask(input);
            case TODO:
                return addTask(parser.parseTodo(input));
            case DEADLINE:
                return addTask(parser.parseDeadline(input));
            case EVENT:
                return addTask(parser.parseEvent(input));
            case UNKNOWN:
                parser.throwUnknownCommand();
                throw new AssertionError("Unknown-command parser did not throw an exception");
            default:
                throw new AssertionError("Unhandled command type: " + commandType);
        }
    }

    /** Marks the task identified by the command as done. */
    private String markTask(String input) throws ClarryException {
        int taskIndex = parser.parseIndex(input, "mark", tasks.size());
        assert taskIndex >= 0 && taskIndex < tasks.size()
                : "Parser must return an index within the task list";
        Task task = tasks.get(taskIndex);
        task.markAsDone();
        saveTasks(tasks);
        return ui.getMarkedMessage(task);
    }

    /** Marks the task identified by the command as not done. */
    private String unmarkTask(String input) throws ClarryException {
        int taskIndex = parser.parseIndex(input, "unmark", tasks.size());
        assert taskIndex >= 0 && taskIndex < tasks.size()
                : "Parser must return an index within the task list";
        Task task = tasks.get(taskIndex);
        task.markAsNotDone();
        saveTasks(tasks);
        return ui.getUnmarkedMessage(task);
    }

    /** Deletes the task identified by the command. */
    private String deleteTask(String input) throws ClarryException {
        int taskIndex = parser.parseIndex(input, "delete", tasks.size());
        assert taskIndex >= 0 && taskIndex < tasks.size()
                : "Parser must return an index within the task list";
        Task deletedTask = tasks.delete(taskIndex);
        saveTasks(tasks);
        return ui.getDeletedMessage(deletedTask, tasks.size());
    }

    /** Adds a parsed task to the task list. */
    private String addTask(Task task) {
        tasks.add(task);
        saveTasks(tasks);
        return ui.getAddedMessage(task, tasks.size());
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
