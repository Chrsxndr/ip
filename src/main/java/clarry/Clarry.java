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

    /** Creates Clarry with its console user interface. */
    public Clarry() {
        ui = new Ui();
        storage = new Storage(DATA_FILE);
        parser = new Parser();
    }

    /** Starts Clarry's command loop. */
    public void run() {
        ui.showWelcome();
        TaskList tasks = loadTasks();

        commandLoop:
        while (true) {
            String input = ui.readCommand();

            try {
                CommandType commandType = parser.parseCommandType(input);
                switch (commandType) {
                case BYE:
                    if (!input.equals("bye")) {
                        parser.requireExactCommand(input, "bye");
                    }
                    ui.showGoodbye();
                    break commandLoop;
                case LIST:
                    if (!input.equals("list")) {
                        parser.requireExactCommand(input, "list");
                    }
                    ui.showList(tasks.getTasks());
                    break;
                case ON:
                    LocalDate date = parser.parseDate(input);
                    ui.showTasksOnDate(date, tasks.getTasksOnDate(date));
                    break;
                case MARK:
                    int markIndex = parser.parseIndex(input, "mark", tasks.size());
                    tasks.get(markIndex).markAsDone();
                    saveTasks(tasks);
                    ui.showMarked(tasks.get(markIndex));
                    break;
                case UNMARK:
                    int unmarkIndex = parser.parseIndex(input, "unmark", tasks.size());
                    tasks.get(unmarkIndex).markAsNotDone();
                    saveTasks(tasks);
                    ui.showUnmarked(tasks.get(unmarkIndex));
                    break;
                case DELETE:
                    int deleteIndex = parser.parseIndex(input, "delete", tasks.size());
                    Task deletedTask = tasks.delete(deleteIndex);
                    saveTasks(tasks);
                    ui.showDeleted(deletedTask, tasks.size());
                    break;
                case TODO:
                    Task todoTask = parser.parseTodo(input);
                    tasks.add(todoTask);
                    saveTasks(tasks);
                    ui.showAdded(todoTask, tasks.size());
                    break;
                case DEADLINE:
                    Task deadlineTask = parser.parseDeadline(input);
                    tasks.add(deadlineTask);
                    saveTasks(tasks);
                    ui.showAdded(deadlineTask, tasks.size());
                    break;
                case EVENT:
                    Task eventTask = parser.parseEvent(input);
                    tasks.add(eventTask);
                    saveTasks(tasks);
                    ui.showAdded(eventTask, tasks.size());
                    break;
                case UNKNOWN:
                    parser.throwUnknownCommand();
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

}
