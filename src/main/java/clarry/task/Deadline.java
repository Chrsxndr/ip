package clarry.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import clarry.ClarryException;

/**
 * Represents a task that must be completed by a deadline.
 */
public class Deadline extends Task {
    private final LocalDate by;

    /**
     * Creates an incomplete deadline task.
     *
     * @param description description of the task
     * @param by deadline date in {@code yyyy-MM-dd} format
     * @throws ClarryException if the deadline date is invalid
     */
    public Deadline(String description, String by) throws ClarryException {
        super(description);
        try {
            this.by = LocalDate.parse(by);
        } catch (DateTimeParseException e) {
            throw new ClarryException(
                    "OOPS!!! Please use yyyy-mm-dd for the deadline date, e.g. 2019-10-15.");
        }
    }

    @Override
    public String toString() {
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("MMM dd yyyy");
        return "[D][" + getStatusIcon() + "] " + getDescription() + " (by: " + by.format(outputFormat) + ")";
    }

    @Override
    public String toFileFormat() {
        return "D | " + (isDone() ? "1" : "0") + " | " + getDescription() + " | " + by;
    }

    /**
     * Returns whether this deadline falls on the specified date.
     *
     * @param date date to check
     * @return whether the deadline is on the date
     */
    public boolean occursOn(LocalDate date) {
        return by.equals(date);
    }
}
