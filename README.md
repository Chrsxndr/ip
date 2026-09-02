# Clarry

Clarry is a simple command-line task manager written in Java. It helps you keep track of todos, deadlines, and events without leaving the terminal.

## Requirements

- Java Development Kit (JDK) 25
- IntelliJ IDEA (recommended)

## Running in IntelliJ IDEA

1. Open IntelliJ IDEA and select **File > Open**.
2. Choose this project folder and import it as a Gradle project when prompted.
3. Configure the project SDK to use **JDK 25** and set the language level to **SDK default**.
4. Open [Clarry.java](src/main/java/clarry/Clarry.java), then click the green arrow beside the `main` method.

The fully qualified main class is `clarry.Clarry`. If IntelliJ reports that it cannot find `Clarry`, open
**Run > Edit Configurations** and set **Main class** to `clarry.Clarry`. Also ensure that the project module is
selected under **Use classpath of module**.

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
| `deadline DESCRIPTION /by DATE` | `deadline submit assignment /by 2026-09-01` | Adds a task with a deadline. |
| `event DESCRIPTION /from START /to END` | `event meeting /from 2026-09-01 14:00 /to 2026-09-01 16:00` | Adds an event. |
| `list` | `list` | Shows all tasks. |
| `find KEYWORD` | `find book` | Finds tasks containing a keyword, ignoring letter case. |
| `on DATE` | `on 2026-09-01` | Shows deadlines and events occurring on a date. |
| `mark NUMBER` | `mark 2` | Marks a task as complete. |
| `unmark NUMBER` | `unmark 2` | Marks a task as incomplete. |
| `delete NUMBER` | `delete 2` | Removes a task. |
| `bye` | `bye` | Closes Clarry. |

Task numbers shown by `list` start at 1.
Dates use `yyyy-MM-dd`, while event date-times use `yyyy-MM-dd HH:mm`.

## Saved tasks

Clarry automatically saves tasks after every change in `data/clarry.txt` and reloads them next time it starts. The `data` folder is created automatically when needed and is intentionally not tracked by Git, so your personal task list will not be committed to the project.

If the save file is missing, Clarry starts with an empty list. If one saved line is malformed, Clarry skips that line and loads the remaining valid tasks.

## Running with Gradle

From the project root on Windows, run:

```powershell
.\gradlew.bat run
```

On macOS or Linux, run `./gradlew run` instead.

To compile the application and run its JUnit tests:

```powershell
.\gradlew.bat build
```

## Creating and running the JAR

Create the executable fat JAR with:

```powershell
.\gradlew.bat shadowJar
```

Gradle creates `build/libs/clarry.jar`. Run it with Java 25:

```powershell
java -jar "build\libs\clarry.jar"
```

The `build` directory contains generated files and should not be committed to Git.
