import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Provides the command-line interface for the Clarry task manager.
 */
public class Clarry {
    private static final Path DATA_FILE = Paths.get("data", "clarry.txt");

    public static void main(String[] args) {
        String banner = "  _____ _\n"
                        + " / ____| |\n"
                        + "| |    | | __ _ _ __ _ __ _   _\n"
                        + "| |    | |/ _` | '__| '__| | | |\n"
                        + "| |____| | (_| | |  | |  | |_| |\n"
                        + " \\_____|_|\\__,_|_|  |_|  \\__,  |\n"
                        + "                          __/  |\n"
                        + "                         |____/\n";
        System.out.println(banner);
        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm Clarry.");
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");

        Scanner scanner = new Scanner(System.in);
        List<Task> tasks = loadTasks();

        commandLoop:
        while (true) {
            String input = scanner.nextLine();

            try {
                CommandType commandType = CommandType.fromWord(getCommandWord(input));
                switch (commandType) {
                case BYE:
                    if (!input.equals("bye")) {
                        throwUnknownCommand();
                    }
                    System.out.println("____________________________________________________________");
                    System.out.println(" Bye. Hope to see you again soon!");
                    System.out.println("____________________________________________________________");
                    break commandLoop;
                case LIST:
                    if (!input.equals("list")) {
                        throwUnknownCommand();
                    }
                    System.out.println("____________________________________________________________");
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println(" " + (i + 1) + "." + tasks.get(i));
                    }
                    System.out.println("____________________________________________________________");
                    break;
                case ON:
                    LocalDate date = parseDate(input, "on");
                    System.out.println("____________________________________________________________");
                    System.out.println(" Here are the tasks on " + date + ":");
                    int displayedTaskCount = 0;
                    for (Task task : tasks) {
                        if ((task instanceof Deadline && ((Deadline) task).occursOn(date))
                                || (task instanceof Event && ((Event) task).occursOn(date))) {
                            displayedTaskCount++;
                            System.out.println(" " + displayedTaskCount + "." + task);
                        }
                    }
                    System.out.println("____________________________________________________________");
                    break;
                case MARK:
                    int markIndex = parseIndex(input, "mark", tasks.size());
                    tasks.get(markIndex).markAsDone();
                    saveTasks(tasks);
                    System.out.println("____________________________________________________________");
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   " + tasks.get(markIndex));
                    System.out.println("____________________________________________________________");
                    break;
                case UNMARK:
                    int unmarkIndex = parseIndex(input, "unmark", tasks.size());
                    tasks.get(unmarkIndex).markAsNotDone();
                    saveTasks(tasks);
                    System.out.println("____________________________________________________________");
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   " + tasks.get(unmarkIndex));
                    System.out.println("____________________________________________________________");
                    break;
                case DELETE:
                    int deleteIndex = parseIndex(input, "delete", tasks.size());
                    Task deletedTask = tasks.remove(deleteIndex);
                    saveTasks(tasks);
                    printDeleted(deletedTask, tasks.size());
                    break;
                case TODO:
                    String description = input.length() > 4 ? input.substring(5).trim() : "";
                    if (description.isEmpty()) {
                        throw new ClarryException("OOPS!!! The description of a todo cannot be empty.");
                    }
                    Task todoTask = new Todo(description);
                    tasks.add(todoTask);
                    saveTasks(tasks);
                    printAdded(todoTask, tasks.size());
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
                    printAdded(deadlineTask, tasks.size());
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
                    printAdded(eventTask, tasks.size());
                    break;
                case UNKNOWN:
                    throwUnknownCommand();
                    break;
                }
            } catch (ClarryException e) {
                System.out.println("____________________________________________________________");
                System.out.println(" " + e.getMessage());
                System.out.println("____________________________________________________________");
            } catch (NumberFormatException e) {
                System.out.println("____________________________________________________________");
                System.out.println(" OOPS!!! Please provide a valid task number.");
                System.out.println("____________________________________________________________");
            }
        }

        scanner.close();
    }

    /**
     * Saves all tasks to Clarry's relative data file.
     *
     * @param tasks tasks to save
     */
    private static void saveTasks(List<Task> tasks) {
        File file = DATA_FILE.toFile();
        File parentDirectory = file.getParentFile();
        if (parentDirectory != null && !parentDirectory.exists() && !parentDirectory.mkdirs()) {
            printSaveError();
            return;
        }

        try (FileWriter writer = new FileWriter(file)) {
            for (Task task : tasks) {
                writer.write(task.toFileFormat() + System.lineSeparator());
            }
        } catch (IOException e) {
            printSaveError();
        }
    }

    /**
     * Loads the saved tasks, skipping any corrupted save-file lines.
     *
     * @return tasks reconstructed from the save file, or an empty list if it is absent
     */
    private static List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        File file = DATA_FILE.toFile();
        if (!file.exists()) {
            return tasks;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    tasks.add(parseSavedTask(line));
                } catch (Exception e) {
                    printCorruptedLineError();
                }
            }
        } catch (IOException e) {
            System.out.println("____________________________________________________________");
            System.out.println(" OOPS!!! Could not load saved tasks.");
            System.out.println("____________________________________________________________");
        }
        return tasks;
    }

    /**
     * Reconstructs one task from a line in Clarry's save-file format.
     *
     * @param line saved task data
     * @return reconstructed task
     * @throws IllegalArgumentException if the saved data is structurally invalid
     * @throws ClarryException if a saved deadline date is invalid
     */
    private static Task parseSavedTask(String line) throws ClarryException {
        String[] parts = line.split(" \\| ", -1);
        if (parts.length < 3 || !(parts[1].equals("0") || parts[1].equals("1"))
                || parts[2].isEmpty()) {
            throw new IllegalArgumentException("Invalid task data");
        }

        Task task;
        switch (parts[0]) {
        case "T":
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid todo data");
            }
            task = new Todo(parts[2]);
            break;
        case "D":
            if (parts.length != 4 || parts[3].isEmpty()) {
                throw new IllegalArgumentException("Invalid deadline data");
            }
            task = new Deadline(parts[2], parts[3]);
            break;
        case "E":
            if (parts.length != 5 || parts[3].isEmpty() || parts[4].isEmpty()) {
                throw new IllegalArgumentException("Invalid event data");
            }
            task = new Event(parts[2], parts[3], parts[4]);
            break;
        default:
            throw new IllegalArgumentException("Unknown task type");
        }

        if (parts[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /** Prints Clarry's message for a failed save operation. */
    private static void printSaveError() {
        System.out.println("____________________________________________________________");
        System.out.println(" OOPS!!! Could not save tasks to disk.");
        System.out.println("____________________________________________________________");
    }

    /** Prints Clarry's message for a malformed saved task. */
    private static void printCorruptedLineError() {
        System.out.println("____________________________________________________________");
        System.out.println(" OOPS!!! Skipping a corrupted line in the save file.");
        System.out.println("____________________________________________________________");
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
    /**
     * Prints confirmation that a task was added.
     *
     * @param task task that was added
     * @param taskCount current number of tasks
     */
    private static void printAdded(Task task, int taskCount) {
        System.out.println("____________________________________________________________");
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }

    /**
     * Prints confirmation that a task was deleted.
     *
     * @param task task that was deleted
     * @param taskCount current number of tasks
     */
    private static void printDeleted(Task task, int taskCount) {
        System.out.println("____________________________________________________________");
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }
}
