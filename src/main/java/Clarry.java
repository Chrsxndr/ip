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
                        + "                         |____/ \n";
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

            if (input.equals("bye")) {
                System.out.println("____________________________________________________________");
                System.out.println(" Bye. Hope to see you again soon!");
                System.out.println("____________________________________________________________");
                break;
            } else if (input.equals("list")) {
                System.out.println("____________________________________________________________");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + "." + tasks[i]);
                }
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("mark ")) {
                int index = Integer.parseInt(input.substring(5)) - 1;
                tasks[index].markAsDone();
                System.out.println("____________________________________________________________");
                System.out.println(" Nice! I've marked this task as done:");
                System.out.println("   " + tasks[index]);
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("unmark ")) {
                int index = Integer.parseInt(input.substring(7)) - 1;
                tasks[index].markAsNotDone();
                System.out.println("____________________________________________________________");
                System.out.println(" OK, I've marked this task as not done yet:");
                System.out.println("   " + tasks[index]);
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("todo ")) {
                Task t = new Todo(input.substring(5));
                tasks[taskCount] = t;
                taskCount++;
                printAdded(t, taskCount);
            } else if (input.startsWith("deadline ")) {
                String rest = input.substring(9);
                String[] parts = rest.split(" /by ");
                Task t = new Deadline(parts[0], parts[1]);
                tasks[taskCount] = t;
                taskCount++;
                printAdded(t, taskCount);
            } else if (input.startsWith("event ")) {
                String rest = input.substring(6);
                String[] fromSplit = rest.split(" /from ");
                String[] toSplit = fromSplit[1].split(" /to ");
                Task t = new Event(fromSplit[0], toSplit[0], toSplit[1]);
                tasks[taskCount] = t;
                taskCount++;
                printAdded(t, taskCount);
            } else {
                Task t = new Todo(input);
                tasks[taskCount] = t;
                taskCount++;
                printAdded(t, taskCount);
            }
        }

        scanner.close();
    }
    private static void printAdded(Task t, int taskCount) {
        System.out.println("____________________________________________________________");
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + taskCount + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }
}
