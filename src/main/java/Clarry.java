import java.util.Scanner;

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
        Task[] tasks = new Task[100];
        int taskCount = 0;

        while (true) {
            String input = scanner.nextLine();

            try {
                if (input.equals("bye")) {
                    System.out.println("____________________________________________________________");
                    System.out.println(" Bye. Hope to see you again soon!");
                    System.out.println("____________________________________________________________");
                    break;
                } else if (input.equals("list")) {
                    System.out.println("____________________________________________________________");
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println(" " + (i + 1) + "." + tasks[i]);
                    }
                    System.out.println("____________________________________________________________");
                } else if (input.equals("mark") || input.startsWith("mark ")) {
                    int index = parseIndex(input, "mark", taskCount);
                    tasks[index].markAsDone();
                    System.out.println("____________________________________________________________");
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   " + tasks[index]);
                    System.out.println("____________________________________________________________");
                } else if (input.equals("unmark") || input.startsWith("unmark ")) {
                    int index = parseIndex(input, "unmark", taskCount);
                    tasks[index].markAsNotDone();
                    System.out.println("____________________________________________________________");
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   " + tasks[index]);
                    System.out.println("____________________________________________________________");
                } else if (input.equals("todo") || input.startsWith("todo ")) {
                    String description = input.length() > 4 ? input.substring(5).trim() : "";
                    if (description.isEmpty()) {
                        throw new ClarryException("OOPS!!! The description of a todo cannot be empty.");
                    }
                    Task task = new Todo(description);
                    tasks[taskCount++] = task;
                    printAdded(task, taskCount);
                } else if (input.equals("deadline") || input.startsWith("deadline ")) {
                    String rest = input.length() > 8 ? input.substring(9).trim() : "";
                    String[] parts = rest.split(" /by ", 2);
                    if (parts.length != 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
                        throw new ClarryException("OOPS!!! A deadline needs a description and a '/by' date.");
                    }
                    Task task = new Deadline(parts[0].trim(), parts[1].trim());
                    tasks[taskCount++] = task;
                    printAdded(task, taskCount);
                } else if (input.equals("event") || input.startsWith("event ")) {
                    String rest = input.length() > 5 ? input.substring(6).trim() : "";
                    String[] fromSplit = rest.split(" /from ", 2);
                    String[] toSplit = fromSplit.length == 2 ? fromSplit[1].split(" /to ", 2) : new String[0];
                    if (fromSplit.length != 2 || toSplit.length != 2 || fromSplit[0].trim().isEmpty()
                            || toSplit[0].trim().isEmpty() || toSplit[1].trim().isEmpty()) {
                        throw new ClarryException("OOPS!!! An event needs a description, '/from', and '/to' time.");
                    }
                    Task task = new Event(fromSplit[0].trim(), toSplit[0].trim(), toSplit[1].trim());
                    tasks[taskCount++] = task;
                    printAdded(task, taskCount);
                } else {
                    throw new ClarryException("OOPS!!! I'm sorry, but I don't know what that means :-(");
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
     * Converts a task number in a mark or unmark command to a zero-based index.
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
    private static void printAdded(Task t, int taskCount) {
        System.out.println("____________________________________________________________");
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }
}
