package clarry.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import clarry.ClarryException;

/** Tests task collection and date-filtering behavior. */
class TaskListTest {
    @Test
    void getTasksOnDate_returnsMatchingDeadlineAndEventInOriginalOrder() throws ClarryException {
        LocalDate date = LocalDate.of(2026, 8, 27);
        Deadline deadline = new Deadline("submit report", "2026-08-27");
        Todo todo = new Todo("buy milk");
        Event event = new Event("team meeting", "2026-08-27 10:00", "2026-08-27 11:00");
        TaskList taskList = new TaskList(List.of(deadline, todo, event));

        assertEquals(List.of(deadline, event), taskList.getTasksOnDate(date));
    }

    @Test
    void getTasksOnDate_withoutMatchingTasks_returnsEmptyList() throws ClarryException {
        TaskList taskList = new TaskList(List.of(new Todo("buy milk")));

        assertEquals(List.of(), taskList.getTasksOnDate(LocalDate.of(2026, 8, 27)));
    }

    @Test
    void getTasks_returnsUnmodifiableSnapshot() throws ClarryException {
        TaskList taskList = new TaskList(List.of(new Todo("buy milk")));

        assertThrows(UnsupportedOperationException.class,
                () -> taskList.getTasks().add(new Todo("read book")));
        assertEquals(1, taskList.size());
    }
}
