import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

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
        System.out.println(banner);
        showDivider();
        System.out.println(" Hello! I'm Clarry.");
        System.out.println(" What can I do for you?");
        showDivider();
    }

    /** Reads one full command line from the user. */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Displays the standard divider line. */
    public void showDivider() {
        System.out.println(DIVIDER);
    }

    /** Displays Clarry's goodbye message. */
    public void showGoodbye() {
        showDivider();
        System.out.println(" Bye. Hope to see you again soon!");
        showDivider();
    }

    /** Displays all tasks in the task list. */
    public void showList(List<Task> tasks) {
        showDivider();
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
        showDivider();
    }

    /** Displays the tasks that occur on a specified date. */
    public void showTasksOnDate(LocalDate date, List<Task> tasks) {
        showDivider();
        System.out.println(" Here are the tasks on " + date + ":");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
        showDivider();
    }

    /** Displays a task addition confirmation. */
    public void showAdded(Task task, int taskCount) {
        showDivider();
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
        showDivider();
    }

    /** Displays a task deletion confirmation. */
    public void showDeleted(Task task, int taskCount) {
        showDivider();
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
        showDivider();
    }

    /** Displays a task completion confirmation. */
    public void showMarked(Task task) {
        showDivider();
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
        showDivider();
    }

    /** Displays a task incomplete confirmation. */
    public void showUnmarked(Task task) {
        showDivider();
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
        showDivider();
    }

    /** Displays an error message. */
    public void showError(String message) {
        showDivider();
        System.out.println(" " + message);
        showDivider();
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
}
