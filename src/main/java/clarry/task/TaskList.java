package clarry.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the tasks currently known to Clarry.
 */
public class TaskList implements Iterable<Task> {
    private final List<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the supplied tasks.
     *
     * @param tasks tasks to add to the new list
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the specified zero-based index.
     *
     * @param index zero-based index of the task to remove
     * @return removed task
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the task at the specified zero-based index.
     *
     * @param index zero-based index of the task
     * @return task at the index
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns an unmodifiable snapshot of all tasks.
     *
     * @return all tasks in list order
     */
    public List<Task> getTasks() {
        return List.copyOf(tasks);
    }

    /**
     * Returns every deadline or event that occurs on a given date.
     *
     * @param date date to check
     * @return tasks occurring on the date, in list order
     */
    public List<Task> getTasksOnDate(LocalDate date) {
        List<Task> tasksOnDate = new ArrayList<>();
        for (Task task : tasks) {
            if ((task instanceof Deadline && ((Deadline) task).occursOn(date))
                    || (task instanceof Event && ((Event) task).occursOn(date))) {
                tasksOnDate.add(task);
            }
        }
        return tasksOnDate;
    }

    /**
     * Returns an iterator over a snapshot of the tasks in list order.
     *
     * @return task iterator
     */
    @Override
    public java.util.Iterator<Task> iterator() {
        return getTasks().iterator();
    }
}
