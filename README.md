# Clarry

Clarry is a simple command-line task manager written in Java. It helps you keep track of todos, deadlines, and events without leaving the terminal.

## Requirements

- Java Development Kit (JDK) 25
- IntelliJ IDEA (recommended)

## Getting started

1. Open IntelliJ IDEA and select **File > Open**.
1. Choose this project folder and accept the default import options.
1. Configure the project SDK to use **JDK 25**. Set the project language level to **SDK default**.
1. Open [Clarry.java](src/main/java/Clarry.java), then right-click it and choose **Run `Clarry.main()`**.

You should see Clarry's welcome banner in the Run console. Type a command and press Enter. Enter `bye` when you are finished.

```text
  _____ _
 / ____| |
| |    | | __ _ _ __ _ __ _   _
| |    | |/ _` | '__| '__| | | |
| |____| | (_| | |  | |  | |_| |
 \_____|_|\__,_|_|  |_|  \__,  |
                          __/  |
                         |____/
```

## Commands

| Command | Example | Purpose |
| --- | --- | --- |
| `todo DESCRIPTION` | `todo read book` | Adds a todo task. |
| `deadline DESCRIPTION /by DATE` | `deadline submit assignment /by Friday` | Adds a task with a deadline. |
| `event DESCRIPTION /from START /to END` | `event project meeting /from 2pm /to 4pm` | Adds an event. |
| `list` | `list` | Shows all tasks. |
| `mark NUMBER` | `mark 2` | Marks a task as complete. |
| `unmark NUMBER` | `unmark 2` | Marks a task as incomplete. |
| `delete NUMBER` | `delete 2` | Removes a task. |
| `bye` | `bye` | Closes Clarry. |

Task numbers shown by `list` start at 1.

## Saved tasks

Clarry automatically saves tasks after every change in `data/clarry.txt` and reloads them next time it starts. The `data` folder is created automatically when needed and is intentionally not tracked by Git, so your personal task list will not be committed to the project.

If the save file is missing, Clarry starts with an empty list. If one saved line is malformed, Clarry skips that line and loads the remaining valid tasks.

## Running from the terminal

From the project root, compile and run the program with:

```powershell
javac -d out src/main/java/*.java
java -cp out Clarry
```

The generated `out` folder contains compiled files and can be deleted safely.
