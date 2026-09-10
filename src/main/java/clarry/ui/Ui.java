package clarry.ui;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import clarry.task.Task;

/**
 * Handles all console input and output for Clarry.
 */
public class Ui {
    private static final String DIVIDER = "____________________________________________________________";
    private final Scanner scanner;

    /** Creates a UI that reads commands from standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Displays Clarry's welcome message. */
    public void showWelcome() {
        String banner = "  _____ _\n"
                + " / ____| |\n"
                + "| |    | | __ _ _ __ _ __ _   _\n"
                + "| |    | |/ _` | '__| '__| | | |\n"
                + "| |____| | (_| | |  | |  | |_| |\n"
                + " \\_____|_|\\__,_|_|  |_|  \\__,  |\n"
                + "                          __/  |\n"
                + "                         |____/\n";
        printLines(
                banner,
                DIVIDER,
                " Hello! I'm Clarry.",
                " What can I do for you?",
                DIVIDER);
    }

    /** Reads one full command line from the user. */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Displays an error message. */
    public void showError(String message) {
        showResponse(getErrorMessage(message));
    }

    /** Displays response text between the standard divider lines. */
    public void showResponse(String response) {
        printLines(DIVIDER, response, DIVIDER);
    }

    /** Returns Clarry's goodbye response. */
    public String getGoodbyeMessage() {
        return " Bye. Hope to see you again soon!";
    }

    /** Returns a formatted list of all tasks. */
    public String getListMessage(List<Task> tasks) {
        return getTaskListMessage(" Here are the tasks in your list:", tasks);
    }

    /** Returns a formatted list of tasks on the specified date. */
    public String getTasksOnDateMessage(LocalDate date, List<Task> tasks) {
        return getTaskListMessage(" Here are the tasks on " + date + ":", tasks);
    }

    /** Returns a formatted list of tasks matching a search. */
    public String getFoundTasksMessage(List<Task> tasks) {
        return getTaskListMessage(" Here are the matching tasks in your list:", tasks);
    }

    /** Returns a task-addition response. */
    public String getAddedMessage(Task task, int taskCount) {
        return " Got it. I've added this task:\n"
                + "   " + task + "\n"
                + " Now you have " + taskCount + " tasks in the list.";
    }

    /** Returns a task-deletion response. */
    public String getDeletedMessage(Task task, int taskCount) {
        return " Noted. I've removed this task:\n"
                + "   " + task + "\n"
                + " Now you have " + taskCount + " tasks in the list.";
    }

    /** Returns a task-completion response. */
    public String getMarkedMessage(Task task) {
        return " Nice! I've marked this task as done:\n   " + task;
    }

    /** Returns a task-incomplete response. */
    public String getUnmarkedMessage(Task task) {
        return " OK, I've marked this task as not done yet:\n   " + task;
    }

    /** Returns an error response. */
    public String getErrorMessage(String message) {
        return " " + message;
    }

    /** Displays an error when saving fails. */
    public void showSaveError() {
        showError("OOPS!!! Could not save tasks to disk.");
    }

    /** Displays an error when loading fails. */
    public void showLoadError() {
        showError("OOPS!!! Could not load saved tasks.");
    }

    /** Displays an error for a malformed saved task. */
    public void showCorruptedLineError() {
        showError("OOPS!!! Skipping a corrupted line in the save file.");
    }

    /** Builds a numbered task-list response with the supplied heading. */
    private String getTaskListMessage(String heading, List<Task> tasks) {
        StringBuilder message = new StringBuilder(heading);
        for (int i = 0; i < tasks.size(); i++) {
            message.append("\n ").append(i + 1).append(".").append(tasks.get(i));
        }
        return message.toString();
    }

    /** Prints each supplied line in the order received. */
    private void printLines(String... lines) {
        for (String line : lines) {
            System.out.println(line);
        }
    }
}
