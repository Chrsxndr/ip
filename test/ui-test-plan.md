# Clarry UI test plan

Run each case as a new Clarry session. Expected output includes the startup banner and all responses in the session.

## Task types, completion status, and listing

Aim: Verify that todo, deadline, and event tasks have the correct type-specific display, and that marking and unmarking update the stored task.

### Inputs

```text
todo read book
deadline submit assignment /by Friday
event project meeting /from 2pm /to 4pm
mark 2
unmark 2
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
   [D][ ] submit assignment (by: Friday)
 Now you have 2 tasks in the list.
____________________________________________________________
____________________________________________________________
 Got it. I've added this task:
   [E][ ] project meeting (from: 2pm to: 4pm)
 Now you have 3 tasks in the list.
____________________________________________________________
____________________________________________________________
 Nice! I've marked this task as done:
   [D][X] submit assignment (by: Friday)
____________________________________________________________
____________________________________________________________
 OK, I've marked this task as not done yet:
   [D][ ] submit assignment (by: Friday)
____________________________________________________________
____________________________________________________________
 Here are the tasks in your list:
 1.[T][ ] read book
 2.[D][ ] submit assignment (by: Friday)
 3.[E][ ] project meeting (from: 2pm to: 4pm)
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
event meeting /from 2pm
mark
mark abc
mark 1
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
 OOPS!!! A deadline needs a description and a '/by' date.
____________________________________________________________
____________________________________________________________
 OOPS!!! An event needs a description, '/from', and '/to' time.
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
 OOPS!!! I'm sorry, but I don't know what that means :-(
____________________________________________________________
____________________________________________________________
 Bye. Hope to see you again soon!
____________________________________________________________
```
