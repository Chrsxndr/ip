/**
 * Represents a task and whether it has been completed.
 */
public class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the icon used to show this task's completion status.
     *
     * @return {@code "X"} if the task is done, or a space otherwise
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /** Marks this task as complete. */
    public void markAsDone() {
        this.isDone = true;
    }

    /** Marks this task as incomplete. */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns this task's description.
     *
     * @return task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns a display-friendly representation of this task.
     *
     * @return completion status and description
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
