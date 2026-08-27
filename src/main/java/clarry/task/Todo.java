package clarry.task;

/**
 * Represents a basic task without a date or time.
 */
public class Todo extends Task {
    /**
     * Creates an incomplete todo task.
     *
     * @param description description of the task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns this todo in the format shown in Clarry's task list.
     *
     * @return formatted todo description and completion status
     */
    @Override
    public String toString() {
        return "[T][" + getStatusIcon() + "] " + getDescription();
    }

    /**
     * Returns this todo in Clarry's persistent storage format.
     *
     * @return pipe-separated todo data
     */
    @Override
    public String toFileFormat() {
        return "T | " + (isDone() ? "1" : "0") + " | " + getDescription();
    }
}
