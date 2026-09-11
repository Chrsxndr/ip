# Clarry UI test plan

Run each case as a new Clarry session. Before each case, remove `data/clarry.txt` so saved tasks from a previous session do not affect the result. Expected output includes the startup banner and all responses in the session.

## Help command

Aim: Verify that help lists every supported command, does not add tasks, and rejects extra arguments.

### Inputs

```text
help
list
help extra
bye
```

### Expected output

```text
  _____ _
 / ____| |
| |    | | __ _ _ __ _ __ _   _
| |    | |/ _` | '__| '__| | | |
| |____| | (_| | |  | |  | |_| |
 \_____|_|\__,_|_|  |_|  \__,  |
                          __/  |
                         |____/

____________________________________________________________
 Hello! I'm Clarry.
 What can I do for you?
____________________________________________________________
____________________________________________________________
 Here are the commands I understand:
 help - show this help message
 list - show all tasks
 todo DESCRIPTION - add a todo
 deadline DESCRIPTION /by YYYY-MM-DD - add a deadline
 event DESCRIPTION /from YYYY-MM-DD HH:mm /to YYYY-MM-DD HH:mm - add an event
 on YYYY-MM-DD - show tasks occurring on a date
 find KEYWORD - find tasks by description
 mark NUMBER - mark a task as done
 unmark NUMBER - mark a task as not done
 delete NUMBER - delete a task
 bye - exit Clarry
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
____________________________________________________________
____________________________________________________________
 OOPS!!! I'm sorry, but I don't know what that means :-(
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## Task types, completion status, and listing

Aim: Verify task-type display, completion updates, deletion, and renumbering of the remaining tasks.

### Inputs

```text
todo read book
deadline submit assignment /by 2019-10-15
event project meeting /from 2019-10-15 14:00 /to 2019-10-16 16:00
mark 2
unmark 2
list
find BOOK
on 2019-10-16
delete 2
list
bye
```

### Expected output

```text
  _____ _
 / ____| |
| |    | | __ _ _ __ _ __ _   _
| |    | |/ _` | '__| '__| | | |
| |____| | (_| | |  | |  | |_| |
 \_____|_|\__,_|_|  |_|  \__,  |
                          __/  |
                         |____/

____________________________________________________________
 Hello! I'm Clarry.
 What can I do for you?
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [T][ ] read book
 Now you have 1 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [D][ ] submit assignment (by: Oct 15 2019)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: Oct 15 2019 14:00 to: Oct 16 2019 16:00)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] submit assignment (by: Oct 15 2019)
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [D][ ] submit assignment (by: Oct 15 2019)
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[D][ ] submit assignment (by: Oct 15 2019)
 3.[E][ ] project meeting (from: Oct 15 2019 14:00 to: Oct 16 2019 16:00)
____________________________________________________________
____________________________________________________________
 Here are the matching tasks in your list:
 1.[T][ ] read book
____________________________________________________________
____________________________________________________________
 Here are the tasks on 2019-10-16:
 1.[E][ ] project meeting (from: Oct 15 2019 14:00 to: Oct 16 2019 16:00)
____________________________________________________________
____________________________________________________________
 Noted. I've removed this task:
   [D][ ] submit assignment (by: Oct 15 2019)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[E][ ] project meeting (from: Oct 15 2019 14:00 to: Oct 16 2019 16:00)
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```

## Invalid commands and task details

Aim: Verify that invalid commands and incomplete task details show an error message and that Clarry continues accepting commands.

### Inputs

```text
todo
deadline return book
deadline return book /by Friday
event meeting /from 2pm
event meeting /from 2019-02-29 14:00 /to 2019-02-29 16:00
event meeting /from 2019-10-15 14:00 /to 2019-10-15 13:00
on Friday
mark
mark abc
mark 1
delete
delete 1
find
nonsense
bye
```

### Expected output

```text
  _____ _
 / ____| |
| |    | | __ _ _ __ _ __ _   _
| |    | |/ _` | '__| '__| | | |
| |____| | (_| | |  | |  | |_| |
 \_____|_|\__,_|_|  |_|  \__,  |
                          __/  |
                         |____/

____________________________________________________________
 Hello! I'm Clarry.
 What can I do for you?
____________________________________________________________
____________________________________________________________
 OOPS!!! The description of a todo cannot be empty.
____________________________________________________________
____________________________________________________________
 OOPS!!! A deadline needs a '/by' date, e.g. deadline return book /by 2019-10-15
____________________________________________________________
____________________________________________________________
 OOPS!!! Please use yyyy-mm-dd for the deadline date, e.g. 2019-10-15.
____________________________________________________________
____________________________________________________________
 OOPS!!! An event needs a description, '/from', and '/to' date and time.
____________________________________________________________
____________________________________________________________
 OOPS!!! Please use yyyy-mm-dd HH:mm for event dates, e.g. 2019-10-15 14:00.
____________________________________________________________
____________________________________________________________
 OOPS!!! An event cannot end before it starts.
____________________________________________________________
____________________________________________________________
 OOPS!!! Please use yyyy-mm-dd for the date, e.g. on 2019-10-15.
____________________________________________________________
____________________________________________________________
 OOPS!!! Please specify which task number to mark.
____________________________________________________________
____________________________________________________________
 OOPS!!! Please provide a valid task number.
____________________________________________________________
____________________________________________________________
 OOPS!!! That task number doesn't exist.
____________________________________________________________
____________________________________________________________
 OOPS!!! Please specify which task number to delete.
____________________________________________________________
____________________________________________________________
 OOPS!!! That task number doesn't exist.
____________________________________________________________
____________________________________________________________
 OOPS!!! Please specify a keyword to find.
____________________________________________________________
____________________________________________________________
 OOPS!!! I'm sorry, but I don't know what that means :-(
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```
