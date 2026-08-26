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

    @Override
    public String toString() {
        return "[T][" + getStatusIcon() + "] " + getDescription();
    }

    @Override
    public String toFileFormat() {
        return "T | " + (isDone() ? "1" : "0") + " | " + getDescription();
    }
}
