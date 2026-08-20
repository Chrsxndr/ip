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
        String[] tasks = new String[100];
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
                    System.out.println(" " + (i + 1) + ". " + tasks[i]);
                }
                System.out.println("____________________________________________________________");
            } else {
                tasks[taskCount] = input;
                taskCount++;
                System.out.println("____________________________________________________________");
                System.out.println(" added: " + input);
                System.out.println("____________________________________________________________");
            }
        }

        scanner.close();
    }
}
