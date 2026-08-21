import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Provides the command-line interface for the Clarry task manager.
 */
public class Clarry {
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
        List<Task> tasks = new ArrayList<>();

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
                case MARK:
                    int markIndex = parseIndex(input, "mark", tasks.size());
                    tasks.get(markIndex).markAsDone();
                    System.out.println("____________________________________________________________");
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   " + tasks.get(markIndex));
                    System.out.println("____________________________________________________________");
                    break;
                case UNMARK:
                    int unmarkIndex = parseIndex(input, "unmark", tasks.size());
                    tasks.get(unmarkIndex).markAsNotDone();
                    System.out.println("____________________________________________________________");
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   " + tasks.get(unmarkIndex));
                    System.out.println("____________________________________________________________");
                    break;
                case DELETE:
                    int deleteIndex = parseIndex(input, "delete", tasks.size());
                    Task deletedTask = tasks.remove(deleteIndex);
                    printDeleted(deletedTask, tasks.size());
                    break;
                case TODO:
                    String description = input.length() > 4 ? input.substring(5).trim() : "";
                    if (description.isEmpty()) {
                        throw new ClarryException("OOPS!!! The description of a todo cannot be empty.");
                    }
                    Task todoTask = new Todo(description);
                    tasks.add(todoTask);
                    printAdded(todoTask, tasks.size());
                    break;
                case DEADLINE:
                    String deadlineDetails = input.length() > 8 ? input.substring(9).trim() : "";
                    String[] deadlineParts = deadlineDetails.split(" /by ", 2);
                    if (deadlineParts.length != 2 || deadlineParts[0].trim().isEmpty()
                            || deadlineParts[1].trim().isEmpty()) {
                        throw new ClarryException("OOPS!!! A deadline needs a description and a '/by' date.");
                    }
                    Task deadlineTask = new Deadline(deadlineParts[0].trim(), deadlineParts[1].trim());
                    tasks.add(deadlineTask);
                    printAdded(deadlineTask, tasks.size());
                    break;
                case EVENT:
                    String eventDetails = input.length() > 5 ? input.substring(6).trim() : "";
                    String[] fromSplit = eventDetails.split(" /from ", 2);
                    String[] toSplit = fromSplit.length == 2 ? fromSplit[1].split(" /to ", 2) : new String[0];
                    if (fromSplit.length != 2 || toSplit.length != 2 || fromSplit[0].trim().isEmpty()
                            || toSplit[0].trim().isEmpty() || toSplit[1].trim().isEmpty()) {
                        throw new ClarryException("OOPS!!! An event needs a description, '/from', and '/to' time.");
                    }
                    Task eventTask = new Event(fromSplit[0].trim(), toSplit[0].trim(), toSplit[1].trim());
                    tasks.add(eventTask);
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
